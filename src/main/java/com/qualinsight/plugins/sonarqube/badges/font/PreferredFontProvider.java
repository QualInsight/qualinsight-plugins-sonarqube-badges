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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qualinsight.plugins.sonarqube.badges.exception.FontLoadingException;

/**
 * {@link FontProvider} that provides the preferred {@link Font} if it is available on the machine running SonarQube.
 *
 * @author Michel Pawlak
 */
public class PreferredFontProvider extends FontProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferredFontProvider.class);

    private static final String FONT_NAME = "Verdana";

    private static final int FONT_SIZE = 11;

    private static final int FONT_STYLE = Font.PLAIN;

    private static final String FONT_FAMILY = "DejaVu Sans,Verdana,Sans PT,Lucida Grande,Tahoma,Helvetica,Arial,sans-serif";

    /**
     * Constructor that loads the preferred {@link Font} or throws a {@link FontLoadingException}.
     *
     * @throws FontLoadingException if the preferred {@link Font} cannot be loaded.
     */
    public PreferredFontProvider() throws FontLoadingException {
        final Font preferredFont = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        if (preferredFont.getFontName()
            .equals(Font.DIALOG)) {
            throw new FontLoadingException();
        }
        LOGGER.info("SVGImageGenerator will be using font '{}' in order to compute SVG badges width.", preferredFont.getName());
        setFont(preferredFont);
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
