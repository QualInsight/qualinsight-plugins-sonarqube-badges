/*
 * qualinsight-plugins-sonarqube-badges
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
package com.qualinsight.plugins.sonarqube.badges.internal;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import com.qualinsight.plugins.sonarqube.badges.internal.exception.ForbiddenException;
import com.qualinsight.plugins.sonarqube.badges.internal.exception.ProjectNotFoundException;

public class QualityGateServiceResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(final HttpResponse response) throws IOException {
        final int status = response.getStatusLine()
            .getStatusCode();
        if ((status >= 200) && (status < 300)) {
            final HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else if (status == 401) {
            throw new ForbiddenException();
        } else if (status == 404) {
            throw new ProjectNotFoundException();
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}
