# SVG Status plugin for SonarQube
Plugin for SonarQube that provides a webservice to retrieve projects' quality gate status as an SVG image similarily to travis-ci build status images.

## Configuration

This plugin requires no manual configuration. It auto-configures itself using SonarQube ``sonar.core.serverBaseURL`` property value.

## Webservice API

The webservice is self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/status``.

## Image types

Five different images types are generated depending on the plugin's execution:
* "Passing" indicates that the project passes the quality gate (QG)
* "Warning" indicates that the project does not pass the quality gate due to QG warnings
* "Failing" indicates that the project does not pass the quality gate due to QG errors
* "No gate" indicates that no quality gate has been set for the specified project
* "Server error" indicates that a server error occured while generating the image

## Build status

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-status.svg?branch=master)
