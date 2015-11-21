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

import org.junit.Test;

import com.indoqa.lang.util.NumberUtils;

public class NumberUtilsTest {

    @Test
    public void test() {
        assertEquals("I", NumberUtils.toRomanNumeral(1));
        assertEquals("II", NumberUtils.toRomanNumeral(2));
        assertEquals("III", NumberUtils.toRomanNumeral(3));
        assertEquals("IV", NumberUtils.toRomanNumeral(4));
        assertEquals("V", NumberUtils.toRomanNumeral(5));

        assertEquals("IX", NumberUtils.toRomanNumeral(9));
        assertEquals("X", NumberUtils.toRomanNumeral(10));
        assertEquals("XI", NumberUtils.toRomanNumeral(11));

        assertEquals("XIX", NumberUtils.toRomanNumeral(19));
        assertEquals("XX", NumberUtils.toRomanNumeral(20));
        assertEquals("XXI", NumberUtils.toRomanNumeral(21));

        assertEquals("XXIX", NumberUtils.toRomanNumeral(29));
        assertEquals("XXX", NumberUtils.toRomanNumeral(30));
        assertEquals("XXXI", NumberUtils.toRomanNumeral(31));

        assertEquals("XLIX", NumberUtils.toRomanNumeral(49));
        assertEquals("L", NumberUtils.toRomanNumeral(50));
        assertEquals("LI", NumberUtils.toRomanNumeral(51));

        assertEquals("XCIX", NumberUtils.toRomanNumeral(99));
        assertEquals("C", NumberUtils.toRomanNumeral(100));
        assertEquals("CI", NumberUtils.toRomanNumeral(101));

        assertEquals("CMXCIX", NumberUtils.toRomanNumeral(999));
        assertEquals("M", NumberUtils.toRomanNumeral(1000));
        assertEquals("MI", NumberUtils.toRomanNumeral(1001));

        assertEquals("MCMXCIX", NumberUtils.toRomanNumeral(1999));
        assertEquals("MM", NumberUtils.toRomanNumeral(2000));
        assertEquals("MMI", NumberUtils.toRomanNumeral(2001));

        assertEquals("MMII", NumberUtils.toRomanNumeral(2002));
        assertEquals("MMXXII", NumberUtils.toRomanNumeral(2022));
        assertEquals("MMCCXXII", NumberUtils.toRomanNumeral(2222));
    }
}
