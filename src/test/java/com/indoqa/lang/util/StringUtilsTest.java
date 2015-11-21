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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.indoqa.lang.util.StringUtils;

public class StringUtilsTest {

    @Test
    public void appendArray() {
        StringBuilder stringBuilder = new StringBuilder("a, ");

        String[] values = {"b", "c", "d"};
        StringUtils.append(stringBuilder, values, ", ");

        assertEquals("a, b, c, d", stringBuilder.toString());
    }

    @Test
    public void appendIterable() {
        StringBuilder stringBuilder = new StringBuilder("a, ");

        List<String> values = Arrays.asList("b", "c", "d");
        StringUtils.append(stringBuilder, values, ", ");

        assertEquals("a, b, c, d", stringBuilder.toString());
    }

    @Test
    public void countLinesContent() {
        assertEquals(3, StringUtils.countLines("a\nb\nc"));
    }

    @Test
    public void countLinesPassingNullContent() {
        assertEquals(0, StringUtils.countLines(null));
    }

    @Test
    public void escapeSolr() {
        assertEquals("test", StringUtils.escapeSolr("test"));
        assertEquals("test\\ test", StringUtils.escapeSolr("test test"));
        assertEquals("\\+test\\-test", StringUtils.escapeSolr("+test-test"));
        assertEquals("test\\^\\ test", StringUtils.escapeSolr("test^ test"));
        assertEquals("test\\(test\\)\\{0\\}\\[1\\]", StringUtils.escapeSolr("test(test){0}[1]"));
        assertEquals("test\\\"abc\\~1\\\"", StringUtils.escapeSolr("test\"abc~1\""));
        assertEquals("test\\!", StringUtils.escapeSolr("test!"));
        assertEquals("deloitte_enron_file__D__Data_enron\\-mail\\-archive_arnold\\-j_deleted_items_485_null",
            StringUtils.escapeSolr("deloitte_enron_file__D__Data_enron-mail-archive_arnold-j_deleted_items_485_null"));
    }
}
