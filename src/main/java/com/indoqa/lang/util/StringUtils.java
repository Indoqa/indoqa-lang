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

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.indoqa.lang.collection.ArrayIterable;

/**
 * Utility functions that are not available in any of the popular StringUtils classes ( {@link org.apache.commons.lang.StringUtils},
 * {@link org.springframework.util.StringUtils}).
 */
public class StringUtils {

    /**
     * 2<sup>10</sup> bytes (aka 1 Kibibyte).
     */
    public static final long ONE_KIB = (long) Math.pow(2, 10);

    /**
     * 2<sup>20</sup> bytes (aka 1 Mebibyte)
     */
    public static final long ONE_MIB = (long) Math.pow(2, 20);

    /**
     * 2<sup>30</sup> bytes (aka 1 Gibibyte)
     */
    public static final long ONE_GIB = (long) Math.pow(2, 30);

    private static final DecimalFormat GIBI_BYTE_FORMAT = new DecimalFormat("#,##0.00 GiB");
    private static final DecimalFormat MEBI_BYTE_FORMAT = new DecimalFormat("#,##0.00 MiB");
    private static final DecimalFormat KIBI_BYTE_FORMAT = new DecimalFormat("#,##0.0 KiB");
    private static final DecimalFormat BYTE_FORMAT = new DecimalFormat("#,##0 B");

    private static final Pattern CONTROL_CHARACTERS_PATTERN = Pattern.compile("[\\p{Cntrl}&&[^ \\t\\n\\r]]");

    private static final char[] CHARACTERS_TO_BE_ESCAPED_FOR_SOLR = {'"', '(', ')', ':', '[', ']', '+', '-', '*', '~', ' ', '^', '{',
        '}', '!', '/'};

    private static final char[] ILLEGAL_HTML_ID_CHARACTERS = {'"', '\'', ':', ';', ',', '+', 'ä', 'Ä', 'ö', 'Ö', 'ü', 'Ü', '&', '$'};

    private static final int INITIAL_STRING_BUILDER_CAPACITY = 200;

    static {
        Arrays.sort(CHARACTERS_TO_BE_ESCAPED_FOR_SOLR);
        Arrays.sort(ILLEGAL_HTML_ID_CHARACTERS);
    }

    /**
     * <p>
     * <code>StringUtils</code> instances should NOT be constructed in standard programming. Instead, the class should be used as
     * <code>StringUtils.countLines("foo");</code>.
     * </p>
     * 
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance to operate.
     * </p>
     */
    public StringUtils() {
        super();
    }

    public static void append(StringBuilder stringBuilder, Iterable<?> values, String separator) {
        for (Iterator<?> i = values.iterator(); i.hasNext();) {
            stringBuilder.append(String.valueOf(i.next()));

            if (i.hasNext()) {
                stringBuilder.append(separator);
            }
        }
    }

    public static void append(StringBuilder stringBuilder, Object[] values, String separator) {
        append(stringBuilder, new ArrayIterable<Object>(values), separator);
    }

    /**
     * Formats the given number of bytes to a human-readable representation up to GiB units.<br>
     * <br>
     * This method will provide higher accuracy than the same one found in commons-io, by offering 2 decimal places for GiB and MiB and
     * 1 decimal place for KiB ranges. <br>
     * <br>
     * The result will contain the appropriate binary unit (B, KiB, MiB or GiB)
     * 
     * @param bytes The number of bytes to be formatted.
     * @return The formatted result.
     */
    public static String byteCountToDisplaySize(long bytes) {
        if (bytes / ONE_GIB > 0) {
            return GIBI_BYTE_FORMAT.format((double) bytes / ONE_GIB);
        }

        if (bytes / ONE_MIB > 0) {
            return MEBI_BYTE_FORMAT.format((double) bytes / ONE_MIB);
        }

        if (bytes / ONE_KIB > 0) {
            return KIBI_BYTE_FORMAT.format((double) bytes / ONE_KIB);
        }

        return BYTE_FORMAT.format(bytes);
    }

    /**
     * Count the lines of the passed test. A line is considered to be terminated by any one of a line feed ('\n'), a carriage return
     * ('\r'), or a carriage return followed immediately by a linefeed.
     * 
     * @param content The text whose lines are to be counted.
     * @return The number of lines.
     * @throws IOException
     */
    public static int countLines(String content) {
        if (org.apache.commons.lang.StringUtils.isEmpty(content)) {
            return 0;
        }

        LineNumberReader lnr = new LineNumberReader(new StringReader(content));

        int count = 0;
        try {
            while (lnr.readLine() != null) {
                count++;
            }
        } catch (IOException ioe) {
            return -1;
        }

        return count;
    }

    public static String escapeSolr(String value) {
        StringBuilder stringBuilder = new StringBuilder(value);

        for (int i = 0; i < stringBuilder.length(); i++) {
            boolean isCharacterToBeEscaped = Arrays.binarySearch(CHARACTERS_TO_BE_ESCAPED_FOR_SOLR, stringBuilder.charAt(i)) >= 0;
            if (isCharacterToBeEscaped) {
                stringBuilder.insert(i, '\\');
                i++;
            }
        }

        return stringBuilder.toString();
    }

    public static String join(Object... values) {
        StringBuilder stringBuilder = new StringBuilder(INITIAL_STRING_BUILDER_CAPACITY);

        for (Object eachValue : values) {
            stringBuilder.append(eachValue);
        }

        return stringBuilder.toString();
    }

    public static String joinIfNotBlank(Iterable<?> values, String separator) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object eachValue : values) {
            if (eachValue == null) {
                continue;
            }

            String stringValue = eachValue.toString();
            if (org.apache.commons.lang.StringUtils.isBlank(stringValue)) {
                continue;
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.append(separator);
            }

            stringBuilder.append(stringValue);
        }

        return stringBuilder.toString();
    }

    public static String joinIfNotBlank(Object[] values, String separator) {
        return joinIfNotBlank(new ArrayIterable<Object>(values), separator);
    }

    public static String quote(String value) {
        if (value == null) {
            return null;
        }

        return join("\"", value, "\"");
    }

    /**
     * Replaces control characters defined in CONTROL_CHARACTERS_PATTERN with the replacement string.
     * 
     * @param text The text whose control characters are to be replaced.
     * @param replacement The text the control characters should be replaced with
     * @return The String with the replaced control characters or empty String if text is null or empty.
     * @throws IllegalArgumentException if the replacement string is <code>null</code>
     */
    public static String replaceControlCharactersWith(String text, String replacement) {
        if (org.apache.commons.lang.StringUtils.isBlank(text)) {
            return "";
        }

        if (replacement == null) {
            throw new IllegalArgumentException("The replacement string can't be null");
        }

        // we don't use String.replaceAll because this would compile the pattern every time
        Matcher matcher = CONTROL_CHARACTERS_PATTERN.matcher(text);
        return matcher.replaceAll(replacement);
    }

    public static String sanitzeHtmlId(String id) {
        if (org.apache.commons.lang.StringUtils.isBlank(id)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder(id);

        for (int i = 0; i < stringBuilder.length(); i++) {
            char character = stringBuilder.charAt(i);

            if (Arrays.binarySearch(ILLEGAL_HTML_ID_CHARACTERS, character) >= 0) {
                stringBuilder.setCharAt(i, '_');
            }
        }

        return stringBuilder.toString();
    }

    public static String unquote(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }

        if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }
}
