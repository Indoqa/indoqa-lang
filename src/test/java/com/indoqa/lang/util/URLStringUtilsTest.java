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

import static junit.framework.Assert.*;

import org.junit.Test;

import com.indoqa.lang.util.URLStringUtils;

public class URLStringUtilsTest {

    // clean tests
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    public void clean01() {
        assertEquals("aaebc1_", URLStringUtils.clean("a\u00e4bc1_"));
    }

    @Test
    public void clean02() {
        assertEquals("aaebc", URLStringUtils.clean("a\u00e4bc"));
    }

    @Test
    public void clean03() {
        assertNull(URLStringUtils.clean(null));
    }

    @Test
    public void clean04() {
        assertEquals("", URLStringUtils.clean(""));
    }

    @Test
    public void clean05() {
        assertEquals("0123456789", URLStringUtils.clean("0123456789"));
    }

    @Test
    public void clean06() {
        assertEquals("aeoeue-", URLStringUtils.clean("\u00e4\u00f6\u00fc "));
    }

    @Test
    public void clean07() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", URLStringUtils.clean("abcdefghijklmnopqrstuvwxyz"));
    }

    @Test
    public void clean08() {
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ_-.", URLStringUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ_-."));
    }

    @Test
    public void clean09() {
        assertEquals("AZ", URLStringUtils.clean("A!\"\u00a7$%&/()=*+~#',;:<>|Z"));
    }

    @Test
    public void clean10() {
        assertEquals("AZ-1", URLStringUtils.clean("AZ---(1)"));
    }

    // clean double characters
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    public void cleanDouble1() {
        assertEquals(new StringBuilder("-ab").toString(),
            URLStringUtils.cleanMultipleChar(new StringBuilder("---ab"), '-').toString());
    }

    @Test
    public void cleanDouble2() {
        assertEquals(new StringBuilder("-ab-bc-de").toString(),
            URLStringUtils.cleanMultipleChar(new StringBuilder("-ab--bc---de"), '-').toString());
    }

    @Test
    public void cleanDouble3() {
        assertEquals(new StringBuilder("").toString(), URLStringUtils.cleanMultipleChar(new StringBuilder(""), '-').toString());
    }

    @Test
    public void cleanDouble4() {
        assertNull(URLStringUtils.cleanMultipleChar(null, '-'));
    }

    // relativize tests
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    public void relativizerWorking1() {
        assertEquals("../../", URLStringUtils.relativizePath("a/b/c"));
    }

    @Test
    public void relativizerWorking2() {
        assertEquals("../../", URLStringUtils.relativizePath("/a/b/c"));
    }

    @Test
    public void relativizerWorking3() {
        assertEquals("", URLStringUtils.relativizePath("a"));
    }

    @Test
    public void relativizerWorking4() {
        assertEquals("", URLStringUtils.relativizePath(""));
    }
}
