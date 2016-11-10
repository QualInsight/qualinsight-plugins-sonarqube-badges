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
package com.qualinsight.plugins.sonarqube.badges.ws;

import org.sonar.api.server.ws.WebService;
import com.qualinsight.plugins.sonarqube.badges.ws.ce.CeActivityBadgeAction;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeAction;
import com.qualinsight.plugins.sonarqube.badges.ws.measure.MeasureBadgeAction;

/**
 * WebService extension that provides the plugins' webservice controller and action for generating SVG quality gate status images.
 *
 * @author Michel Pawlak
 */
public final class BadgesWebService implements WebService {

    private QualityGateBadgeAction qualityGateBadgeAction;

    private MeasureBadgeAction measureBadgeAction;

    private CeActivityBadgeAction ceActivityBadgeAction;

    /**
     * {@link BadgesWebService} IoC constructor
     *
     * @param qualityGateBadgeAction action to retrieve Quality Gate status badges
     * @param measureBadgeAction action to retrieve measure badges
     * @param ceActivityBadgeAction action to retrieve Compute Engine Activity badges
     */
    public BadgesWebService(final QualityGateBadgeAction qualityGateBadgeAction, final MeasureBadgeAction measureBadgeAction, final CeActivityBadgeAction ceActivityBadgeAction) {
        this.qualityGateBadgeAction = qualityGateBadgeAction;
        this.measureBadgeAction = measureBadgeAction;
        this.ceActivityBadgeAction = ceActivityBadgeAction;
    }

    @Override
    public void define(final Context context) {
        final NewController controller = context.createController("api/badges");
        controller.setDescription("SVG Badges generation web service");
        this.qualityGateBadgeAction.createOn(controller);
        this.measureBadgeAction.createOn(controller);
        this.ceActivityBadgeAction.createOn(controller);
        controller.done();
    }
}
