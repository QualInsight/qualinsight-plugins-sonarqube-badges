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
package com.qualinsight.plugins.sonarqube.badges.ws.measure;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.ServerSide;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageData;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageGenerator;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageMinimizer;

/**
 * Generates SVG badge based on a measure value. A reusable {@link InputStream} is kept in a cache for each generated image in order to decrease computation time.
 *
 * @author Michel Pawlak
 */
@ServerSide
public final class MeasureBadgeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasureBadgeGenerator.class);

    private final Map<MeasureHolder, InputStream> measureBadgesMap = new HashMap<>();

    private SVGImageGenerator imageGenerator;

    private SVGImageMinimizer minimizer;

    /**
     * {@link MeasureBadgeGenerator} IoC constructor.
     *
     * @param imageGenerator {@link SVGImageGenerator} service to be used.
     * @param fontReplacer {@link SVGImageMinimizer} service to be used.
     */
    public MeasureBadgeGenerator(final SVGImageGenerator imageGenerator, final SVGImageMinimizer fontReplacer) {
        this.imageGenerator = imageGenerator;
        this.minimizer = fontReplacer;
        LOGGER.info("MeasureBadgeGenerator is now ready.");
    }

    /**
     * Returns an {@link InputStream} holding the content of the generated image for the provided quality gate status. All {@link InputStream}s are cached for future reuse.
     *
     * @param measureHolder measure for which the image has to be generated
     * @return {@link InputStream} holding the expected SVG image
     * @throws IOException if a IO problem occurs during streams manipulation
     */
    public InputStream svgImageInputStreamFor(final MeasureHolder measureHolder) throws IOException {
        InputStream svgImageRawInputStream;
        InputStream svgImageTransformedInputStream;
        if (this.measureBadgesMap.containsKey(measureHolder)) {
            LOGGER.debug("Found SVG image for {} status in cache, reusing it.");
            svgImageTransformedInputStream = this.measureBadgesMap.get(measureHolder);
            // we don't trust previous InpuStream user, so we reset the position of the InpuStream
            svgImageTransformedInputStream.reset();
        } else {
            LOGGER.debug("Generating SVG image for {} status, then caching it.");
            final SVGImageData data = SVGImageData.Builder.instance(this.imageGenerator.fontProvider())
                .withLabelText(measureHolder.metricName())
                .withLabelBackgroundColor(SVGImageGenerator.DEFAULT_LABEL_BACKGROUND_COLOR)
                .withValueText(measureHolder.value())
                .withValueBackgroundColor(measureHolder.backgroundColor())
                .build();
            svgImageRawInputStream = this.imageGenerator.generateFor(data);
            // minimze SVG stream
            svgImageTransformedInputStream = this.minimizer.process(svgImageRawInputStream);
            // mark svgImageInputStream position to make it reusable
            svgImageTransformedInputStream.mark(Integer.MAX_VALUE);
            // put it into cache
            this.measureBadgesMap.put(measureHolder, svgImageTransformedInputStream);
        }
        return svgImageTransformedInputStream;
    }
}
