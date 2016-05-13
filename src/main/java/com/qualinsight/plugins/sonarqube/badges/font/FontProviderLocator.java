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
package com.qualinsight.plugins.sonarqube.badges.font;

import java.awt.Font;
import org.sonar.api.server.ServerSide;
import com.qualinsight.plugins.sonarqube.badges.exception.FontLoadingException;

/**
 * Class that locates a suitable {@link FontProvider}.
 *
 * @author Michel Pawlak
 */
@ServerSide
public class FontProviderLocator {

    private FontProvider fontProvider;

    /**
     * Constructor that locates the most suitable {@link FontProvider}.
     */
    public FontProviderLocator() {
        try {
            this.fontProvider = new PreferredFontProvider();
        } catch (final FontLoadingException e) {
            this.fontProvider = new FallbackFontProvider();
        }
    }

    /**
     * Returns a {@link FontProvider} giving access to the preferred {@link Font} or fallback {@link Font}.
     * 
     * @return the most suitable {@link FontProvider}
     */
    public FontProvider fontProvider() {
        return this.fontProvider;
    }

}
