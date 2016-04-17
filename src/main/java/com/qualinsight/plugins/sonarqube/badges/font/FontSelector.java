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
package com.qualinsight.plugins.sonarqube.badges.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.List;
import com.google.common.collect.ImmutableList;

/**
 * Helper class that selects the preferred Font from a specfied list.
 *
 * @author Michel Pawlak
 */
public final class FontSelector {

    private static final String DUMMY_FONT_NAME = "DUMMY_FONT_NAME";

    private static final int FONT_SIZE = 11;

    private static final int FONT_STYLE = Font.PLAIN;

    private static final List<String> preferredFontNames = ImmutableList.of("DejaVu Sans", "Verdana", "Geneva", "Tahoma", "Arial", "sans-serif");

    private FontSelector() {
        // Utility class
    }

    /**
     * Scans available Fonts then selects the preferred one to be used by the plugin.
     *
     * @return preferred available Font configured for the plugin or a dummy Font if no preferred Font is available.
     */
    public static Font select() {
        final Font[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getAllFonts();
        for (final String preferredFontName : preferredFontNames) {
            for (final Font font : availableFonts) {
                if (font.getFontName()
                    .equals(preferredFontName)) {
                    return new Font(preferredFontName, FONT_STYLE, FONT_SIZE);
                }
            }
        }
        return new Font(DUMMY_FONT_NAME, FONT_STYLE, FONT_SIZE);
    }

}
