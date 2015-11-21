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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public final class EncodingUtils {

    private static final String ENCODING = "UTF-8";

    private EncodingUtils() {
        // hide utility class constructor
    }

    public static int getByteCount(String string) {
        return getByteCount(string, 0, string.length());
    }

    public static int getByteCount(String string, int start, int length) {
        try {
            CountingOutputStream countingOutputStream = new CountingOutputStream();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(countingOutputStream, ENCODING);
            outputStreamWriter.write(string, start, length);
            outputStreamWriter.close();

            return countingOutputStream.getCount();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to get byte count from String. " + ENCODING + " is not available!", e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get byte count from String.", e);
        }
    }

    public static byte[] getBytes(String string) {
        try {
            return string.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to get bytes from String. " + ENCODING + " is not available!", e);
        }
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, 0, bytes.length);
    }

    public static String getString(byte[] bytes, int offset, int length) {
        try {
            return new String(bytes, offset, length, ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Failed to create String from bytes. " + ENCODING + " is not available!", e);
        }
    }

    protected static class CountingOutputStream extends OutputStream {

        private int count;

        public int getCount() {
            return this.count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public void write(int b) throws IOException {
            this.count++;
        }
    }
}
