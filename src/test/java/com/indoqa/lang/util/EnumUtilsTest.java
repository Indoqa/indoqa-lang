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

import static com.indoqa.lang.util.EnumUtils.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.function.Function;

import org.junit.Test;

public class EnumUtilsTest {

    @Test
    public void lookupByProperty_multiple() {
        Collection<Cars> cars = Cars.lookupByColor(Color.RED);
        assertNotNull(cars);
        assertEquals(2, cars.size());
    }

    @Test
    public void lookupByProperty_multiple_not_existing() {
        assertTrue(Cars.lookupByColor(Color.GREEN).isEmpty());
    }

    @Test
    public void lookupByProperty_multiple_null() {
        assertTrue(Cars.lookupByColor(null).isEmpty());
    }

    @Test
    public void lookupByProperty_single() {
        Cars mercedes = Cars.lookupByDisplayName("Mercedes");

        assertNotNull(mercedes);
        assertEquals("Mercedes", mercedes.getName());
        assertEquals(Color.RED, mercedes.getColor());
    }

    @Test
    public void lookupByProperty_single_empty() {
        assertNull(Cars.lookupByDisplayName(""));
    }

    @Test
    public void lookupByProperty_single_not_existing() {
        assertNull(Cars.lookupByDisplayName("not-existing"));
    }

    @Test
    public void lookupByProperty_single_null() {
        assertNull(Cars.lookupByDisplayName(null));
    }

    private enum Cars {

        VW("VW", Color.BLUE),

        BWM("BWM", Color.RED),

        Mercedes("Mercedes", Color.RED);

        private static final Function<String, Cars> NAME_LOOKUP_FN = lookupEnum(Cars.class, e -> e.getName());
        private static final Function<Color, Collection<Cars>> COLOR_LOOKUP_FN = lookupEnums(Cars.class, e -> e.getColor());

        private final String name;
        private final Color color;

        private Cars(String displayName, Color color) {
            this.name = displayName;
            this.color = color;
        }

        public static Collection<Cars> lookupByColor(Color color) {
            return COLOR_LOOKUP_FN.apply(color);
        }

        public static Cars lookupByDisplayName(String name) {
            return NAME_LOOKUP_FN.apply(name);
        }

        public Color getColor() {
            return this.color;
        }

        public String getName() {
            return this.name;
        }
    }

    private enum Color {
        RED, BLUE, GREEN;
    }
}
