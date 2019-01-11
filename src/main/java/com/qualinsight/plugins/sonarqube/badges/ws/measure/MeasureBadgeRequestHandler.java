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
package com.qualinsight.plugins.sonarqube.badges.ws.measure;

import com.google.common.collect.ImmutableList;
import com.qualinsight.plugins.sonarqube.badges.BadgesPluginProperties;
import com.qualinsight.plugins.sonarqube.badges.ws.GitlabProjectPathKeyResolver;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageTemplate;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeRequestHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonarqube.ws.WsMeasures.ComponentWsResponse;
import org.sonarqube.ws.WsMeasures.Measure;
import org.sonarqube.ws.WsQualityGates.ProjectStatusWsResponse;
import org.sonarqube.ws.WsQualityGates.ProjectStatusWsResponse.Condition;
import org.sonarqube.ws.WsQualityGates.ProjectStatusWsResponse.Status;
import org.sonarqube.ws.client.HttpException;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measure.ComponentWsRequest;
import org.sonarqube.ws.client.qualitygate.ProjectStatusWsRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * {@link RequestHandler} implementation that handles measure badges requests.
 *
 * @author Michel Pawlak
 */
@ServerSide
public class MeasureBadgeRequestHandler implements RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasureBadgeRequestHandler.class);

    private MeasureBadgeGenerator measureBadgeGenerator;

    private Settings settings;

    /**
     * {@link QualityGateBadgeRequestHandler} IoC constructor
     *
     * @param measureBadgeGenerator helper extension that generate measure badges
     * @param settings              SonarQube properties
     */
    public MeasureBadgeRequestHandler(final MeasureBadgeGenerator measureBadgeGenerator, final Settings settings) {
        this.measureBadgeGenerator = measureBadgeGenerator;
        this.settings = settings;
    }

    @Override
    public void handle(final Request request, final Response response) throws Exception {
        if (this.settings.getBoolean(BadgesPluginProperties.MEASURE_BADGES_ACTIVATION_KEY)) {
            String key = request.mandatoryParam("key");
            final String metric = request.mandatoryParam("metric");
            final SVGImageTemplate template = request.mandatoryParamAsEnum("template", SVGImageTemplate.class);
            final boolean blinkingValueBackgroundColor = request.mandatoryParamAsBoolean("blinking");
            boolean gitlabBadgeSplitProjectName = request.mandatoryParamAsBoolean("gitlab");

            if (gitlabBadgeSplitProjectName) {
                key = GitlabProjectPathKeyResolver.getGitlabProjectNameByPath(key);
            }

            final WsClient wsClient = WsClientFactories.getLocal()
                    .newClient(request.localConnector());
            LOGGER.debug("Retrieving measure for key '{}' and metric {}.", key, metric);
            MeasureHolder measureHolder;
            try {
                measureHolder = retrieveMeasureHolder(wsClient, key, metric);
                measureHolder = applyQualityGateTreshold(wsClient, key, metric, measureHolder);
            } catch (final HttpException e) {
                LOGGER.debug("No project found with key '{}': {}", key, e);
                measureHolder = new MeasureHolder(metric);
            }
            // we prepare the response OutputStream
            final OutputStream responseOutputStream = response.stream()
                    .setMediaType("image/svg+xml")
                    .output();
            LOGGER.debug("Retrieving SVG image for metric holder '{}'.", measureHolder);
            final InputStream svgImageInputStream = this.measureBadgeGenerator.svgImageInputStreamFor(measureHolder, template, blinkingValueBackgroundColor);
            LOGGER.debug("Writing SVG image to response OutputStream.");
            IOUtils.copy(svgImageInputStream, responseOutputStream);
            responseOutputStream.close();
            // don't close svgImageInputStream, we want it to be reusable
        } else {
            LOGGER.warn("Received a measure badge request, but webservice is turned off.");
            response.noContent();
        }
    }

    private MeasureHolder retrieveMeasureHolder(final WsClient wsClient, final String key, final String metric) {
        MeasureHolder measureHolder;
        final ComponentWsRequest componentWsRequest = new ComponentWsRequest();
        componentWsRequest.setComponentKey(key);
        componentWsRequest.setMetricKeys(ImmutableList.of(metric));
        final ComponentWsResponse componentWsResponse = wsClient.measures()
                .component(componentWsRequest);
        final List<Measure> measures = componentWsResponse.getComponent()
                .getMeasuresList();
        if (measures.isEmpty()) {
            measureHolder = new MeasureHolder(metric);
        } else {
            measureHolder = new MeasureHolder(measures.get(0));
        }
        return measureHolder;
    }

    private MeasureHolder applyQualityGateTreshold(final WsClient wsClient, final String key, final String metric, final MeasureHolder measureHolder) {
        final ProjectStatusWsRequest projectStatusWsRequest = new ProjectStatusWsRequest();
        projectStatusWsRequest.setProjectKey(key);
        final ProjectStatusWsResponse projectStatusWsResponse = wsClient.qualityGates()
                .projectStatus(projectStatusWsRequest);
        final List<Condition> conditions = projectStatusWsResponse.getProjectStatus()
                .getConditionsList();
        for (final Condition condition : conditions) {
            if (metric.equals(condition.getMetricKey())) {
                final Status status = condition.getStatus();
                switch (status) {
                    case ERROR:
                        measureHolder.setBackgroundColor(SVGImageColor.RED);
                        break;
                    case OK:
                        measureHolder.setBackgroundColor(SVGImageColor.GREEN);
                        break;
                    case WARN:
                        measureHolder.setBackgroundColor(SVGImageColor.ORANGE);
                        break;
                    default:
                        measureHolder.setBackgroundColor(SVGImageColor.GRAY);
                        break;

                }
            }
        }
        return measureHolder;
    }
}
