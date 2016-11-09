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
package com.qualinsight.plugins.sonarqube.badges;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

/**
 * Declares all properties the plugin uses.
 * 
 * @author Michel Pawlak
 */
public final class BadgesPluginProperties {

    private static final String CATEGORY = "SVG Badges";

    /**
     * Key for quality gate badges activation.
     */
    public static final String GATE_BADGES_ACTIVATION_KEY = "qualinsight.badges.activation.gate";

    /**
     * Key for measure badges activation.
     */
    public static final String MEASURE_BADGES_ACTIVATION_KEY = "qualinsight.badges.activation.measure";
    
    /**
     * Key for compute engine activity badges activation.
     */
    public static final String CE_ACTIVITY_ACTIVATION_KEY = "qualinsight.badges.activation.ce_activity";

    private BadgesPluginProperties() {
        // Helper class
    }

    /**
     * Plugin properties retrieval method.
     *
     * @return list of properties declared by the plugin.
     */
    public static List<PropertyDefinition> properties() {
        final List<PropertyDefinition> properties = new ArrayList<>();
        properties.add(PropertyDefinition.builder(GATE_BADGES_ACTIVATION_KEY)
            .category(CATEGORY)
            .name("Enable quality gate badges")
            .description("Setting this property to true enables the quality gate badge generation webservice.")
            .type(PropertyType.BOOLEAN)
            .defaultValue("true")
            .build());
        properties.add(PropertyDefinition.builder(MEASURE_BADGES_ACTIVATION_KEY)
            .category(CATEGORY)
            .name("Enable measures badges")
            .description(
                "Setting this property to true enables the measures badge generation webservice. Note that this type of badges can be resource consuming and scalability hasn't been tested yet.")
            .type(PropertyType.BOOLEAN)
            .defaultValue("false")
            .build());
        properties.add(PropertyDefinition.builder(CE_ACTIVITY_ACTIVATION_KEY)
            .category(CATEGORY)
            .name("Enable compute engine activity badges")
            .description("Setting this property to true enables the compute engine activity badge generation webservice.")
            .type(PropertyType.BOOLEAN)
            .defaultValue("true")
            .build());
        return properties;
    }
}