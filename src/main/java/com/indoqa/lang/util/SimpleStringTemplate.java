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

public class SimpleStringTemplate {

    private StringBuilder stringBuilder;

    public SimpleStringTemplate(String template) {
        super();

        this.stringBuilder = new StringBuilder(template);
    }

    public String getResult() {
        return this.stringBuilder.toString();
    }

    public void replace(String placeholder, Object replacement) {
        String searchString = "{" + placeholder + "}";
        String replacementString = String.valueOf(replacement);

        while (true) {
            int startIndex = this.stringBuilder.indexOf(searchString);
            if (startIndex == -1) {
                break;
            }

            this.stringBuilder.replace(startIndex, startIndex + searchString.length(), replacementString);
        }
    }

    @Override
    public String toString() {
        return this.getResult();
    }

    protected void append(Object value) {
        this.stringBuilder.append(value);
    }
}
