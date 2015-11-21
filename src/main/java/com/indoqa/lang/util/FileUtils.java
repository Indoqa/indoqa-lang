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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public final class FileUtils {

    private FileUtils() {
        // hide utility class constructor
    }

    /**
     * Gets the canonical form of the given <code>file</code>.
     * 
     * If {@link File#getCanonicalFile()} fails, this method will return the given <code>file</code> instead of throwing an
     * {@link IOException}.
     * 
     * If the given <code>file</code> is NULL, this method will return NULL.
     * 
     * @param file The file to get the canonical form of.
     * @return The canonical file or if that fails, the given file.
     */
    public static File getCanonicalFile(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file;
        }
    }

    public static String getRelativeFilePath(File file, File directory) {
        return getRelativeFilePath(getCanonicalFile(file).getAbsolutePath(), getCanonicalFile(directory).getAbsolutePath());
    }

    public static String getRelativeFilePath(String filePath, String directoryPath) {
        String baseDir = FilenameUtils.normalize(directoryPath + File.separator);
        String currentFile = FilenameUtils.normalize(filePath);

        if (!currentFile.startsWith(baseDir)) {
            throw new IllegalArgumentException(
                "Failed to get the relative file path. The given file path must start with the base directory!");
        }

        return currentFile.substring(baseDir.length());
    }
}
