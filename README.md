# SVG Badges plugin for SonarQube
Plugin for SonarQube that provides a set of webservices to retrieve projects' data as a SVG image similarily to travis-ci build status badges. You can see a running example below.

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-badges.svg?branch=master) [![Quality Gate](http://nemo.sonarqube.org/api/badges/gate?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)](http://nemo.sonarqube.org/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)

## Installation 

In order to use this plugin on your SonarQube server instance, you need first to install it. The plugin is available in SonarQube's update center under the name "SVG Badges". 

## Usage

Webservices provided by the plugin are self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/badges``.

### Generating a Quality Gate status badge

Using ``/api/badges/gate?key=<project or view key>`` you can generate a badge to display the quality gate status of a project or view. Five different images types can be generated as a result, depending on the project's status and SonarQube configuration:

* [Passing](images/passing.svg) indicates that the project passes the quality gate (QG)
* [Warning](images/warning.svg) indicates that the project does not pass the quality gate due to QG warnings
* [Failing](images/failing.svg) indicates that the project does not pass the quality gate due to QG errors
* [No Gate](images/no_gate.svg) indicates that no quality gate has been set for the specified project
* [Not Found](images/not_found.svg) indicates that the project / view could not be found

#### Displaying your badge on a web page

You can display generated badges using HTML or Markdown as follows.

Note that the plugin is currently installed on SonarQube's Nemo public instance. If you want to display a badge for one of the Opensource project analyzed on Nemo, just use ``nemo.sonarqube.org`` as ``<serverBaseURL>``.

##### HTML Link:

```
<a href="<serverBaseURL>/dashboard/index/<project or view key>"><img src="<serverBaseURL>/api/badges/gate?key=<project or view key>"/></a>
```

Example:

```
<a href="http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"><img src="http://localhost:9000/api/badges/gate?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"/></a>
```

##### Markdown Link:

```
[![Quality Gate](<serverBaseURL>/api/badges/gate?key=<project or view key>)](<serverBaseURL>/dashboard/index/<project or view key>)
```

Example:

```
[![Quality Gate](http://localhost:9000/api/badges/gate?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)](http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)
```

### Generating a project measure badge

Using ``/api/badges/measure?key=<project or view key>&metric=<metric key>`` you can generate a badge to display any measure related to a project. SonarQube's [CoreMetrics class](https://github.com/SonarSource/sonarqube/blob/master/sonar-plugin-api/src/main/java/org/sonar/api/measures/CoreMetrics.java) lists all `metric keys` that can be used.

##### HTML Link:

```
<a href="<serverBaseURL>/dashboard/index/<project or view key>"><img src="<serverBaseURL>/api/badges/measure?key=<project or view key>&metric=<core metric key>"/></a>
```

Example:

```
<a href="http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"><img src="http://localhost:9000/api/badges/measure?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges&metric=coverage"/></a>
```

##### Markdown Link:

```
[![Quality Gate](<serverBaseURL>/api/badges/gate?key=<project or view key>&metric=<core metric key>)](<serverBaseURL>/dashboard/index/<project or view key>)
```

Example:

```
[![Quality Gate](http://localhost:9000/api/badges/measure?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges&metric=coverage)](http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)
```

## Known limitations

If the security option "force user authentication" is set on your SonarQube instance, then all webservices provided by the plugin are unreachable unless the user is authenticated. As a result, badges cannot be displayed if this option is set (see issue [#15](https://github.com/QualInsight/qualinsight-plugins-sonarqube-badges/issues/15) and [SONAR-6948](https://jira.sonarsource.com/browse/SONAR-6948).) 

## Conclusion

New feature ideas and contributions are more than welcome. A [Google group](https://groups.google.com/forum/#!forum/svg-badges) named [SVG Bagdes](https://groups.google.com/forum/#!forum/svg-badges) has been created in order to facilitate discussions about this plugin. This project's quality can be followed on [Nemo](https://nemo.sonarqube.org/overview?id=com.qualinsight.plugins.sonarqube%3Aqualinsight-plugins-sonarqube-badges).


