#!/bin/sh

if [ "${TRAVIS_PULL_REQUEST}" != "false" ] 
then
	echo "Running SonarQube analysis for Pull Request '${TRAVIS_PULL_REQUEST}'"
	mvn clean verify sonar:sonar \
		-Dsonar.analysis.mode=preview \
		-Dsonar.host.url=${SONAR_HOST_URL} \
		-Dsonar.login=${SONAR_TOKEN} \
		-Dsonar.github.login=${GITHUB_LOGIN} \
		-Dsonar.github.oauth=${GITHUB_TOKEN} \
		-Dsonar.github.repository=${TRAVIS_REPO_SLUG} \
		-Dsonar.github.pullRequest=${TRAVIS_PULL_REQUEST}
elif [ "${TRAVIS_BRANCH}" = "master" ]
then
	echo "Running build and SonarQube analysis"
	mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent verify sonar:sonar \
		-Dsonar.host.url=${SONAR_HOST_URL} \
		-Dsonar.login=${SONAR_TOKEN}
else
	echo "Running build, no analysis for branch '${TRAVIS_BRANCH}'"
	mvn clean verify
fi