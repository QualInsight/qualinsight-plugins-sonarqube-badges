# SVG Badges plugin for SonarQube
Plugin for SonarQube that provides a webservice to retrieve projects' quality gate status as a SVG image similarily to travis-ci build status badges. 

## Badges types

Five different images types are generated depending on the plugin's execution:
* [Passing](images/passing.svg) indicates that the project passes the quality gate (QG)
* [Warning](images/warning.svg) indicates that the project does not pass the quality gate due to QG warnings
* [Failing](images/failing.svg) indicates that the project does not pass the quality gate due to QG errors
* [No Gate](images/no_gate.svg) indicates that no quality gate has been set for the specified project
* [Server error](images/server_error.svg) indicates that a server error occured while generating the image

Unfortunately it appears that GitHub does not render SVG images in ``README.md`` file due to potential cross site scripting vulnerabilities. To display the different images you have thus to click on them.

## Usage

In order to use this plugin, you need to install it, then set SonarQube's ``sonar.core.serverBaseURL`` property to the URL of your SonarQube's server instance.

This configuration step is mandatory. If the ``sonar.core.serverBaseURL`` property is not set (i.e. default value is used) the plugin may behave incorrectly as SonarQube REST API may be unreachable. 

Notes:
- if authentication is required on your SonarQube instance in order to access a project's page, then the plugin is currently unable to work due to a SonarQube limitation (see issue #15.) 

### Linking your badge to a project's page

Once the plugin is installed, you can display generated badge and link to your SonarQube project's or view's page as follows.

##### HTML Link:

```
<a href="<serverBaseURL>/dashboard/index/<key>"><img src="<serverBaseURL>/api/badges/image?key=<key>"/></a>
```

Example:

```
<a href="http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"><img src="http://localhost:9000/api/badges/image?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"/></a>
```

##### Markdown Link:

```
[![Quality Gate](<serverBaseURL>/api/badges/image?key=<key>)](<serverBaseURL>/dashboard/index/<key>)
```

Example:

```
[![Quality Gate](http://localhost:9000/api/badges/image?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)](http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)
```

### Direct Webservice API usage

The webservice is self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/badges``.

## Conclusion

New feature ideas and contributions are more than welcome. A [Google group](https://groups.google.com/forum/#!forum/svg-badges) named [SVG Bagdes](https://groups.google.com/forum/#!forum/svg-badges) has been created in order to facilitate discussions about this plugin. This project's quality can be followed on [Nemo](https://nemo.sonarqube.org/overview?id=com.qualinsight.plugins.sonarqube%3Aqualinsight-plugins-sonarqube-badges).

## Build status

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-badges.svg?branch=master)
