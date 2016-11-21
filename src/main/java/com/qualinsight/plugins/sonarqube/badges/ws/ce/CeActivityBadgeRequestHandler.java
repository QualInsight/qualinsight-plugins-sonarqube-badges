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
package com.qualinsight.plugins.sonarqube.badges.ws.ce;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonarqube.ws.WsCe.ActivityResponse;
import org.sonarqube.ws.WsCe.Task;
import org.sonarqube.ws.client.HttpException;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.ce.ActivityWsRequest;
import org.sonarqube.ws.client.ce.CeService;
import com.qualinsight.plugins.sonarqube.badges.BadgesPluginProperties;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageTemplate;

/**
 * {@link RequestHandler} implementation that handles Compute Engine Activity badges requests.
 *
 */
@ServerSide
public class CeActivityBadgeRequestHandler implements RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CeActivityBadgeRequestHandler.class);

    private CeActivityBadgeGenerator ceActivityBadgeGenerator;

    private Settings settings;

    /**
     * {@link CeActivityBadgeRequestHandler} IoC constructor
     *
     * @param ceActivityBadgeGenerator helper extension that generate compute engine activity badges
     * @param settings SonarQube properties
     */
    public CeActivityBadgeRequestHandler(final CeActivityBadgeGenerator ceActivityBadgeGenerator, final Settings settings) {
        this.ceActivityBadgeGenerator = ceActivityBadgeGenerator;
        this.settings = settings;
    }

    @Override
    public void handle(final Request request, final Response response) throws Exception {
        if (this.settings.getBoolean(BadgesPluginProperties.CE_ACTIVITY_BADGES_ACTIVATION_KEY)) {
            final String key = request.mandatoryParam("key");
            final SVGImageTemplate template = request.mandatoryParamAsEnum("template", SVGImageTemplate.class);
            final boolean blinkingValueBackgroundColor = request.mandatoryParamAsBoolean("blinking");

            final WsClient wsClient = WsClientFactories.getLocal()
                .newClient(request.localConnector());

            LOGGER.debug("Retrieving compute engine activity status for key '{}'.", key);
            CeActivityBadge status = CeActivityBadge.NOT_FOUND;

            try {
                final ActivityWsRequest wsRequest = new ActivityWsRequest();
                wsRequest.setQuery(key);

                final CeService ceService = wsClient.ce();
                final ActivityResponse activityResponse = ceService.activity(wsRequest);
                if (activityResponse.getTasksCount() >= 1) {
                    // The task are ordered by date. 0 is the most recent.
                    final Task task = activityResponse.getTasks(0);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("CeActivity Task Information");
                        LOGGER.debug("CeActivity Task - id: " + task.getId());
                        LOGGER.debug("CeActivity Task - type: " + task.getType());
                        LOGGER.debug("CeActivity Task - componentId: " + task.getComponentId());
                        LOGGER.debug("CeActivity Task - componentKey: " + task.getComponentKey());
                        LOGGER.debug("CeActivity Task - componentName: " + task.getComponentName());
                        LOGGER.debug("CeActivity Task - componentQualifier: " + task.getComponentQualifier());
                        LOGGER.debug("CeActivity Task - analysisId: " + task.getAnalysisId());
                        LOGGER.debug("CeActivity Task - status: " + task.getStatus());
                        LOGGER.debug("CeActivity Task - submittedAt: " + task.getSubmittedAt());
                        LOGGER.debug("CeActivity Task - submitterLogin: " + task.getSubmitterLogin());
                        LOGGER.debug("CeActivity Task - startedAt: " + task.getStartedAt());
                        LOGGER.debug("CeActivity Task - executedAt: " + task.getExecutedAt());
                        LOGGER.debug("CeActivity Task - executionTimeMs: " + task.getExecutionTimeMs());
                    }
                    // status of the activity
                    status = CeActivityBadge.valueOf(task.getStatus()
                        .toString());
                }
            } catch (final HttpException e) {
                LOGGER.debug("No project found with key '{}': {}", key, e);
            }

            // we prepare the response OutputStream
            final OutputStream responseOutputStream = response.stream()
                .setMediaType("image/svg+xml")
                .output();
            LOGGER.debug("Retrieving SVG image for for compute engine activity status '{}'.", status);
            final InputStream svgImageInputStream = this.ceActivityBadgeGenerator.svgImageInputStreamFor(status, template, blinkingValueBackgroundColor);
            LOGGER.debug("Writing SVG image to response OutputStream.");
            IOUtils.copy(svgImageInputStream, responseOutputStream);
            responseOutputStream.close();
            // don't close svgImageInputStream, we want it to be reusable

        } else {
            LOGGER.warn("Received a compute engine activity badge request, but webservice is turned off.");
            response.noContent();
        }

    }

}
