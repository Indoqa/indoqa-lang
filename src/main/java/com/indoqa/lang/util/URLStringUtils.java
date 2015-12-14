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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Utilities to deal with URL {@link String}s or parts of it.
 */
public final class URLStringUtils {

    private static final String ALLOWED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-_.";
    private static final String[][] REPLACEMENTS = {{"\u00e4", "\u00f6", "\u00fc", "\u00df", " ", "\u00c4", "\u00d6", "\u00dc"},
            {"ae", "oe", "ue", "ss", "-", "AE", "OU", "UE"}};

    private URLStringUtils() {
        // hide utility class constructor
    }

    /**
     * Clean up a {@link String} and only allow characters that don't need to be escaped in URLs.
     * <p>
     * The implementation is null-safe. Passing null as input returns null as result.
     * <p>
     * German umlauts and ß are replaced with their two-character representations (e.g. ae).
     * <p>
     * Multiple dashes are replaced by a single one.
     *
     * @param input The string to be cleaned
     * @return A String that only contains characters that don't need to be escaped if used as part of URLs.
     */
    public static String clean(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            boolean replaced = false;

            for (int j = 0; j < REPLACEMENTS[0].length; j++) {
                if (REPLACEMENTS[0][j].toCharArray()[0] == ch) {
                    result.append(REPLACEMENTS[1][j]);
                    replaced = true;
                    break;
                }

            }

            if (!replaced && ALLOWED.indexOf(ch) >= 0) {
                result.append(ch);
            }
        }

        return cleanMultipleChar(result, '-').toString();
    }

    public static StringBuilder cleanMultipleChar(StringBuilder text, char doubleCh) {
        if (text == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        char previousChar = 0;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);

            if (previousChar == doubleCh && previousChar == currentChar) {
                previousChar = currentChar;
                continue;
            }

            previousChar = currentChar;
            result.append(currentChar);
        }

        return result;
    }

    /**
     * Calculate the "relativizer" of a path. E.g. <code>a/b/c</code> becomes <code>../../</code>. If a path doesn't start with a
     * slash, one is prepended.
     * <p>
     * The implementation is null-safe. Passing null as input returns null as result.
     *
     * @param path The path to be relativized.
     * @return The relativizer.
     */
    public static String relativizePath(final String path) {
        Validate.notNull(path, "A value for path is expected.");

        if (StringUtils.isBlank(path)) {
            return "";
        }

        String cleanedPath = path;
        if (cleanedPath.startsWith("/")) {
            cleanedPath = cleanedPath.substring(1);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.countMatches(cleanedPath, "/"); i++) {
            sb.append("../");
        }

        return sb.toString();
    }

    public static String replaceParameter(String path, String parameterName, Object parameterValue) {
        StringBuilder stringBuilder = new StringBuilder(path);
        String searchString = "{" + parameterName + "}";

        while (true) {
            int index = stringBuilder.indexOf(searchString);
            if (index == -1) {
                break;
            }

            stringBuilder.replace(index, index + searchString.length(), String.valueOf(parameterValue));
        }

        return stringBuilder.toString();
    }

    /**
     * URL encodes the given <code>input</code>.<br>
     * <br>
     * This behaves exactly like <code>URLEncoder.encode(input, "UTF-8")</code> but also deals with the dreaded
     * {@link UnsupportedEncodingException} by converting it into an {@link IllegalStateException}
     *
     * @param input The input to URL encode
     * @return The encoded result.
     *
     * @throws IllegalStateException If UTF-8 is not supported.
     */
    public static String urlEncode(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not URL encode input '" + input + "'. UTF-8 is not available!", e);
        }
    }
}
