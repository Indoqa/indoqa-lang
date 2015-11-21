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
package com.indoqa.lang.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeletingFileInputStream extends FileInputStream {

    private final File file;

    public DeletingFileInputStream(File file) throws FileNotFoundException {
        super(file);

        this.file = file;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.deleteFile();
        }
    }

    protected void deleteFile() {
        if (this.file == null || !this.file.exists()) {
            return;
        }

        try {
            this.file.delete();
        } catch (Exception ignored) {
            // do nothing
        }
    }
}
