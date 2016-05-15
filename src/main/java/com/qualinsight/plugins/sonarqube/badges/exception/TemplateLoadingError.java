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
import com.qualinsight.plugins.sonarqube.badges.ws.SVGImageTemplate;

/**
 * Thrown if a problem occurs when trying to load {@link SVGImageTemplate}.
 *
 * @author Michel Pawlak
 */
public class TemplateLoadingError extends Error {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to be used if a {@link IOException} occurs.
     *
     * @param cause cause bound to the {@link Exception}
     */
    public TemplateLoadingError(final IOException cause) {
        super("Template could not be loaded", cause);
    }

    /**
     * Constructor to be used if a template file is not found.
     *
     * @param cause cause bound to the {@link Exception}
     */
    public TemplateLoadingError(final String templatePath) {
        super("Template could not be loaded: " + templatePath);
    }
}
