/*
 * qualinsight-plugins-sonarqube-badges
 * Copyright (c) 2015-2016, QualInsight
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
package com.qualinsight.plugins.sonarqube.badges.ws.gate;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonarqube.ws.WsQualityGates.ProjectStatusWsResponse;
import org.sonarqube.ws.client.HttpException;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.qualitygate.ProjectStatusWsRequest;

/**
 * {@link RequestHandler} implementation that handles Quality Gate badges requests.
 * 
 * @author Michel Pawlak
 */
@ServerSide
public class QualityGateBadgeRequestHandler implements RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityGateBadgeRequestHandler.class);

    private QualityGateBadgeGenerator qualityGateBadgeGenerator;

    /**
     * {@link QualityGateBadgeRequestHandler} IoC constructor
     *
     * @param qualityGateBadgeGenerator helper extension that generate quality gate badges
     */
    public QualityGateBadgeRequestHandler(final QualityGateBadgeGenerator qualityGateBadgeGenerator) {
        this.qualityGateBadgeGenerator = qualityGateBadgeGenerator;
    }

    @Override
    public void handle(final Request request, final Response response) throws Exception {
        final String key = request.mandatoryParam("key");
        final WsClient wsClient = WsClientFactories.getLocal()
            .newClient(request.getLocalConnector());
        LOGGER.debug("Retrieving quality gate status for key '{}'.", key);
        QualityGateBadge status = QualityGateBadge.NOT_FOUND;
        try {
            final ProjectStatusWsRequest wsRequest = new ProjectStatusWsRequest();
            wsRequest.setProjectKey(key);
            final ProjectStatusWsResponse wsResponse = wsClient.qualityGates()
                .projectStatus(wsRequest);
            status = QualityGateBadge.valueOf(wsResponse.getProjectStatus()
                .getStatus()
                .toString());
        } catch (final HttpException e) {
            LOGGER.debug("No project found with key '{}': {}", key, e);
        }
        // we prepare the response OutputStream
        final OutputStream responseOutputStream = response.stream()
            .setMediaType("image/svg+xml")
            .output();
        LOGGER.debug("Retrieving SVG image for for quality gate status '{}'.", status);
        final InputStream svgImageInputStream = this.qualityGateBadgeGenerator.svgImageInputStreamFor(status);
        LOGGER.debug("Writing SVG image to response OutputStream.");
        IOUtils.copy(svgImageInputStream, responseOutputStream);
        responseOutputStream.close();
        // don't close svgImageInputStream, we want it to be reusable
    }
}
