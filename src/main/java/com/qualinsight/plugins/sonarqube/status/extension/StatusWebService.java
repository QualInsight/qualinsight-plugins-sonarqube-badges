/*
 * qualinsight-plugins-sonarqube-status
 * Copyright (c) 2015, QualInsight
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
package com.qualinsight.plugins.sonarqube.status.extension;

import java.io.OutputStream;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import com.qualinsight.plugins.sonarqube.status.internal.QualityGateStatus;
import com.qualinsight.plugins.sonarqube.status.internal.QualityGateStatusRetriever;
import com.qualinsight.plugins.sonarqube.status.internal.SVGImageGenerator;

public class StatusWebService implements WebService {

    private QualityGateStatusRetriever qualityGateStatusRetriever;

    private SVGImageGenerator svgImageGenerator;

    public StatusWebService(final QualityGateStatusRetriever qualityGateStatusRetriever, final SVGImageGenerator svgImageGenerator) {
        this.qualityGateStatusRetriever = qualityGateStatusRetriever;
        this.svgImageGenerator = svgImageGenerator;
    }

    @Override
    public void define(final Context context) {
        final NewController controller = context.createController("api/status");
        controller.setDescription("Status SVG image web service");
        controller.createAction("image")
            .setDescription("Retrieve the quality gate status of a project as a SVG image.")
            .setHandler(new RequestHandler() {

                @Override
                public void handle(final Request request, final Response response) throws Exception {
                    final QualityGateStatus status = StatusWebService.this.qualityGateStatusRetriever.retrieveFor(request.mandatoryParam("key"));
                    final OutputStream responseOutputStream = response.stream()
                        .setMediaType("image/svg+xml")
                        .output();
                    StatusWebService.this.svgImageGenerator.generate(status, responseOutputStream);
                    responseOutputStream.close();
                }
            })
            .createParam("key")
            .setDescription("Key of the project")
            .setExampleValue("org.codehaus.sonar:sonar")
            .setRequired(true);
        controller.done();
    }

}
