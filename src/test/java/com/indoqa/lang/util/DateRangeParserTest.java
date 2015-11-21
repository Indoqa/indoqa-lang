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

import java.text.ParseException;

import org.junit.Test;

import junit.framework.Assert;

public class DateRangeParserTest {

    @Test
    public void testInputCompoundAll() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1y 2w 17d 15h 11m");

        long expectedResult = DateRangeParser.MILLISECONDS_PER_YEAR * 1 + DateRangeParser.MILLISECONDS_PER_WEEK * 2
            + DateRangeParser.MILLISECONDS_PER_DAY * 17 + DateRangeParser.MILLISECONDS_PER_HOUR * 15
            + DateRangeParser.MILLISECONDS_PER_MINUTE * 11;
        Assert.assertEquals(expectedResult, offsetInMilliseconds);
    }

    @Test
    public void testInputDay() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1d");
        Assert.assertEquals(DateRangeParser.MILLISECONDS_PER_DAY, offsetInMilliseconds);
    }

    @Test
    public void testInputEmptyInput() {
        try {
            DateRangeParser.getOffsetInMilliseconds("");
            Assert.fail("Emtpy string doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputHour() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1h");
        Assert.assertEquals(DateRangeParser.MILLISECONDS_PER_HOUR, offsetInMilliseconds);
    }

    @Test
    public void testInputInvalidInteger1() {
        try {
            DateRangeParser.getOffsetInMilliseconds("1dy");
            Assert.fail("Invalid integer doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputInvalidInteger2() {
        try {
            DateRangeParser.getOffsetInMilliseconds("x22xsadasy");
            Assert.fail("Invalid integer doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputInvalidInteger3() {
        try {
            DateRangeParser.getOffsetInMilliseconds("-1y");
            Assert.fail("Invalid integer doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputInvalidInteger4() {
        try {
            DateRangeParser.getOffsetInMilliseconds("1y 2w 44g4m 2d");
            Assert.fail("Invalid integer doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputMinute() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1m");
        Assert.assertEquals(DateRangeParser.MILLISECONDS_PER_MINUTE, offsetInMilliseconds);
    }

    @Test
    public void testInputNullInput() {
        try {
            DateRangeParser.getOffsetInMilliseconds(null);
            Assert.fail("Null doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputUnknownUnit1() {
        try {
            DateRangeParser.getOffsetInMilliseconds("1x");
            Assert.fail("Unknown unit doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputUnknownUnit2() {
        try {
            DateRangeParser.getOffsetInMilliseconds("1y 1m 1x");
            Assert.fail("Unknown unit doesn't create a ParseException!");
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testInputWeek() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1w");
        Assert.assertEquals(DateRangeParser.MILLISECONDS_PER_WEEK, offsetInMilliseconds);
    }

    @Test
    public void testInputYear() throws ParseException {
        long offsetInMilliseconds = DateRangeParser.getOffsetInMilliseconds("1y");
        Assert.assertEquals(DateRangeParser.MILLISECONDS_PER_YEAR, offsetInMilliseconds);
    }

    @Test
    public void testOutputCompound1() {
        long offset = DateRangeParser.MILLISECONDS_PER_YEAR + DateRangeParser.MILLISECONDS_PER_WEEK * 2
            + DateRangeParser.MILLISECONDS_PER_DAY * 2 + DateRangeParser.MILLISECONDS_PER_HOUR * 3
            + DateRangeParser.MILLISECONDS_PER_MINUTE * 3;
        String result = DateRangeParser.getStringRepresentationOfOffset(offset);
        Assert.assertEquals("1y 2w 2d 3h 3m", result);
    }

    @Test
    public void testOutputCompound2() {
        long offset = DateRangeParser.MILLISECONDS_PER_YEAR + DateRangeParser.MILLISECONDS_PER_WEEK * 2
            + DateRangeParser.MILLISECONDS_PER_DAY * 2 + DateRangeParser.MILLISECONDS_PER_HOUR * 3
            + DateRangeParser.MILLISECONDS_PER_MINUTE * 3 + 6325;
        String result = DateRangeParser.getStringRepresentationOfOffset(offset);
        Assert.assertEquals("1y 2w 2d 3h 3m 6s 325ms", result);
    }

    @Test
    public void testOutputDay() {
        String result = DateRangeParser.getStringRepresentationOfOffset(DateRangeParser.MILLISECONDS_PER_DAY * 5);
        Assert.assertEquals("5d", result);
    }

    @Test
    public void testOutputHour() {
        String result = DateRangeParser.getStringRepresentationOfOffset(DateRangeParser.MILLISECONDS_PER_HOUR * 2);
        Assert.assertEquals("2h", result);
    }

    @Test
    public void testOutputMinute() {
        String result = DateRangeParser.getStringRepresentationOfOffset(DateRangeParser.MILLISECONDS_PER_MINUTE * 3);
        Assert.assertEquals("3m", result);
    }

    @Test
    public void testOutputWeek() {
        String result = DateRangeParser.getStringRepresentationOfOffset(DateRangeParser.MILLISECONDS_PER_WEEK * 3);
        Assert.assertEquals("3w", result);
    }

    @Test
    public void testOutputYear() {
        String result = DateRangeParser.getStringRepresentationOfOffset(DateRangeParser.MILLISECONDS_PER_YEAR);
        Assert.assertEquals("1y", result);
    }

    @Test
    public void testOutputYear2() {
        String result = DateRangeParser.getStringRepresentationOfOffset(DateRangeParser.MILLISECONDS_PER_YEAR * 4);
        Assert.assertEquals("4y", result);
    }

}
