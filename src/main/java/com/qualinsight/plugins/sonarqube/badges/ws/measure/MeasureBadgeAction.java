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

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.WebService.NewAction;
import org.sonar.api.server.ws.WebService.NewController;

@ServerSide
public class MeasureBadgeAction {

    private MeasureBadgeRequestHandler measureBadgeRequestHandler;

    /**
     * {@link MeasureBadgeAction} IoC constructor
     */
    public MeasureBadgeAction(final MeasureBadgeRequestHandler measureBadgeRequestHandler) {
        this.measureBadgeRequestHandler = measureBadgeRequestHandler;
    }

    public void createOn(final NewController controller) {
        final NewAction action = controller.createAction("measure")
            .setDescription("Retrieves a measure for a project as a SVG image.")
            .setHandler(this.measureBadgeRequestHandler);
        action.createParam("key")
            .setDescription("Key of the project")
            .setExampleValue("org.codehaus.sonar:sonar")
            .setRequired(true);
        action.createParam("metric")
            .setDescription("measured metric to be retrieved")
            .setExampleValue("coverage")
            .setRequired(true);
    }
}
