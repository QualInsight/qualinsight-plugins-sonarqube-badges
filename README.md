# SVG Status plugin for SonarQube
Plugin for SonarQube that provides a webservice to retrieve projects' quality gate status as an SVG image similarily to travis-ci build status images. 

_Note 1:_ Generated images are cached in order to lower computing time.

## Image types

Five different images types are generated depending on the plugin's execution:
* [Passing](images/passing.svg) indicates that the project passes the quality gate (QG)
* [Warning](images/warning.svg) indicates that the project does not pass the quality gate due to QG warnings
* [Failing](images/failing.svg) indicates that the project does not pass the quality gate due to QG errors
* [No Gate](images/no_gate.svg) indicates that no quality gate has been set for the specified project
* [Server error](images/server_error.svg) indicates that a server error occured while generating the image

_Note 2:_ Unfortunately it appears that GitHub does not render SVG images in ``README.md`` file due to potential cross site scripting vulnerabilities. To display the different images you have thus to click on them.

## Usage

This plugin requires no manual configuration. It auto-configures itself using SonarQube ``sonar.core.serverBaseURL`` property value. 

### Adding an image link

Once the plugin is installed, you can display generated status image and link to your SonarQube project's or view's page as follows.

##### HTML Link:

```
<a href="<serverBaseURL>/dashboard/index/<key>"><img src="<serverBaseURL>/api/status/image?key=<key>"/></a>
```

Example:

```
<a href="http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-status"><img src="http://localhost:9000/api/status/image?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-status"/></a>
```

##### Markdown Link:

```
[![Quality Gate](<serverBaseURL>/api/status/image?key=<key>)](<serverBaseURL>/dashboard/index/<key>)
```

Example:

```
[![Quality Gate](http://localhost:9000/api/status/image?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-status)](http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-status)
```

### Direct Webservice API usage

The webservice is self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/status``.

## Build status

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-status.svg?branch=master)
