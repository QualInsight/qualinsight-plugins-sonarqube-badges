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

import java.awt.Color;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.sonarqube.ws.WsMeasures.Measure;

public class MeasureHolder {

    private static final String NA = "N/A";

    private static final Color COLOR = new Color(150, 150, 150, 255);

    private String metric;

    private String value;

    public MeasureHolder(final String metric) {
        this.metric = metric;
        this.value = NA;
    }

    public MeasureHolder(final Measure measure) {
        this.metric = measure.getMetric();
        this.value = measure.getValue();
    }

    String metric() {
        return this.metric.replace('_', ' ');
    }

    String value() {
        return this.value + (this.metric.endsWith("_density") ? " %" : "");
    }

    Color color() {
        return COLOR;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.metric)
            .append(this.value)
            .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof MeasureHolder)) {
            return false;
        }
        final MeasureHolder other = (MeasureHolder) obj;
        return new EqualsBuilder().append(this.metric, other.metric)
            .append(this.value, other.value)
            .isEquals();
    }

}
