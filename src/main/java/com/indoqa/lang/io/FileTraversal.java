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
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract utility class to recursively find all files and sub directories.
 * 
 * Usage
 * 
 * <pre>
 * new FileTraversal() {
 * 
 *     public void onFile(final File f) {
 *         System.out.println(f);
 *     }
 * }.traverse(new File(&quot;somedir&quot;));
 * </pre>
 */
public abstract class FileTraversal {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void onDirectory(@SuppressWarnings("unused") final File directory) {
        // empty default implementation
    }

    public void onFile(@SuppressWarnings("unused") final File file) {
        // empty default implementation
    }

    public final void traverse(final File file) throws IOException {
        this.log.trace("Traversing " + file.getAbsolutePath());

        if (file.isDirectory()) {
            this.onDirectory(file);

            final File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    this.traverse(child);
                }
            }

            return;
        }

        this.onFile(file);
    }
}
