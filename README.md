# SVG Status plugin for SonarQube
Plugin for SonarQube that provides a webservice to retrieve projects' quality gate status as an SVG image similarily to travis-ci build status images.

## Configuration

This plugin requires no manual configuration. It auto-configures itself using SonarQube ``sonar.core.serverBaseURL`` property value.

## Webservice API

The webservice is self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/status``.

## Image types

Five different images types are generated depending on the plugin's execution:
* ![Quality Gate passed](https://github.com/QualInsight/qualinsight-plugins-sonarqube-status/blob/master/images/passing.svg) indicates that the project passes the quality gate (QG)
* ![Quality Gate warning](https://github.com/QualInsight/qualinsight-plugins-sonarqube-status/blob/master/images/warning.svg) indicates that the project does not pass the quality gate due to QG warnings
* ![Quality Gate failed](https://github.com/QualInsight/qualinsight-plugins-sonarqube-status/blob/master/images/failing.svg) indicates that the project does not pass the quality gate due to QG errors
* ![No Quality Gate](https://github.com/QualInsight/qualinsight-plugins-sonarqube-status/blob/master/images/no_gate.svg) indicates that no quality gate has been set for the specified project
* ![Server error](https://github.com/QualInsight/qualinsight-plugins-sonarqube-status/blob/master/images/server_error.svg) indicates that a server error occured while generating the image

## Build status

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-status.svg?branch=master)
