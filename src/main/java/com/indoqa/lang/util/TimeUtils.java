/*
 * Licensed to the Indoqa Software Design und Beratung GmbH (Indoqa) under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Indoqa licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indoqa.lang.util;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY;
import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR;
import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE;
import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {

    private static final int[] CALENDAR_TIME_FIELDS = new int[] {HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND};

    private static final String SOLR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");

    private static final String[] UNITS = {"d", "h", "m", "s", "ms"};
    private static final long[] FACTORS = {MILLIS_PER_DAY, MILLIS_PER_HOUR, MILLIS_PER_MINUTE, MILLIS_PER_SECOND, 1};

    private TimeUtils() {
        // hide utility class constructor
    }

    /**
     * Converts a relative date offset described as milliseconds in a string representation like '1y 3w 2d 4h' using the
     * {@link DateRangeParser}
     *
     * @param offset The offset in milliseconds
     * @return A human readaby string representation
     */
    public static String convertRelativeLongOffsetToString(long offset) {
        return DateRangeParser.getStringRepresentationOfOffset(offset);
    }

    /**
     * Converts a string representation like '1y 3w 2d 4h' into a long offset, using the {@link DateRangeParser}.
     *
     * @param textualRepresentation A string representation of a relative time offset, see {@link DateRangeParser} for details.
     * @return The relative long offset for the string representation.
     * @throws ParseException If textualRepresentation is either empty or null.
     */
    public static long convertRelativeStringOffsetToMilliseconds(String textualRepresentation) throws ParseException {
        return DateRangeParser.getOffsetInMilliseconds(textualRepresentation);
    }

    /**
     * Format a {@link Date} to contain both date and time information with full millisecond precision. The time zone used is always
     * UTC.
     *
     * The result returned by this method is fully compatible with {@link #parseDate(String)}.
     *
     * @param date The date to be formatted.
     * @return The formatted date.
     */
    public static String formatDate(Date date) {
        return createDateFormat().format(date);
    }

    /**
     * Convenience method for {@link #formatDuration(long)}
     *
     * @param start Start date for the duration
     * @param end End date for the duration
     *
     * @return A human readable representation of the duration
     * @throws IllegalArgumentException If either start or end date is null
     */
    public static String formatDuration(Date start, Date end) {
        if (start == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }

        if (end == null) {
            throw new IllegalArgumentException("End date must not be null.");
        }

        return formatDuration(end.getTime() - start.getTime());
    }

    /**
     * Formats a duration in milliseconds to contain the following information days (d), hours (h), minutes (m), seconds (s),
     * milliseconds (ms). Only the biggest unit (following the next unit if it's not empty) will be printed.
     *
     * @param durationInMillis Duration in milliseconds to be formatted
     * @return A human readable representation of the duration
     */
    public static String formatDuration(long durationInMillis) {
        StringBuilder resultBuilder = new StringBuilder();

        long remainingMilliseconds = durationInMillis;
        int maxUnit = UNITS.length;
        for (int i = 0; i < maxUnit; i++) {
            long factor = FACTORS[i];

            if (remainingMilliseconds < factor) {
                continue;
            }

            if (resultBuilder.length() > 0) {
                resultBuilder.append(' ');
            }

            resultBuilder.append(remainingMilliseconds / factor);
            resultBuilder.append(UNITS[i]);

            maxUnit = Math.min(maxUnit, i + 2);
            remainingMilliseconds %= factor;
        }

        return resultBuilder.toString();
    }

    /**
     * Formats the given amount of time to represent seconds and milliseconds.<br>
     * <br>
     * The result's format will be <code>#,##0.000</code>.
     *
     * @param amount The amount of time expressed in <code>timeUnit</code>.
     * @param timeUnit The unit of <code>amount</code>
     *
     * @return The formatted result.
     *
     * @see DecimalFormat
     * @see TimeUnit
     */
    public static String formatSecondsAndMilliseconds(long amount, TimeUnit timeUnit) {
        double milliseconds = timeUnit.toMillis(amount);
        double seconds = milliseconds / MILLIS_PER_SECOND;
        return new DecimalFormat("#,##0.000").format(seconds);
    }

    public static String formatSolrDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(SOLR_DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        return dateFormat.format(date);
    }

    public static Date getEndOfDay(Date date) {
        return getEndOfDay(date, TimeZone.getDefault());
    }

    public static Date getEndOfDay(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);

        calendar.setTime(date);
        for (int eachField : CALENDAR_TIME_FIELDS) {
            calendar.set(eachField, calendar.getMaximum(eachField));
        }

        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        return getStartOfDay(date, TimeZone.getDefault());
    }

    public static Date getStartOfDay(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);

        calendar.setTime(date);
        for (int eachField : CALENDAR_TIME_FIELDS) {
            calendar.set(eachField, calendar.getMinimum(eachField));
        }

        return calendar.getTime();
    }

    /**
     * Parse a date expression with both date and time information with full millisecond precision and create a {@link Date}. The
     * assumed time zone is always UTC.
     *
     * This method accepts date expressions generated with {@link #formatDate(Date)}.
     *
     * @param value the date expression to be parsed.
     * @return The parsed date.
     */
    public static Date parseDate(String value) {
        try {
            return createDateFormat().parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse date expression '" + value + "'", e);
        }
    }

    public static Date parseSolrDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(SOLR_DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        return dateFormat.parse(date);
    }

    private static DateFormat createDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        return dateFormat;
    }
}
