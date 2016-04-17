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
package com.qualinsight.plugins.sonarqube.badges.exception;

import java.io.IOException;

/**
 * Exception thrown if a problem occurs during the process of font replacement in SVG image.
 * 
 * @author Michel Pawlak
 */
public class SVGImageFontReplacementException extends IOException {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    public SVGImageFontReplacementException(final Exception e) {
        super(e);
    }

}
