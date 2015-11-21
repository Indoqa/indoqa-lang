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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils {

    private ReflectionUtils() {
        // hide utility class constructor
    }

    /**
     * Returns all declared fields of the given <code>type</code>. <br/>
     * This method differs from {@link Class#getDeclaredFields()} in that it also returns all declared fields that are inherited.
     * 
     * @param type The class to retrieve the declared fields for.
     * @return The declared fields of <code>type</code> and all its superclasses.
     * 
     * @see Class#getDeclaredFields()
     */
    public static List<Field> getAllDeclaredFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> currentType = type;

        while (currentType != null) {
            Field[] declaredFields = currentType.getDeclaredFields();
            for (Field eachDeclaredField : declaredFields) {
                result.add(eachDeclaredField);
            }

            currentType = currentType.getSuperclass();
        }

        return result;
    }
}
