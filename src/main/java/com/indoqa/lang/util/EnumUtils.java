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

import static java.util.Collections.emptyList;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public final class EnumUtils {

    private EnumUtils() {
        // hide utility class constructor
    }

    public static <K, E extends Enum<E>> Function<K, E> lookupEnum(Class<E> enumClass, Function<E, K> mapper) {
        @SuppressWarnings("unchecked")
        E[] emptyArray = (E[]) Array.newInstance(enumClass, 0);
        return createLookupFunction(EnumSet.allOf(enumClass).toArray(emptyArray), mapper)
            .andThen(e -> e.isEmpty() ? null : e.iterator().next());
    }

    public static <K, E extends Enum<E>> Function<K, Collection<E>> lookupEnums(Class<E> enumClass, Function<E, K> mapper) {
        @SuppressWarnings("unchecked")
        E[] emptyArray = (E[]) Array.newInstance(enumClass, 0);
        return createLookupFunction(EnumSet.allOf(enumClass).toArray(emptyArray), mapper);
    }

    private static <K, E extends Enum<E>> Function<K, Collection<E>> createLookupFunction(E[] enumsValues, Function<E, K> mapper) {
        Map<K, Collection<E>> propertyIndex = new HashMap<>(enumsValues.length);

        for (E eachEnumValue : enumsValues) {
            K mapKey = mapper.apply(eachEnumValue);
            Collection<E> mapKeys = propertyIndex.get(mapKey);
            if (mapKeys == null) {
                mapKeys = new ArrayList<>();
            }
            mapKeys.add(eachEnumValue);
            propertyIndex.put(mapKey, mapKeys);
        }

        return (K key) -> {
            Collection<E> results = propertyIndex.get(key);
            return results != null ? results : emptyList();
        };
    }
}
