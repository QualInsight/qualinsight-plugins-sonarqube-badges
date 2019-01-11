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

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.server.ws.WebService.Action;
import org.sonar.api.server.ws.WebService.Controller;
import org.sonar.api.server.ws.WebService.NewAction;
import org.sonar.api.server.ws.WebService.NewController;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageTemplate;

/**
 * Creates {@link Action} for quality gate badge {@link WebService} {@link Controller}.
 *
 * @author Michel Pawlak
 */
@ServerSide
public class QualityGateBadgeAction {

    private static final String RESPONSE_EXAMPLE_FILE = "/com/qualinsight/plugins/sonarqube/badges/ws/gate/example.svg";

    private static final String SINCE_VERSION = "1.2.0";

    private QualityGateBadgeRequestHandler qualityGateBadgeRequestHandler;

    /**
     * {@link QualityGateBadgeAction} IoC constructor
     *
     * @param qualityGateBadgeRequestHandler request handler to be bound to the action
     */
    public QualityGateBadgeAction(final QualityGateBadgeRequestHandler qualityGateBadgeRequestHandler) {
        this.qualityGateBadgeRequestHandler = qualityGateBadgeRequestHandler;
    }

    /**
     * Adds the action to a provided controller
     *
     * @param controller the action needs to be added to
     */
    public void createOn(final NewController controller) {
        final NewAction action = controller.createAction("gate")
            .setDescription("Retrieves the quality gate status of a project as a SVG image.")
            .setHandler(this.qualityGateBadgeRequestHandler)
            .setSince(SINCE_VERSION)
            .setResponseExample(getClass().getResource(RESPONSE_EXAMPLE_FILE));
        action.createParam("key")
            .setDescription("Key of the project")
            .setExampleValue("org.codehaus.sonar:sonar")
            .setRequired(true);
        action.createParam("template")
            .setDescription("Template to be used for badge generation")
            .setPossibleValues((Object[]) SVGImageTemplate.values())
            .setDefaultValue(SVGImageTemplate.ROUNDED)
            .setRequired(false);
        action.createParam("blinking")
            .setDescription("Set to 'true' if you want quality gate badges to be blinking if the quality gate is in 'ERROR'.")
            .setBooleanPossibleValues()
            .setDefaultValue(Boolean.FALSE)
            .setRequired(false);
        action.createParam("gitlab")
                .setDescription("Set to 'true' if you want to using it at gitlab badges.")
                .setBooleanPossibleValues()
                .setDefaultValue(Boolean.FALSE)
                .setRequired(false);
    }
}
