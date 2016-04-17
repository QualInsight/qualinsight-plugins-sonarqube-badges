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
package com.qualinsight.plugins.sonarqube.badges;

import java.util.List;
import com.google.common.collect.ImmutableList;
import org.sonar.api.SonarPlugin;
import com.qualinsight.plugins.sonarqube.badges.ws.BadgesWebService;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeAction;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeGenerator;
import com.qualinsight.plugins.sonarqube.badges.ws.gate.QualityGateBadgeRequestHandler;
import com.qualinsight.plugins.sonarqube.badges.ws.util.SVGImageFontReplacer;
import com.qualinsight.plugins.sonarqube.badges.ws.util.SVGImageGenerator;

/**
 * Core BadgesPlugin class. It declares all extensions used by the plugin.
 *
 * @author Michel Pawlak
 */
public final class BadgesPlugin extends SonarPlugin {

    @SuppressWarnings("rawtypes")
    @Override
    public List getExtensions() {
        return ImmutableList.builder()
            .add(SVGImageFontReplacer.class)
            .add(SVGImageGenerator.class)
            .add(QualityGateBadgeRequestHandler.class)
            .add(QualityGateBadgeGenerator.class)
            .add(QualityGateBadgeAction.class)
            .add(BadgesWebService.class)
            .build();
    }
}
