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
package com.qualinsight.plugins.sonarqube.badges.ws;

import org.sonar.api.server.ws.WebService;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeAction;

/**
 * WebService extension that provides the plugins' webservice controller and action for generating SVG quality gate status images.
 *
 * @author Michel Pawlak
 */
public final class BadgesWebService implements WebService {

    private QualityGateBadgeAction qualityGateBadgeAction;

    /**
     * {@link BadgesWebService} IoC constructor
     *
     * @param qualityGateBadgeAction action to retrieve Quality Gate status badges
     */
    public BadgesWebService(final QualityGateBadgeAction qualityGateBadgeAction) {
        this.qualityGateBadgeAction = qualityGateBadgeAction;
    }

    @Override
    public void define(final Context context) {
        final NewController controller = context.createController("api/badges");
        controller.setDescription("SVG Badges generation web service");
        this.qualityGateBadgeAction.createOn(controller);
        controller.done();
    }
}
