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
import org.apache.commons.io.IOUtils;
import com.qualinsight.plugins.sonarqube.badges.exception.TemplateLoadingError;

public enum SVGImageTemplate {
    DEFAULT("/com/qualinsight/plugins/sonarqube/badges/ws/badge-default-template.svg"),
    FLAT("/com/qualinsight/plugins/sonarqube/badges/ws/badge-flat-template.svg");

    private String content;

    SVGImageTemplate(final String templatePath) {
        final InputStream resourceStream = getClass().getResourceAsStream(templatePath);
        try {
            if (resourceStream != null) {
                this.content = IOUtils.toString(resourceStream);
            } else {
                throw new TemplateLoadingError(templatePath);
            }
        } catch (final IOException e) {
            throw new TemplateLoadingError(e);
        }
    }

    public String content() {
        return this.content;
    }

}
