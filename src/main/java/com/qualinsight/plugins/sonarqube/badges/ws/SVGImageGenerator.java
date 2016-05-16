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
package com.qualinsight.plugins.sonarqube.badges.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.ServerSide;
import com.qualinsight.plugins.sonarqube.badges.font.FontProvider;
import com.qualinsight.plugins.sonarqube.badges.font.FontProviderLocator;

/**
 * Generates SVG images.
 *
 * @author Michel Pawlak
 */
@ServerSide
public final class SVGImageGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SVGImageGenerator.class);

    private FontProvider fontProvider;

    /**
     * {@link SVGImageGenerator} IoC constructor.
     *
     * @param fontProviderLocator {@link FontProviderLocator} that will give access to a {@link FontProvider}.
     */
    public SVGImageGenerator(final FontProviderLocator fontProviderLocator) throws IOException {
        this.fontProvider = fontProviderLocator.fontProvider();
        LOGGER.info("SVGImageGenerator is now ready.");
    }

    /**
     * Generates a SVG image object from a provided Data object.
     *
     * @param data Data object holding data required to produce a SVGGraphics2D object
     * @return generated InputStream to read SVG image from
     */
    public InputStream generateFor(final SVGImageData data) {

        final Map<String, String> replacements = new HashMap<>();
        replacements.put("{{fontFamily}}", data.fontFamily());
        replacements.put("{{labelText}}", data.labelText());
        replacements.put("{{labelBackgroundColor}}", data.labelBackgroundColor()
            .hexColor());
        replacements.put("{{labelWidth}}", data.labelWidth());
        replacements.put("{{labelHalfWidth}}", data.labelHalfWidth());
        replacements.put("{{valueText}}", data.valueText());
        replacements.put("{{valueBackgroundColor}}", data.valueBackgroundColor()
            .hexColor());
        replacements.put("{{valueWidth}}", data.valueWidth());
        replacements.put("{{valueHalfWidth}}", data.valueHalfWidth());
        replacements.put("{{totalWidth}}", data.totalWidth());
        return IOUtils.toInputStream(StringUtils.replaceEach(data.template()
            .content(), replacements.keySet()
            .toArray(new String[0]), replacements.values()
            .toArray(new String[0])));
    }

    /**
     * Returns the {@link FontProvider} to be used by badge generators.
     *
     * @return font
     */
    public FontProvider fontProvider() {
        return this.fontProvider;
    }

}
