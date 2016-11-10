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

import static com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor.GRAY;
import static com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor.GREEN;
import static com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor.ORANGE;
import static com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor.RED;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor;

/**
 * Possible badges for a SonarQube project or view. Each badge holds information about how it has to be displayed as a SVG image.
 *
 */
public enum CeActivityBadge {
    /**
     * The compute engine activity was canceled.
     */
    CANCELED("canceled",
        GRAY),
    /**
     * The compute engine activity failed.
     */
    FAILED("failed",
        RED),
    /**
     * The compute engine activity is in progress.
     */
    IN_PROGRESS("in progress",
        ORANGE),
    /**
     * The compute engine activity is queued.
     */
    PENDING("pending",
        GRAY),
    /**
     * The compute engine activity is success.
     */
    SUCCESS("success",
        GREEN),
    /**
     * The project / view could not be found on the SonarQube's server.
     */
    NOT_FOUND("not found",
        RED);

    private final String displayText;

    private final SVGImageColor displayBackgroundColor;

    private CeActivityBadge(final String displayText, final SVGImageColor displayBackgroundColor) {
        this.displayText = displayText;
        this.displayBackgroundColor = displayBackgroundColor;
    }

    /**
     * Text to be displayed for the badge type
     *
     * @return text to be displayed
     */
    public String displayText() {
        return this.displayText;
    }

    /**
     * Background color to be displayed for the badge type
     *
     * @return background color to be displayed
     */
    public SVGImageColor displayBackgroundColor() {
        return this.displayBackgroundColor;
    }
}
