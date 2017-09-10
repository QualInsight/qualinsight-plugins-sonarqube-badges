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
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
 * Holds measure badge data.
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
     * Get period value by period index
     *
     * @param periods PeriodsValue obtained from {@link Metric}
     * @param periodIndex Period Index that wants to be obtained. Possible values are 1, 2, or 3.
     *
     * @return String of metric value or <i>null</i>
     */
    private String getPeriodValueByPeriodIndex(PeriodsValue periods, int periodIndex) {
        String periodTempValue = null;
        for (PeriodValue periodvalue : periods.getPeriodsValueList()) {
            if (periodvalue.hasIndex() && periodvalue.getIndex() == periodIndex) {
                periodTempValue = periodvalue.getValue();
                break;
            }
        }
        if (periodTempValue == null) {
            periodTempValue = periods.getPeriodsValue(0).getValue();
        }
        return periodTempValue;
    }

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
     * @param requestedPeriod used to retrieve which period requested for which value will be constructed
     */
    @SuppressWarnings("unchecked")
    public MeasureHolder(final Measure measure, int requestedPeriod) {
        final DecimalFormat valueDf = new DecimalFormat("#.##");
        valueDf.setRoundingMode(RoundingMode.CEILING);
        final DecimalFormat periodValueDf = new DecimalFormat("+#.##;-#.##");
        periodValueDf.setRoundingMode(RoundingMode.CEILING);

        final Metric<Serializable> metric = CoreMetrics.getMetric(measure.getMetric());
        this.metricName = metric.getName()
            .replace(" (%)", "")
            .toLowerCase();
        String tempValue = null;
        String periodTempValue = null;
        if (measure.hasPeriods() && requestedPeriod > 0) {
            periodTempValue = this.getPeriodValueByPeriodIndex(measure.getPeriods(), requestedPeriod);
        }
        tempValue = measure.getValue();

        tempValue = tempValue != null ? valueDf.format(Double.parseDouble(tempValue)) : null;
        periodTempValue = periodTempValue != null ? periodValueDf.format(Double.parseDouble(periodTempValue)) : null;

        // Build value
        // If metric has no value, period's value will be treated as value
        // If metric has value, period's value will be treated as delta

        if (tempValue == null) {
            String percentage = metric.isPercentageType() ? "%" : "";
            this.value = periodTempValue == null ? NA : periodTempValue + percentage;
        } else {
            this.value = tempValue + (metric.isPercentageType() ? "%" : "");
            this.value = this.value + (periodTempValue != null ? " " + "(" + periodTempValue + ")" : "");
        }
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
            .append(this.backgroundColor)
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
            .append(this.backgroundColor, other.backgroundColor)
            .isEquals();
    }

}
