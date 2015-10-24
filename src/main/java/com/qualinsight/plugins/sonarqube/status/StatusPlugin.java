/*
 * qualinsight-plugins-sonarqube-status
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
package com.qualinsight.plugins.sonarqube.status;

import java.util.List;
import com.google.common.collect.ImmutableList;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import com.qualinsight.plugins.sonarqube.status.extension.StatusWebService;
import com.qualinsight.plugins.sonarqube.status.internal.QualityGateStatusRetriever;
import com.qualinsight.plugins.sonarqube.status.internal.SVGImageGenerator;

/**
 * Core StatusPlugin class. It declares all extensions used by the plugin.
 *
 * @author Michel Pawlak
 */
@Properties({
    @Property(key = "sonar.status.server.scheme", name = "SonarQube connection scheme", defaultValue = "http"),
    @Property(key = "sonar.status.server.host", name = "SonarQube host", defaultValue = "localhost"),
    @Property(key = "sonar.status.server.port", name = "SonarQube port", defaultValue = "9000"),
    @Property(key = "sonar.status.server.contextRoot", name = "SonarQube context root", defaultValue = "")
})
public final class StatusPlugin extends SonarPlugin {

    @SuppressWarnings("rawtypes")
    @Override
    public List getExtensions() {
        return ImmutableList.builder()
            .add(QualityGateStatusRetriever.class)
            .add(SVGImageGenerator.class)
            .add(StatusWebService.class)
            .build();
    }
}
