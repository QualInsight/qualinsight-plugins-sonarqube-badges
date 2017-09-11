package com.qualinsight.plugins.sonarqube.badges.ws.measure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Label formatter for Measure badges with added support for Period Index label formatting
 *
 * Possible values for period are:
 * Number of days before analysis, for example 5.
 * A custom date. Format is yyyy-MM-dd, for example 2010-12-25
 * 'previous_analysis' to compare to previous analysis
 * 'previous_version' to compare to the previous version in the project history
 * A version, for example '1.2' or 'BASELINE'
 *
 */
public class MeasureBagdeMetricNameFormatter {

    private static final String PERIOD_DATE_FORMAT = "yyyy-MM-dd";

    private static final String PRINTED_DATE_FORMAT = "dd MMM yyyy";

    private static final String SINCE = " since ";

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasureHolder.class);

    private static Date getDate(String input) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PERIOD_DATE_FORMAT);
            date = sdf.parse(input);
            if (!input.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            LOGGER.debug("Period {} is not a date", input);
        }
        return date;
    }

    private static boolean isDate(String input) {
        return getDate(input) != null;
    }

    private static boolean isDays(String input) {
        try {
            return Integer.parseInt(input) >= 0;
        } catch (NumberFormatException e) {
            LOGGER.debug("Period {} is not a number", input);
        }
        return false;
    }

    private static boolean isPrevious(String input) {
        return input.equalsIgnoreCase("previous_analysis") || input.equalsIgnoreCase("previous_version");
    }

    private MeasureBagdeMetricNameFormatter () {
        throw new IllegalStateException("MeasureBagdeMetricNameFormatter should not be instantiated");
    }

    public static String getMetricNameWithPeriod(String metricName, String period) {
        String formattedMetric = metricName.replace(" (%)", "").toLowerCase();
        if (period != null && period.length() > 0) {
            if (isPrevious(period)) {
                formattedMetric = formattedMetric + SINCE + period.replace("_", " ").toLowerCase();
            } else if (isDays(period)) {
                formattedMetric = formattedMetric + SINCE + period + " days";
            } else if (isDate(period)) {
                SimpleDateFormat sdf = new SimpleDateFormat(PRINTED_DATE_FORMAT);
                formattedMetric = formattedMetric + SINCE + sdf.format(getDate(period));
            } else {
                formattedMetric = formattedMetric + SINCE + period;
            }
        }
        return formattedMetric;
    }

}
