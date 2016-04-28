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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import com.google.common.collect.ImmutableList;
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
import org.sonarqube.ws.client.HttpException;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measure.ComponentWsRequest;
import com.qualinsight.plugins.sonarqube.badges.BadgesPuginProperties;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeRequestHandler;

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
     */
    public MeasureBadgeRequestHandler(final MeasureBadgeGenerator measureBadgeGenerator, final Settings settings) {
        this.measureBadgeGenerator = measureBadgeGenerator;
        this.settings = settings;
    }

    @Override
    public void handle(final Request request, final Response response) throws Exception {
        if (this.settings.getBoolean(BadgesPuginProperties.MEASURE_BADGES_ACTIVATION_KEY)) {
            final String key = request.mandatoryParam("key");
            final String metric = request.mandatoryParam("metric");
            final WsClient wsClient = WsClientFactories.getLocal()
                .newClient(request.localConnector());
            LOGGER.debug("Retrieving measure for key '{}' and metric {}.", key, metric);
            MeasureHolder measureHolder;
            try {
                final ComponentWsRequest wsRequest = new ComponentWsRequest();
                wsRequest.setComponentKey(key);
                wsRequest.setMetricKeys(ImmutableList.of(metric));
                final ComponentWsResponse wsResponse = wsClient.measures()
                    .component(wsRequest);
                final List<Measure> measures = wsResponse.getComponent()
                    .getMeasuresList();
                if (measures.isEmpty()) {
                    measureHolder = new MeasureHolder(metric);
                    LOGGER.debug("No measure found ! Using '{}' value", measureHolder.value());
                } else {
                    measureHolder = new MeasureHolder(measures.get(0));
                }
            } catch (final HttpException e) {
                LOGGER.debug("No project found with key '{}': {}", key, e);
                measureHolder = new MeasureHolder(metric);
            }
            // we prepare the response OutputStream
            final OutputStream responseOutputStream = response.stream()
                .setMediaType("image/svg+xml")
                .output();
            LOGGER.debug("Retrieving SVG image for metric holder '{}'.", measureHolder);
            final InputStream svgImageInputStream = this.measureBadgeGenerator.svgImageInputStreamFor(measureHolder);
            LOGGER.debug("Writing SVG image to response OutputStream.");
            IOUtils.copy(svgImageInputStream, responseOutputStream);
            responseOutputStream.close();
            // don't close svgImageInputStream, we want it to be reusable
        } else {
            response.noContent();
        }
    }
}
