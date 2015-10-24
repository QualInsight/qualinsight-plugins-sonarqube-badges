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
package com.qualinsight.plugins.sonarqube.status.internal;

import java.awt.Color;

public enum QualityGateStatus {
    NONE("no gate",
        new Color(150, 150, 150, 255),
        50),
    SERVER_ERROR("server error",
        new Color(224, 93, 68, 255),
        76),
    OK("passing",
        new Color(86, 209, 41, 255),
        50),
    WARN("warning",
        new Color(255, 165, 0, 255),
        52),
    ERROR("failing",
        new Color(224, 93, 68, 255),
        44);

    private final String displayText;

    private final Color displayBackgroundColor;

    private int displayWidth;

    private QualityGateStatus(final String displayText, final Color displayBackgroundColor, final int displayWidth) {
        this.displayText = displayText;
        this.displayBackgroundColor = displayBackgroundColor;
        this.displayWidth = displayWidth;
    }

    public String displayText() {
        return this.displayText;
    }

    public Color displayBackgroundColor() {
        return this.displayBackgroundColor;
    }

    public int displayWidth() {
        return this.displayWidth;
    }

}
