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
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qualinsight.plugins.sonarqube.badges.exception.NoSuitableFontError;

/**
 * {@link FontProvider} that provides fallback {@link Font} that is embedded with the plugin. Should only be used if the preferred {@link Font} cannot be loaded.
 *
 * @author Michel Pawlak
 */
public class FallbackFontProvider extends FontProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FallbackFontProvider.class);

    private static final String FONT_PATH = "/com/qualinsight/plugins/sonarqube/badges/font/PTC55F.ttf";

    private static final int FONT_SIZE = 11;

    private static final int FONT_STYLE = Font.PLAIN;

    private static final String FONT_FAMILY = "Sans PT', 'Lucida Grande', 'Tahoma', 'Helvetica', 'Arial', 'sans-serif";

    /**
     * Contructor that loads the fallback {@link Font}.
     */
    public FallbackFontProvider() {
        final InputStream myStream = getClass().getResourceAsStream(FONT_PATH);
        Font importedFont;
        try {
            importedFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
        } catch (FontFormatException | IOException e) {
            throw new NoSuitableFontError(e);
        }
        final Font font = importedFont.deriveFont(FONT_STYLE, FONT_SIZE);
        LOGGER.info("SVGImageGenerator will be using fallback font '{}' in order to compute SVG badges width.", font.getName());
        setFont(font);
    }

    @Override
    public String fontName() {
        return font().getFontName();
    }

    @Override
    public String fontFamilyName() {
        return FONT_FAMILY;
    }

}
