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

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public final class NumberUtils {

    private static final Map<Integer, String> ROMAN_VALUES = new TreeMap<Integer, String>(Collections.reverseOrder());

    static {
        ROMAN_VALUES.put(1000, "M");
        ROMAN_VALUES.put(900, "CM");
        ROMAN_VALUES.put(500, "D");
        ROMAN_VALUES.put(400, "CD");
        ROMAN_VALUES.put(100, "C");
        ROMAN_VALUES.put(90, "XC");
        ROMAN_VALUES.put(50, "L");
        ROMAN_VALUES.put(40, "XL");
        ROMAN_VALUES.put(10, "X");
        ROMAN_VALUES.put(9, "IX");
        ROMAN_VALUES.put(5, "V");
        ROMAN_VALUES.put(4, "IV");
        ROMAN_VALUES.put(1, "I");
    }

    private NumberUtils() {
        // hidden utility class constructor
    }

    public static int clamp(int min, int value, int max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }

    public static long clamp(long min, long value, long max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }

    public static String toRomanNumeral(int input) {
        if (input < 1 || input > 3999) {
            return String.valueOf(input);
        }

        int remainingValue = input;

        StringBuilder stringBuilder = new StringBuilder();

        for (Entry<Integer, String> eachEntry : ROMAN_VALUES.entrySet()) {
            while (remainingValue >= eachEntry.getKey()) {
                remainingValue -= eachEntry.getKey();
                stringBuilder.append(eachEntry.getValue());
            }

            if (remainingValue == 0) {
                break;
            }
        }

        return stringBuilder.toString();
    }
}
