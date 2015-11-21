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

import static com.indoqa.lang.util.ReflectionUtils.getAllDeclaredFields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedFieldFinder {

    private final Map<String, List<Field>> cachedFields = new ConcurrentHashMap<String, List<Field>>();

    public void clearCache() {
        this.cachedFields.clear();
    }

    public List<Field> getFields(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        String cacheKey = this.getCacheKey(targetClass, annotationClass);

        List<Field> result = this.cachedFields.get(cacheKey);
        if (result != null) {
            return result;
        }

        result = new LinkedList<Field>();

        for (Field eachDeclaredField : getAllDeclaredFields(targetClass)) {
            if (eachDeclaredField.isAnnotationPresent(annotationClass)) {
                result.add(eachDeclaredField);
            }
        }

        this.cachedFields.put(cacheKey, result);

        return result;
    }

    private String getCacheKey(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(targetClass.getName());
        stringBuilder.append("_");
        stringBuilder.append(annotationClass.getName());

        return stringBuilder.toString();
    }
}
