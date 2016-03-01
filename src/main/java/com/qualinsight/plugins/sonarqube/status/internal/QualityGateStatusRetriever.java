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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;

public final class QualityGateStatusRetriever implements ServerExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityGateStatusRetriever.class);

    private static final String URI_SEPARATOR = "/";

    private static final String SERVER_BASE_URL_KEY = "sonar.core.serverBaseURL";

    private final HttpClient httpclient;

    private final ResponseHandler<String> responseHandler;

    private final HttpGet httpGet;

    private final Settings settings;

    /**
     * {@link QualityGateStatusRetriever} IoC constructor.
     *
     * @param settings SonarQube {@link Settings}, required to retrieve the "sonar.core.serverBaseURL" property.
     * @throws URISyntaxException if "sonar.core.serverBaseURL" in SonarQube settings is a malformed URI.
     */
    public QualityGateStatusRetriever(final Settings settings) throws URISyntaxException {
        this.settings = settings;
        this.httpclient = HttpClients.createDefault();
        this.httpGet = new HttpGet();
        this.responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response) throws IOException {
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
        if (this.settings.getString(SERVER_BASE_URL_KEY)
            .equals(this.settings.getDefaultValue(SERVER_BASE_URL_KEY))) {
            LOGGER.warn("'{}' property has default value which may lead to unexpected behavior. Make sure that you've correctly configured this property in SonarQube's general settings.",
                SERVER_BASE_URL_KEY);
        }
        LOGGER.info("QualityGateStatusRetriever is now ready.");
    }

    /**
     * Enables the retrieval of the quality gate status for project or view with specified key.
     *
     * @param key key of the project or view for which the quality gate status needs to be retrieved.
     * @return current {@link QualityGateStatus} for the specified key
     */
    public QualityGateStatus retrieveFor(final String key) {
        QualityGateStatus status;
        try {
            status = statusFromReponseBody(responseBodyForKey(key));
        } catch (URISyntaxException | IOException | JSONException e) {
            status = QualityGateStatus.SERVER_ERROR;
            // We do not want to spam server logs with malformed requests, therefore we only log in debug mode
            LOGGER.debug("An error occurred while retrieving quality gate status for key '{}': {}", key, e);
        }
        return status;
    }

    private String responseBodyForKey(final String key) throws IOException, URISyntaxException {
        final URIBuilder uriBuilder = new URIBuilder(this.settings.getString(SERVER_BASE_URL_KEY));
        final String uriQuery = new StringBuilder().append("resource=")
            .append(key)
            .append("&metrics=quality_gate_details&format=json")
            .toString();
        this.httpGet.setURI(new URI(uriBuilder.getScheme(), null, uriBuilder.getHost(), uriBuilder.getPort(), StringUtils.removeEnd(uriBuilder.getPath(), URI_SEPARATOR) + "/api/resources/index/",
            uriQuery, null));
        LOGGER.debug("Http GET request line: {}", this.httpGet.getRequestLine());
        final String responseBody = this.httpclient.execute(this.httpGet, this.responseHandler);
        LOGGER.debug("Http GET response body: {}", responseBody);
        return responseBody;
    }

    private static QualityGateStatus statusFromReponseBody(final String responseBody) {
        final JSONArray resources = new JSONArray(responseBody);
        if (!resources.isNull(0)) {
            final JSONObject resource = resources.getJSONObject(0);
            final JSONArray measures = resource.getJSONArray("msr");
            final JSONObject measure = measures.getJSONObject(0);
            final String dataString = measure.getString("data");
            final JSONObject data = new JSONObject(dataString);
            final String statusString = (String) data.get("level");
            return QualityGateStatus.valueOf(statusString);
        }
        return QualityGateStatus.NONE;
    }

}
