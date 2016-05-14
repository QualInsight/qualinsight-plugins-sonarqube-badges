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

import java.io.Serializable;
import java.util.NoSuchElementException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonarqube.ws.WsMeasures.Measure;
import org.sonarqube.ws.WsMeasures.PeriodValue;
import org.sonarqube.ws.WsMeasures.PeriodsValue;
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageColor;

/**
 * Holds measure data.
 *
 * @author Michel Pawlak
 */
public class MeasureHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasureHolder.class);

    private static final String NA = "N/A";

    private String metricName;

    private String value;

    private SVGImageColor backgroundColor = SVGImageColor.GRAY;

    /**
     * Constructs a MeasureHolder from a metric key.
     *
     * @param metricKey key used to retrieve the metric name for which the MeasureHolder is built
     */
    public MeasureHolder(final String metricKey) {
        try {
            this.metricName = CoreMetrics.getMetric(metricKey)
                .getName()
                .replace(" (%)", "")
                .toLowerCase();
        } catch (final NoSuchElementException e) {
            LOGGER.debug("Metric '{}' is not referenced in CoreMetrics.", metricKey, e);
            this.metricName = metricKey;
        }
        this.value = NA;
    }

    /**
     * Constructs a MeasureHolder from a Measure object.
     *
     * @param measure used to retrieve the metric name for which the MeasureHolder is built
     */
    @SuppressWarnings("unchecked")
    public MeasureHolder(final Measure measure) {
        final Metric<Serializable> metric = CoreMetrics.getMetric(measure.getMetric());
        this.metricName = metric.getName()
            .replace(" (%)", "")
            .toLowerCase();
        String tempValue = null;
        if (!measure.hasValue()) {
            if (measure.hasPeriods()) {
                final PeriodsValue periods = measure.getPeriods();
                final PeriodValue periodValue = periods.getPeriodsValue(0);
                tempValue = periodValue.getValue();
            }
        } else {
            tempValue = measure.getValue();
        }
        this.value = tempValue == null ? NA : tempValue + (metric.isPercentageType() ? "%" : "");
    }

    /**
     * Name of measured metric.
     *
     * @return name of measured metric
     */
    public String metricName() {
        return this.metricName;
    }

    /**
     * Measured value.
     *
     * @return measured value
     */
    public String value() {
        return this.value;
    }

    /**
     * Background color to be used in SVG image.
     *
     * @return background color as HEX string
     */
    public SVGImageColor backgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the background color used in SVG image
     *
     * @param backgroundColor color as a {@link SVGImageColor}
     */
    public void setBackgroundColor(final SVGImageColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.metricName)
            .append(this.value)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof MeasureHolder)) {
            return false;
        }
        final MeasureHolder other = (MeasureHolder) obj;
        return new EqualsBuilder().append(this.metricName, other.metricName)
            .append(this.value, other.value)
            .isEquals();
    }

}
