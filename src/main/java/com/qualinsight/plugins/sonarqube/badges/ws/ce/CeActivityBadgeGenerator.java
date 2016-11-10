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

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.ServerSide;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageData;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageGenerator;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageMinimizer;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageTemplate;

/**
 * Generates SVG badge based on a Compute Engine Activity status. A reusable {@link InputStream} is kept in a cache for each generated image in order to decrease computation time.
 *
 */
@ServerSide
public class CeActivityBadgeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CeActivityBadgeGenerator.class);

    private static final String LABEL_TEXT = "ce activity";

    private final Map<SVGImageTemplate, Map<CeActivityBadge, InputStream>> ceActivityBadgesMap = new EnumMap<>(SVGImageTemplate.class);

    private final Map<SVGImageTemplate, Map<CeActivityBadge, InputStream>> ceActivityBlinkingBadgesMap = new EnumMap<>(SVGImageTemplate.class);

    private SVGImageGenerator imageGenerator;

    private SVGImageMinimizer minimizer;

    /**
     * {@link ceActivityBadgeGenerator} IoC constructor.
     *
     * @param imageGenerator {@link SVGImageGenerator} service to be used.
     * @param fontReplacer {@link SVGImageMinimizer} service to be used.
     */
    public CeActivityBadgeGenerator(final SVGImageGenerator imageGenerator, final SVGImageMinimizer fontReplacer) {
        this.imageGenerator = imageGenerator;
        this.minimizer = fontReplacer;
        for (final SVGImageTemplate template : SVGImageTemplate.values()) {
            this.ceActivityBadgesMap.put(template, new EnumMap<CeActivityBadge, InputStream>(CeActivityBadge.class));
            this.ceActivityBlinkingBadgesMap.put(template, new EnumMap<CeActivityBadge, InputStream>(CeActivityBadge.class));
        }
        LOGGER.info("CeActivityBadgeGenerator is now ready.");
    }

    /**
     * Returns an {@link InputStream} holding the content of the generated image for the provided ce activity status. All {@link InputStream} s are cached for future reuse.
     *
     * @param status ce activity status for which the image has to be generated
     * @param template {@link SVGImageTemplate} to be used
     * @param blinkingValueBackgroundColor true if the badge must be blinking in case of ce activity error
     * @return {@link InputStream} holding the expected SVG image
     * @throws IOException if a IO problem occurs during streams manipulation
     */
    public InputStream svgImageInputStreamFor(final CeActivityBadge status, final SVGImageTemplate template, final boolean blinkingValueBackgroundColor) throws IOException {
        InputStream svgImageRawInputStream;
        InputStream svgImageTransformedInputStream;
        Map<SVGImageTemplate, Map<CeActivityBadge, InputStream>> workingMap;
        if (blinkingValueBackgroundColor) {
            workingMap = this.ceActivityBlinkingBadgesMap;
        } else {
            workingMap = this.ceActivityBadgesMap;
        }
        if (workingMap.containsKey(status)) {
            LOGGER.debug("Found SVG image for '{}' ce activity status in cache, reusing it.", status);
            svgImageTransformedInputStream = workingMap.get(template)
                .get(status);
            // we don't trust previous InpuStream user, so we reset the position of the InpuStream
            svgImageTransformedInputStream.reset();
        } else {
            LOGGER.debug("Generating SVG image for '{}' ce activity status, then caching it.", status);
            final SVGImageData data = SVGImageData.Builder.instance(this.imageGenerator.fontProvider())
                .withTemplate(template)
                .withLabelText(LABEL_TEXT)
                .withLabelBackgroundColor(SVGImageColor.DARK_GRAY)
                .withValueText(status.displayText())
                .withValueBackgroundColor(status.displayBackgroundColor())
                .build();
            svgImageRawInputStream = this.imageGenerator.generateFor(data);
            // set parameters
            final Map<String, Object> parameters = ImmutableMap.<String, Object> builder()
                .put("IS_BLINKING_BADGE", Boolean.toString(blinkingValueBackgroundColor && status.equals(CeActivityBadge.FAILED)))
                .build();
            // minimze SVG stream
            svgImageTransformedInputStream = this.minimizer.process(svgImageRawInputStream, parameters);
            // mark svgImageInputStream position to make it reusable
            svgImageTransformedInputStream.mark(Integer.MAX_VALUE);
            // put it into cache
            workingMap.get(template)
                .put(status, svgImageTransformedInputStream);
        }
        return svgImageTransformedInputStream;
    }
}
