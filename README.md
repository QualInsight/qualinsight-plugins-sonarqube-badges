# SVG Status plugin for SonarQube
Plugin for SonarQube that provides a webservice to retrieve projects' quality gate status as an SVG image similarily to travis-ci build status images.

## Configuration

This plugin requires no manual configuration. It auto-configures itself using SonarQube ``sonar.core.serverBaseURL`` property value.

## Webservice API

The webservice is self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/status``.

## Image types

Five different images types are generated depending on the plugin's execution:
* [Passing](images/passing.svg) indicates that the project passes the quality gate (QG)
* [Warning](images/warning.svg) indicates that the project does not pass the quality gate due to QG warnings
* [Failing](images/failing.svg) indicates that the project does not pass the quality gate due to QG errors
* [No Gate](images/no_gate.svg) indicates that no quality gate has been set for the specified project
* [Server error](images/server_error.svg) indicates that a server error occured while generating the image

_Note 1:_ Unfortunately it appears that GitHub does not render SVG images in ``README.md`` file due to potential cross site scripting vulnerabilities. To display the different images you have thus to click on them.

## Build status

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-status.svg?branch=master)
