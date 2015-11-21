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

public final class ExceptionUtils {

    private ExceptionUtils() {
        // hide utility class constructor
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T getCause(Throwable throwable, Class<T> type) {
        Throwable currentThrowable = throwable;

        while (currentThrowable != null) {
            Throwable cause = currentThrowable.getCause();
            if (cause != null && cause.getClass().equals(type)) {
                return (T) cause;
            }

            currentThrowable = cause;
        }

        return null;
    }
}
