# SVG Badges plugin for SonarQube
Plugin for SonarQube that provides a set of webservices to retrieve projects' data as a SVG image similarily to travis-ci build status badges. You can see a running example below.

![Travis build status](https://travis-ci.org/QualInsight/qualinsight-plugins-sonarqube-badges.svg?branch=master) [![Quality Gate](http://nemo.sonarqube.org/api/badges/gate?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)](http://nemo.sonarqube.org/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)

## Installation 

In order to use this plugin on your SonarQube server instance, you need first to install it. The plugin is available in SonarQube's update center under the name "SVN Badges". 

## Usage

### Generating a Quality Gate status badge

If you chose to generate a badge representing the quality gate status of a project, five different images types can be generated depending on the project's status and SonarQube configuration:

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
<a href="<serverBaseURL>/dashboard/index/<key>"><img src="<serverBaseURL>/api/badges/gate?key=<key>"/></a>
```

Example:

```
<a href="http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"><img src="http://localhost:9000/api/badges/gate?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"/></a>
```

##### Markdown Link:

```
[![Quality Gate](<serverBaseURL>/api/badges/gate?key=<key>)](<serverBaseURL>/dashboard/index/<key>)
```

Example:

```
[![Quality Gate](http://localhost:9000/api/badges/gate?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)](http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)
```

### Measure badges

You can also generate a badge in order to retrieve any measure for a project.


##### HTML Link:

```
<a href="<serverBaseURL>/dashboard/index/<key>"><img src="<serverBaseURL>/api/badges/measure?key=<key>&metric=<core metric key>"/></a>
```

Example:

```
<a href="http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges"><img src="http://localhost:9000/api/badges/measure?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges&metric=coverage"/></a>
```

##### Markdown Link:

```
[![Quality Gate](<serverBaseURL>/api/badges/gate?key=<key>&metric=<core metric key>)](<serverBaseURL>/dashboard/index/<key>)
```

Example:

```
[![Quality Gate](http://localhost:9000/api/badges/measure?key=com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges&metric=coverage)](http://localhost:9000/dashboard/index/com.qualinsight.plugins.sonarqube:qualinsight-plugins-sonarqube-badges)
```


## REST documentation

The webservice is self-documented. Once installed, go to the webservice documentation page of your SonarQube instance and look at the documentation for ``/api/badges``.

## Known limitations

* if "force user authentication" is set on your SonarQube instance, then the webservice provided by the plugin is currently unreachable (see issue #15.) 

## Conclusion

New feature ideas and contributions are more than welcome. A [Google group](https://groups.google.com/forum/#!forum/svg-badges) named [SVG Bagdes](https://groups.google.com/forum/#!forum/svg-badges) has been created in order to facilitate discussions about this plugin. This project's quality can be followed on [Nemo](https://nemo.sonarqube.org/overview?id=com.qualinsight.plugins.sonarqube%3Aqualinsight-plugins-sonarqube-badges).


