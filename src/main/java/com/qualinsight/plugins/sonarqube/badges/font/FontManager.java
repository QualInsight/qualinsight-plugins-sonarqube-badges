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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.ServerSide;

/**
 * Helper class that selects the preferred Font from a specfied list.
 *
 * @author Michel Pawlak
 */
@ServerSide
public final class FontManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FontManager.class);

    private static final String DUMMY_FONT_NAME = "DUMMY_FONT_NAME";

    private static final int FONT_SIZE = 11;

    private static final int FONT_STYLE = Font.PLAIN;

    private static final List<String> preferredFontNames = ImmutableList.of("DejaVu Sans", "Verdana", "Geneva", "Tahoma", "Arial", "sans-serif");

    private final Font preferredFont;

    private final String fontFamily;

    /**
     * IoC Constructor.
     */
    public FontManager() {
        this.preferredFont = detectPreferredFont();
        this.fontFamily = detectFontFamily();
        LOGGER.info("Preferred font name: '{}'", this.preferredFont.getFontName());
        LOGGER.info("Font family: {}", this.fontFamily);
    }

    /**
     * Font that will be used to generate SVG image.
     *
     * @return Font
     */
    public Font preferredFont() {
        return this.preferredFont;
    }

    /**
     * String that will be used to set the font-family attribute in produced SVG images.
     *
     * @return Font family String
     */
    public String fontFamily() {
        return this.fontFamily;
    }

    /**
     * Scans available Fonts then selects the preferred one to be used by the plugin.
     *
     * @return preferred available Font configured for the plugin or a dummy Font if no preferred Font is available.
     */
    private Font detectPreferredFont() {
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

    /**
     * Scans available Fonts then keep those available among preferred fonts to build a font family string.
     *
     * @return font family string.
     */
    private String detectFontFamily() {
        final Font[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getAllFonts();
        final StringBuilder sb = new StringBuilder();
        for (final String preferredFontName : preferredFontNames) {
            for (final Font font : availableFonts) {
                if (font.getFontName()
                    .equals(preferredFontName)) {
                    sb.append("'")
                        .append(preferredFontName)
                        .append("',");
                }
            }
        }
        return StringUtils.removeEnd(sb.toString(), ",");
    }

}
