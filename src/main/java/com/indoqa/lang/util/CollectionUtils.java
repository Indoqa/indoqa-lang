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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class CollectionUtils {

    private CollectionUtils() {
        // hide utility constructor
    }

    @SafeVarargs
    public static <T> Set<T> asSet(T... values) {
        Set<T> result = new HashSet<>();

        for (T eachValue : values) {
            result.add(eachValue);
        }

        return result;
    }

    /**
     * Returns up to <code>maxCount</code> elements from the beginning of <code>items</code>.<br/>
     * If <code>items</code> contains less than or exactly <code>maxCount</code> elements this method returns <code>items</code>.<br/>
     * <br/>
     * This returned list will NOT be independent from <code>items</code>! See {@link List#subList(int, int)}.
     *
     * @param items The list to retrieve up to <code>maxCount</code> elements from.
     * @param maxCount The maximum number of elements to retrieve.
     * @return The list of elements.
     */
    public static <T> List<T> getHead(List<T> items, int maxCount) {
        if (items.size() <= maxCount) {
            return items;
        }

        return items.subList(0, Math.min(items.size(), maxCount));
    }

    public static <K, V> Map<K, V> map(K key, V value) {
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<K, V>();
        map.put(key, value);
        return map;
    }

    public static <T extends Comparable<? super T>> T max(T... values) {
        return getBest(new MaxComparison<T>(), values);
    }

    public static <T extends Comparable<? super T>> T min(T... values) {
        return getBest(new MinComparison<T>(), values);
    }

    private static <T extends Comparable<? super T>> T getBest(Comparison<T> comparison, T... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("No values provided!");
        }

        T result = null;

        for (T value : values) {
            if (value == null) {
                continue;
            }

            if (comparison.isBetter(result, value)) {
                result = value;
            }
        }

        return result;
    }

    protected interface Comparison<T extends Comparable<? super T>> {

        boolean isBetter(T current, T candidate);

    }

    protected static final class MaxComparison<T extends Comparable<? super T>> implements Comparison<T> {

        @Override
        public boolean isBetter(T current, T candidate) {
            return current == null || current.compareTo(candidate) < 0;
        }
    }

    protected static final class MinComparison<T extends Comparable<? super T>> implements Comparison<T> {

        @Override
        public boolean isBetter(T current, T candidate) {
            return current == null || current.compareTo(candidate) > 0;
        }
    }
}
