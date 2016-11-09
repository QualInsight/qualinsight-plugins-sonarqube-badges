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
import org.sonarqube.ws.WsCe.Task;
import org.sonarqube.ws.client.HttpException;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.ce.ActivityWsRequest;

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
   * @param ceActivityBadgeGenerator
   *          helper extension that generate compute engine activity badges
   * @param settings
   *          SonarQube properties
   */
  public CeActivityBadgeRequestHandler(final CeActivityBadgeGenerator ceActivityBadgeGenerator, final Settings settings) {
    this.ceActivityBadgeGenerator = ceActivityBadgeGenerator;
    this.settings = settings;
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    if (this.settings.getBoolean(BadgesPluginProperties.CE_ACTIVITY_ACTIVATION_KEY)) {
      final String key = request.mandatoryParam("key");
      final SVGImageTemplate template = request.mandatoryParamAsEnum("template", SVGImageTemplate.class);
      final boolean blinkingValueBackgroundColor = request.mandatoryParamAsBoolean("blinking");

      final WsClient wsClient = WsClientFactories.getLocal().newClient(request.localConnector());

      LOGGER.debug("Retrieving compute engine activity status for key '{}'.", key);
      CeActivityBadge status = CeActivityBadge.NOT_FOUND;

      try {
        final ActivityWsRequest wsRequest = new ActivityWsRequest();
        wsRequest.setQuery(key);

        // The task are ordered by date. 0 is the most recent. 
        Task task = wsClient.ce().activity(wsRequest).getTasks(0);

        LOGGER.debug("CE ACTIVITY TASK INFORMATION");
        LOGGER.debug("CE ACTIVITY TASK - id: " + task.getId());
        LOGGER.debug("CE ACTIVITY TASK - type: " + task.getType());
        LOGGER.debug("CE ACTIVITY TASK - componentId: " + task.getComponentId());
        LOGGER.debug("CE ACTIVITY TASK - componentKey: " + task.getComponentKey());
        LOGGER.debug("CE ACTIVITY TASK - componentName: " + task.getComponentName());
        LOGGER.debug("CE ACTIVITY TASK - componentQualifier: " + task.getComponentQualifier());
        LOGGER.debug("CE ACTIVITY TASK - analysisId: " + task.getAnalysisId());
        LOGGER.debug("CE ACTIVITY TASK - status: " + task.getStatus());
        LOGGER.debug("CE ACTIVITY TASK - submittedAt: " + task.getSubmittedAt());
        LOGGER.debug("CE ACTIVITY TASK - submitterLogin: " + task.getSubmitterLogin());
        LOGGER.debug("CE ACTIVITY TASK - startedAt: " + task.getStartedAt());
        LOGGER.debug("CE ACTIVITY TASK - executedAt: " + task.getExecutedAt());
        LOGGER.debug("CE ACTIVITY TASK - executionTimeMs: " + task.getExecutionTimeMs());

        // status of the activity
        status = CeActivityBadge.valueOf(task.getStatus().toString());

      } catch (final HttpException e) {
        LOGGER.debug("No project found with key '{}': {}", key, e);
      }

      // we prepare the response OutputStream
      final OutputStream responseOutputStream = response.stream().setMediaType("image/svg+xml").output();
      LOGGER.debug("Retrieving SVG image for for compute engine activity status '{}'.", status);
      final InputStream svgImageInputStream = this.ceActivityBadgeGenerator.svgImageInputStreamFor(status, template,
          blinkingValueBackgroundColor);
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
