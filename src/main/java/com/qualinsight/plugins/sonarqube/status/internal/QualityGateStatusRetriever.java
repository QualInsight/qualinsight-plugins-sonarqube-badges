/*
 * qualinsight-plugins-sonarqube-status
 * Copyright (c) 2015, QualInsight
 * http://www.qualinsight.com/
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program. If not, you can retrieve a copy
 * from <http://www.gnu.org/licenses/>.
 */
package com.qualinsight.plugins.sonarqube.status.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;

public class QualityGateStatusRetriever implements ServerExtension {

    private static final String URI_SEPARATOR = "/";

    private final HttpClient httpclient;

    private final ResponseHandler<String> responseHandler;

    private final HttpGet httpGet;

    private final URIBuilder uriBuilder;

    public QualityGateStatusRetriever(final Settings settings) throws URISyntaxException {
        this.uriBuilder = new URIBuilder(settings.getString("sonar.core.serverBaseURL"));
        this.httpclient = HttpClients.createDefault();
        this.httpGet = new HttpGet();
        this.responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                final int status = response.getStatusLine()
                    .getStatusCode();
                if ((status >= 200) && (status < 300)) {
                    final HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
    }

    public QualityGateStatus retrieveFor(final String key) {
        QualityGateStatus status = QualityGateStatus.NONE;
        final String uriQuery = new StringBuilder().append("resource=")
            .append(key)
            .append("&metrics=quality_gate_details&format=json")
            .toString();
        try {
            this.httpGet.setURI(new URI(this.uriBuilder.getScheme(), null, this.uriBuilder.getHost(), this.uriBuilder.getPort(), StringUtils.removeEnd(this.uriBuilder.getPath(), URI_SEPARATOR)
                + "/api/resources/index/", uriQuery, null));
            final String responseBody = this.httpclient.execute(this.httpGet, this.responseHandler);
            final JSONArray resources = new JSONArray(responseBody);
            if (!resources.isNull(0)) {
                final JSONObject resource = resources.getJSONObject(0);
                final JSONArray measures = resource.getJSONArray("msr");
                final JSONObject measure = measures.getJSONObject(0);
                final String dataString = measure.getString("data");
                final JSONObject data = new JSONObject(dataString);
                final String statusString = (String) data.get("level");
                status = QualityGateStatus.valueOf(statusString);
            }
        } catch (URISyntaxException | IOException | JSONException e) {
            status = QualityGateStatus.SERVER_ERROR;
        }
        return status;
    }

}
