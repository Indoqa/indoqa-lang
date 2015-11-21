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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/**
 * Parser that converts a string representation into a long offset and vice versa. The string may contain multiple parts (separated by
 * a blank), every part contains of an integer offset value and a time unit.<br/>
 * <br/>
 * Supported units:
 * <ul>
 * <li>1y -> one year (365 days)</li>
 * <li>1w -> one week</li>
 * <li>1d -> one day</li>
 * <li>1h -> one hour</li>
 * <li>1m -> one month</li>
 * <li>1s -> one second</li>
 * <li>1ms -> one millisecond</li>
 * </ul>
 * <br/>
 * Examples:
 * <ul>
 * <li>"2y 3w" -> 2 years and 3 weeks</li>
 * <li>"4w 3d 5h 2m" -> 4 weeks, 3 days, 5 hours and 2 minutes</li>
 * </ul>
 */
public class DateRangeParser {

    public static final long ONE_MILLISECOND = 1;
    public static final long MILLISECONDS_PER_SECOND = ONE_MILLISECOND * 1000;
    public static final long MILLISECONDS_PER_MINUTE = MILLISECONDS_PER_SECOND * 60;
    public static final long MILLISECONDS_PER_HOUR = MILLISECONDS_PER_MINUTE * 60;
    public static final long MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;
    public static final long MILLISECONDS_PER_WEEK = MILLISECONDS_PER_DAY * 7;
    public static final long MILLISECONDS_PER_YEAR = MILLISECONDS_PER_DAY * 365;

    private static final Map<String, Long> UNIT_CONVERSIONS = buildUnitsMap();

    public static long getOffsetInMilliseconds(String relativeString) throws ParseException {
        if (StringUtils.isBlank(relativeString)) {
            throw new ParseException("Relative date string is emtpy or null!", 0);
        }

        long result = 0;

        String[] parts = relativeString.split(" ");
        int pos = 0;

        for (String part : parts) {
            String unit = extractUnit(relativeString, pos, part);
            int partOffset = extractPartOffset(relativeString, pos, part, unit);

            result += partOffset * UNIT_CONVERSIONS.get(unit);
            pos += part.length() + 1;
        }

        return result;
    }

    public static String getStringRepresentationOfOffset(long offset) {
        if (offset == 0) {
            return "";
        }

        long currentOffset = offset;
        StringBuffer result = new StringBuffer();

        List<Entry<String, Long>> conversionUnitsSortedDesc = getConversionUnitsSortedDesc();
        for (Entry<String, Long> entry : conversionUnitsSortedDesc) {
            String unitLabel = entry.getKey();
            Long unitConversion = entry.getValue();

            if (currentOffset < unitConversion) {
                continue;
            }

            long unitValue = currentOffset / unitConversion;
            result.append(unitValue);
            result.append(unitLabel);
            result.append(" ");

            if (currentOffset % unitConversion == 0) {
                break;
            }

            currentOffset = currentOffset % unitConversion;
        }

        return StringUtils.trim(result.toString());
    }

    private static Map<String, Long> buildUnitsMap() {
        Map<String, Long> units = new HashMap<String, Long>();

        units.put("y", MILLISECONDS_PER_YEAR);
        units.put("w", MILLISECONDS_PER_WEEK);
        units.put("d", MILLISECONDS_PER_DAY);
        units.put("h", MILLISECONDS_PER_HOUR);
        units.put("m", MILLISECONDS_PER_MINUTE);
        units.put("s", MILLISECONDS_PER_SECOND);
        units.put("ms", ONE_MILLISECOND);

        return units;
    }

    private static int extractPartOffset(String relativeString, int pos, String part, String unit) throws ParseException {
        int partOffset;
        try {
            String integerDeltaPart = part.substring(0, part.indexOf(unit));
            partOffset = Integer.parseInt(integerDeltaPart);
        } catch (NumberFormatException e) {
            throw new ParseException(MessageFormat.format(
                "No numeric offset value in relative date part >{0}< of input >{1}< at pos {2}", part, relativeString, pos), pos);
        }

        if (partOffset < 0) {
            throw new ParseException(
                MessageFormat.format("Numeric offset value needs to be positive at relative date part >{0}< of input >{1}< at pos {2}",
                    part, relativeString, pos),
                pos);
        }

        return partOffset;
    }

    private static String extractUnit(String relativeString, int pos, String part) throws ParseException {
        Set<String> keySet = UNIT_CONVERSIONS.keySet();
        for (String key : keySet) {
            if (part.endsWith(key)) {
                return key;
            }
        }

        throw new ParseException(
            MessageFormat.format("No unit found in relative date part >{0}< of input >{1}< at pos {2}", part, relativeString, pos),
            pos);
    }

    private static List<Entry<String, Long>> getConversionUnitsSortedDesc() {
        List<Entry<String, Long>> sortedConversionUnits = new ArrayList<Entry<String, Long>>(UNIT_CONVERSIONS.entrySet());
        Collections.sort(sortedConversionUnits, (o1, o2) -> o1.getValue().compareTo(o2.getValue()) * -1);

        return sortedConversionUnits;
    }
}
