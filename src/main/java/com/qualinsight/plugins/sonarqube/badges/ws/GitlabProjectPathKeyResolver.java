package com.qualinsight.plugins.sonarqube.badges.ws;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Steven on 2019-01-11.
 */
public class GitlabProjectPathKeyResolver {


    public static String getGitlabProjectNameByPath(String projectPathAsKey) {
        if (StringUtils.isEmpty(projectPathAsKey)) {
            return projectPathAsKey;
        }

        if (StringUtils.containsAny(projectPathAsKey, "/")) {
            String[] splits = StringUtils.split(projectPathAsKey, "/");
            if (splits != null) {
                return splits[splits.length - 1];
            }
        }

        return projectPathAsKey;
    }
}
