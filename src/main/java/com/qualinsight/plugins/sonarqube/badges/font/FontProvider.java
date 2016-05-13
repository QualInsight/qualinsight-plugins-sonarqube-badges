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
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public abstract class FontProvider {

    private static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(new AffineTransform(), true, true);

    private Font font;

    /**
     * Set the font held by the {@link FontProvider}.
     *
     * @param font {@link Font} that will be used to generate SVG Badges.
     */
    protected void setFont(final Font font) {
        this.font = font;
    }

    /**
     * Returns the font held by the {@link FontProvider}.
     *
     * @return {@link Font} to be used to generate SVG Badges.
     */
    public Font font() {
        return this.font;
    }

    /**
     * Shortcut method that returns the name of the {@link Font} held by the {@link FontProvider}.
     *
     * @return {@link Font} name.
     */
    abstract public String fontName();

    /**
     * Method that returns the names of the font families that have to be specified in resulting SVG image and that will be used client side.
     *
     * @return {@link Font} family names as a a CSS compatible String.
     */
    abstract public String fontFamilyName();

    /**
     * Computes the width in pixels of a text String when using the {@link Font} held by the {@link FontProvider}.
     *
     * @param text Text to be mesured
     * @return text width in pixels
     */
    public int computeWidth(final String text) {
        final Rectangle2D stringBounds = this.font.getStringBounds(text, FONT_RENDER_CONTEXT);
        return (int) (stringBounds.getWidth());
    }

}
