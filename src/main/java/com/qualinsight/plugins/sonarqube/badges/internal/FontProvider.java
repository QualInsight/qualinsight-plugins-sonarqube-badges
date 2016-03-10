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

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class FontProvider {

    private enum FontFamily {
        VERDANA("Verdana"),
        DEJAVU_SANS("DejaVu Sans"),
        TAHOMA("Tahoma"),
        GENEVA("Geneva"),
        ARIAL("Arial");

        private static final int FONT_SIZE = 11;

        private static final String DEFAULT_FONT_FAMILY = "Dialog";

        private final Font font;

        private FontFamily(final String name) {
            final Map<TextAttribute, Object> fontAttributes = new HashMap<>();
            fontAttributes.put(TextAttribute.FAMILY, name);
            fontAttributes.put(TextAttribute.SIZE, FONT_SIZE);
            fontAttributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
            fontAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
            this.font = new Font(fontAttributes);
        }

        public boolean isAvailable() {
            // if a font is not available, java defaults to the Dialog font.
            return !DEFAULT_FONT_FAMILY.equals(this.font.getFamily());
        }

        public Font font() {
            return this.font;
        }
    }

    public static final Font preferredFont() {
        for (final FontFamily fontFamily : FontFamily.values()) {
            if (fontFamily.isAvailable()) {
                return fontFamily.font();
            }
        }
        return new Font(Font.SANS_SERIF, Font.PLAIN, FontFamily.FONT_SIZE);
    }

}
