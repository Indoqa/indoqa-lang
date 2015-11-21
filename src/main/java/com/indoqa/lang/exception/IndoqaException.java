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

public abstract class IndoqaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IndoqaException() {
        super();
    }

    public IndoqaException(String message) {
        super(message);
    }

    public IndoqaException(String message, Throwable e) {
        super(message, e);
    }

    public IndoqaException(Throwable e) {
        super(e);
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();

        if (message == null) {
            return "[IDQ-Exception] " + "No message!";
        }

        return "[IDQ-Exception] " + message;
    }
}
