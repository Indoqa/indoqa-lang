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
package com.indoqa.lang.manifest;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public final class ManifestUtils {

    private ManifestUtils() {
        // no instantiation
    }

    public static String getAttribute(Class<?> archivedClass, String property) throws IOException {
        Manifest manifest = getManifest(archivedClass);
        if (manifest == null) {
            return null;
        }

        Attributes entries = manifest.getMainAttributes();
        return entries.getValue(property);
    }

    private static Manifest getManifest(Class<?> archivedClass) throws IOException {
        URL codeBase = archivedClass.getProtectionDomain().getCodeSource().getLocation();
        if (!codeBase.getPath().endsWith(".jar")) {
            return null;
        }

        JarInputStream jarInputStream = null;
        try {
            jarInputStream = new JarInputStream(codeBase.openStream());
            return jarInputStream.getManifest();
        } finally {
            if (jarInputStream != null) {
                jarInputStream.close();
            }
        }
    }
}
