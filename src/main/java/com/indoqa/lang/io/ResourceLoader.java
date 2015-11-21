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

import static com.indoqa.lang.util.StringUtils.join;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public final class ResourceLoader {

    private static final String PROTOCOL_FILE = "file:";
    private static final String PROTOCOL_CLASSPATH = "classpath:";

    private ResourceLoader() {
        // hide utility class constructor
    }

    public static URL getUrl(String resource) throws IOException {
        return getUrl(resource, ResourceLoader.class);
    }

    public static URL getUrl(String resource, Class<?> loadingClass) throws IOException {
        return getUrl(resource, loadingClass.getClassLoader());
    }

    public static URL getUrl(String path, ClassLoader classLoader) throws IOException {
        if (path.startsWith(PROTOCOL_CLASSPATH)) {
            String resourcePath = path.substring(PROTOCOL_CLASSPATH.length());
            while (resourcePath.charAt(0) == '/') {
                resourcePath = resourcePath.substring(1);
            }

            return classLoader.getResource(resourcePath);
        }

        if (path.startsWith(PROTOCOL_FILE)) {
            try {
                return new URL(path);
            } catch (MalformedURLException e) {
                throw new IOException("Failed to create URL for filepath '" + path + "'", e);
            }
        }

        // assume a relative path and try to build a URL
        String absolutePath = new File(path).getAbsolutePath();
        try {
            if (absolutePath.startsWith("/")) {
                return new URL(join(PROTOCOL_FILE, "//", absolutePath));
            }

            return new URL(join(PROTOCOL_FILE, "///", absolutePath));
        } catch (MalformedURLException e) {
            throw new IOException("Failed to create URL for relative filepath '" + path + "'", e);
        }
    }
}
