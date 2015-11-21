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

import java.lang.reflect.ParameterizedType;
import java.util.Date;

import org.junit.Test;

import com.indoqa.lang.util.GenericsUtils;
import com.indoqa.lang.util.GenericsUtils.ReflectionException;
import com.indoqa.lang.util.generics.*;

public class GenericsUtilsTest {

    @Test
    public void findByAbstractSuperType() {
        Class<?> type = GenericsUtils.lookupType(GenericInterfaceImpl1.class, GenericInterface.class, AbstractGenericInterface.class,
            "invoke");
        assertEquals(GenericTypeImpl.class, type);
    }

    @Test
    public void findByInterface() {
        Class<?> type = GenericsUtils.lookupType(GenericInterfaceImpl2.class, GenericInterface.class, AbstractGenericInterface.class,
            "invoke");
        assertEquals(GenericTypeImpl.class, type);
    }

    @Test
    public void getGenericParameter() {
        Class<?> genericParameter = GenericsUtils.getGenericParameter(ChildClass.class, AbstractGenericBaseClass.class, 0);
        assertEquals(String.class, genericParameter);

        genericParameter = GenericsUtils.getGenericParameter(ChildClass.class, AbstractGenericBaseClass.class, 1);
        assertEquals(Date.class, genericParameter);

        genericParameter = GenericsUtils.getGenericParameter(ChildClass.class, AbstractGenericExtensionClass.class, 0);
        assertEquals(Void.class, genericParameter);

        genericParameter = GenericsUtils.getGenericParameter(GenericInterfaceImpl1.class, GenericInterface.class, 0);
        assertEquals(GenericTypeImpl.class, genericParameter);
    }

    @Test(expected = ReflectionException.class)
    public void getGenericParameterWithInvalidIndex() {
        GenericsUtils.getGenericParameter(ChildClass.class, AbstractGenericBaseClass.class, 2);
    }

    @Test(expected = ReflectionException.class)
    public void getGenericParameterWithNonParameterizedDeclaringClass() {
        GenericsUtils.getGenericParameter(ChildClass.class, AbstractBaseClass.class, 1);
    }

    @Test(expected = ReflectionException.class)
    public void getGenericParameterWithNonParentClass() {
        GenericsUtils.getGenericParameter(ChildClass.class, Date.class, 1);
    }

    @Test
    public void getParameterizedType() {
        ParameterizedType parameterizedType = GenericsUtils.getParameterizedType(ChildClass.class,
            AbstractGenericExtensionClass.class);
        assertEquals(AbstractGenericExtensionClass.class, parameterizedType.getRawType());
        assertEquals(1, parameterizedType.getActualTypeArguments().length);

        parameterizedType = GenericsUtils.getParameterizedType(ChildClass.class, AbstractGenericBaseClass.class);
        assertEquals(AbstractGenericBaseClass.class, parameterizedType.getRawType());
        assertEquals(2, parameterizedType.getActualTypeArguments().length);
    }

    @Test
    public void getParameterizedTypeWithNonParameterizedDeclaringClass() {
        assertNull(GenericsUtils.getParameterizedType(ChildClass.class, AbstractBaseClass.class));

        assertNull(GenericsUtils.getParameterizedType(ChildClass.class, ChildClass.class));
    }

    @Test(expected = ReflectionException.class)
    public void getParameterizedTypeWithNonParentClass() {
        assertNull(GenericsUtils.getParameterizedType(ChildClass.class, Date.class));
    }
}
