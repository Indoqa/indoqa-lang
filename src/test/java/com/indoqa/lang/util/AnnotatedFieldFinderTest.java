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

import static junit.framework.Assert.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.indoqa.lang.util.AnnotatedFieldFinder;
import com.indoqa.lang.util.field.FinalClass;
import com.indoqa.lang.util.field.TestAnnotation1;
import com.indoqa.lang.util.field.TestAnnotation2;

public class AnnotatedFieldFinderTest {

    @Test
    public void testAnnotation1() {
        AnnotatedFieldFinder finder = new AnnotatedFieldFinder();

        List<Field> fields = finder.getFields(FinalClass.class, TestAnnotation1.class);
        assertNotNull(fields);
        assertEquals(3, fields.size());

        Set<String> actualFields = new HashSet<String>();
        for (Field eachField : fields) {
            actualFields.add(eachField.toString());
        }

        assertTrue(actualFields.contains("private java.lang.String com.indoqa.lang.util.field.BaseClass.value1"));
        assertTrue(actualFields.contains("private java.lang.String com.indoqa.lang.util.field.ExtendedBaseClass.value3"));
        assertTrue(actualFields.contains("private double com.indoqa.lang.util.field.FinalClass.value5"));
    }

    @Test
    public void testAnnotation2() {
        AnnotatedFieldFinder finder = new AnnotatedFieldFinder();

        List<Field> fields = finder.getFields(FinalClass.class, TestAnnotation2.class);
        assertNotNull(fields);
        assertEquals(3, fields.size());

        Set<String> actualFields = new HashSet<String>();
        for (Field eachField : fields) {
            actualFields.add(eachField.toString());
        }

        assertTrue(actualFields.contains("private java.util.Date com.indoqa.lang.util.field.BaseClass.value2"));
        assertTrue(actualFields.contains("private java.lang.String com.indoqa.lang.util.field.ExtendedBaseClass.value3"));
        assertTrue(actualFields.contains("private java.lang.String com.indoqa.lang.util.field.FinalClass.value4"));
    }

    @Test
    public void testDeprecated() {
        AnnotatedFieldFinder finder = new AnnotatedFieldFinder();

        List<Field> fields = finder.getFields(FinalClass.class, Deprecated.class);
        assertNotNull(fields);
        assertEquals(0, fields.size());

        fields = finder.getFields(FinalClass.class, Deprecated.class);
    }
}
