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

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.lang3.time.DateUtils.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.junit.Test;

import junit.framework.Assert;

public class TimeUtilsTest {

    @Test
    public void formatDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.set(2012, 2, 1, 10, 5, 30);
        calendar.set(Calendar.MILLISECOND, 501);

        assertEquals("2012-03-01 09:05:30.501", TimeUtils.formatDate(calendar.getTime()));
    }

    @Test
    public void formatDuration() {
        assertEquals("24d", TimeUtils.formatDuration(MILLIS_PER_DAY * 24));
        assertEquals("23d 1h", TimeUtils.formatDuration(MILLIS_PER_DAY * 23 + MILLIS_PER_HOUR));
        assertEquals("23d 1h", TimeUtils.formatDuration(MILLIS_PER_DAY * 23 + MILLIS_PER_HOUR + MILLIS_PER_MINUTE));
        assertEquals("23d", TimeUtils.formatDuration(MILLIS_PER_DAY * 23 + MILLIS_PER_MINUTE));
        assertEquals("23d", TimeUtils.formatDuration(MILLIS_PER_DAY * 23 + MILLIS_PER_SECOND));
        assertEquals("23d", TimeUtils.formatDuration(MILLIS_PER_DAY * 23 + 100));

        assertEquals("23h", TimeUtils.formatDuration(MILLIS_PER_HOUR * 23));
        assertEquals("23h 1m", TimeUtils.formatDuration(MILLIS_PER_HOUR * 23 + MILLIS_PER_MINUTE));
        assertEquals("23h 1m", TimeUtils.formatDuration(MILLIS_PER_HOUR * 23 + MILLIS_PER_MINUTE + MILLIS_PER_SECOND));
        assertEquals("23h", TimeUtils.formatDuration(MILLIS_PER_HOUR * 23 + MILLIS_PER_SECOND));
        assertEquals("23h", TimeUtils.formatDuration(MILLIS_PER_HOUR * 23 + 100));

        assertEquals("23m", TimeUtils.formatDuration(MILLIS_PER_MINUTE * 23));
        assertEquals("23m 1s", TimeUtils.formatDuration(MILLIS_PER_MINUTE * 23 + MILLIS_PER_SECOND));
        assertEquals("23m 1s", TimeUtils.formatDuration(MILLIS_PER_MINUTE * 23 + MILLIS_PER_SECOND + 100));
        assertEquals("23m", TimeUtils.formatDuration(MILLIS_PER_MINUTE * 23 + 100));

        assertEquals("23s", TimeUtils.formatDuration(MILLIS_PER_SECOND * 23));
        assertEquals("23s 100ms", TimeUtils.formatDuration(MILLIS_PER_SECOND * 23 + 100));
    }

    @Test
    public void getEndOfDay() {
        final DateFormat timeInstance = new SimpleDateFormat("HH:mm:ss.SSS");
        final Random random = new Random();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 10; i++) {
            calendar.add(Calendar.MILLISECOND, random.nextInt(1000000));
            assertEquals("Failed to calculate end of day for date with long value " + calendar.getTimeInMillis(), "23:59:59.999",
                timeInstance.format(TimeUtils.getEndOfDay(calendar.getTime())));
        }

        timeInstance.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals("21:59:59.999", timeInstance.format(TimeUtils.getEndOfDay(new Date(), TimeZone.getTimeZone("GMT+2"))));
        assertEquals("00:59:59.999", timeInstance.format(TimeUtils.getEndOfDay(new Date(), TimeZone.getTimeZone("GMT-1"))));
    }

    @Test
    public void getStartOfDay() {
        final DateFormat timeInstance = new SimpleDateFormat("HH:mm:ss.SSS");
        final Random random = new Random();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 10; i++) {
            calendar.add(Calendar.MILLISECOND, random.nextInt(1000000));
            assertEquals("Failed to calculate start of day for date with long value " + calendar.getTimeInMillis(), "00:00:00.000",
                timeInstance.format(TimeUtils.getStartOfDay(calendar.getTime())));
        }

        timeInstance.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertEquals("22:00:00.000", timeInstance.format(TimeUtils.getStartOfDay(new Date(), TimeZone.getTimeZone("GMT+2"))));
        assertEquals("01:00:00.000", timeInstance.format(TimeUtils.getStartOfDay(new Date(), TimeZone.getTimeZone("GMT-1"))));
    }

    @Test
    public void parseDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        calendar.set(2012, 2, 1, 10, 5, 30);
        calendar.set(Calendar.MILLISECOND, 501);

        assertEquals(calendar.getTimeInMillis(), TimeUtils.parseDate("2012-03-01 09:05:30.501").getTime());
    }

    @Test
    public void testOffsetConversionMillisecondsToString() {
        long offset = DateRangeParser.MILLISECONDS_PER_YEAR + DateRangeParser.MILLISECONDS_PER_WEEK * 2
            + DateRangeParser.MILLISECONDS_PER_DAY * 2 + DateRangeParser.MILLISECONDS_PER_HOUR * 3
            + DateRangeParser.MILLISECONDS_PER_MINUTE * 3;

        String result = TimeUtils.convertRelativeLongOffsetToString(offset);
        Assert.assertEquals("1y 2w 2d 3h 3m", result);
    }

    @Test
    public void testOffsetConversionStringToMilliseconds() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1y 2w 17d 15h 11m");

        long expectedResult = DateRangeParser.MILLISECONDS_PER_YEAR * 1 + DateRangeParser.MILLISECONDS_PER_WEEK * 2
            + DateRangeParser.MILLISECONDS_PER_DAY * 17 + DateRangeParser.MILLISECONDS_PER_HOUR * 15
            + DateRangeParser.MILLISECONDS_PER_MINUTE * 11;
        Assert.assertEquals(expectedResult, offsetInMilliseconds);
    }
}
