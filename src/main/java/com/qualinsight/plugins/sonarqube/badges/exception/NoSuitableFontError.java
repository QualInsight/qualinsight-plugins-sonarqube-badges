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

import java.awt.Font;

/**
 * Thrown if a problem occurs when trying to load embedded fallback {@link Font}.
 *
 * @author Michel Pawlak
 */
public class NoSuitableFontError extends Error {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor that sets a generic message explaining the issue.
     * 
     * @param cause cause bound to the {@link Exception}
     */
    public NoSuitableFontError(final Exception cause) {
        super("Fallback font could not be loaded", cause);
    }
}
