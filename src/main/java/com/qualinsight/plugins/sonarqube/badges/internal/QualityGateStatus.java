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
package com.qualinsight.plugins.sonarqube.badges.internal;

import java.awt.Color;

/**
 * Possible statuses when trying to retrieve the quality gate status for a SonarQube project or view. Each status holds information about how it has to be displayed in a SVG image.
 *
 * @author Michel Pawlak
 */
public enum QualityGateStatus {
    /**
     * No gate is active for the project or view.
     */
    NONE("not set",
        new Color(150, 150, 150, 255),
        50),
    /**
     * A server error occurred while retrieving the quality gate status.
     */
    SERVER_ERROR("server error",
        new Color(224, 93, 68, 255),
        76),
    /**
     * The project / view passes the quality gate.
     */
    OK("passing",
        new Color(86, 209, 41, 255),
        50),
    /**
     * The project / view does not pass the quality gate due to gate warnings.
     */
    WARN("warning",
        new Color(255, 165, 0, 255),
        52),
    /**
     * The project / view does not pass the quality gate due to gate errors.
     */
    ERROR("failing",
        new Color(224, 93, 68, 255),
        44),
    /**
     * The project / view could not be found on the SonarQube's server.
     */
    NOT_FOUND("not found",
        new Color(224, 93, 68, 255),
        65),
    /**
     * Access to the project / view is restricted (see issue #15)
     */
    FORBIDDEN("forbidden",
        new Color(224, 93, 68, 255),
        65);

    private final String displayText;

    private final Color displayBackgroundColor;

    private int displayWidth;

    private QualityGateStatus(final String displayText, final Color displayBackgroundColor, final int displayWidth) {
        this.displayText = displayText;
        this.displayBackgroundColor = displayBackgroundColor;
        this.displayWidth = displayWidth;
    }

    String displayText() {
        return this.displayText;
    }

    Color displayBackgroundColor() {
        return this.displayBackgroundColor;
    }

    int displayWidth() {
        return this.displayWidth;
    }

}
