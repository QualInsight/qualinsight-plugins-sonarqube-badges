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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.sonar.api.server.ServerSide;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.qualinsight.plugins.sonarqube.badges.exception.SVGImageMinimizerException;

/**
 * Server extension that takes care of SVG images miniming.
 *
 * @author Michel Pawlak
 */
@ServerSide
public class SVGImageMinimizer {

    private final DocumentBuilder builder;

    private final Transformer transformer;

    /**
     * IoC constructor
     *
     * @throws SVGImageMinimizerException if a problem occurs during initialization
     */
    public SVGImageMinimizer() throws SVGImageMinimizerException {
        try {
            InputStream xslInputStream = null;
            try {
                xslInputStream = getClass().getClassLoader()
                    .getResourceAsStream("com/qualinsight/plugins/sonarqube/badges/ws/svg-minimizer.xsl");
                final TransformerFactory transformerFactory = TransformerFactory.newInstance();
                this.transformer = transformerFactory.newTransformer(new StreamSource(xslInputStream));
            } finally {
                if (null != xslInputStream) {
                    xslInputStream.close();
                }
            }
            final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setValidating(false);
            builderFactory.setNamespaceAware(true);
            builderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            builderFactory.setFeature("http://xml.org/sax/features/validation", false);
            builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            this.builder = builderFactory.newDocumentBuilder();
        } catch (final IOException | TransformerConfigurationException | ParserConfigurationException e) {
            throw new SVGImageMinimizerException(e);
        }
    }

    /**
     * Processes font transformation on an SVG input stream.
     *
     * @param inputStream InputStream that contains the SVG image to be transformed.
     * @param parameters parameters to set before minimization.
     * @return an InputStream with transformed content.
     * @throws SVGImageMinimizerException if a problem occurs during stream transformation.
     */
    public InputStream process(final InputStream inputStream, final Map<String, Object> parameters) throws SVGImageMinimizerException {
        reset();
        try {
            final Document document = this.builder.parse(inputStream);
            final Source source = new DOMSource(document);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final Result result = new StreamResult(outputStream);
            for (final Entry<String, Object> parameter : parameters.entrySet()) {
                this.transformer.setParameter(parameter.getKey(), parameter.getValue());
            }
            this.transformer.transform(source, result);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (final IOException | TransformerException | SAXException e) {
            throw new SVGImageMinimizerException(e);
        }
    }

    private void reset() {
        this.transformer.clearParameters();
        this.transformer.reset();
        this.builder.reset();
    }

}
