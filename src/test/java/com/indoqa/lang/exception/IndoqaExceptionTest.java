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
package com.indoqa.lang.exception;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.indoqa.lang.exception.IndoqaException;

public class IndoqaExceptionTest {

    @Test
    public void chainedWithMessage() {
        try {
            throw new IndoqaException("Test Message", new NullPointerException()) {

                private static final long serialVersionUID = 1L;
            };
        } catch (IndoqaException e) {
            assertEquals("[IDQ-Exception] Test Message", e.getMessage());
        }
    }

    @Test
    public void chainedWithoutMessage() {
        try {
            throw new IndoqaException(new NullPointerException()) {

                private static final long serialVersionUID = 1L;
            };
        } catch (IndoqaException e) {
            assertEquals("[IDQ-Exception] java.lang.NullPointerException", e.getMessage());
        }
    }

    @Test
    public void unchainedWithMessage() {
        try {
            throw new IndoqaException("Test Message") {

                private static final long serialVersionUID = 1L;
            };
        } catch (IndoqaException e) {
            assertEquals("[IDQ-Exception] Test Message", e.getMessage());
        }
    }

    @Test
    public void unchainedWithoutMessage() {
        try {
            throw new IndoqaException() {

                private static final long serialVersionUID = 1L;
            };
        } catch (IndoqaException e) {
            assertEquals("[IDQ-Exception] No message!", e.getMessage());
        }
    }
}
