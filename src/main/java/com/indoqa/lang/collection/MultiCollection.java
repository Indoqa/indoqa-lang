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
package com.indoqa.lang.collection;

import java.util.*;

public class MultiCollection<T> implements Collection<T> {

    private List<Collection<T>> collections = new LinkedList<Collection<T>>();

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    public void addCollection(Collection<T> collection) {
        this.collections.add(collection);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        for (Collection<T> collection : this.collections) {
            if (collection.contains(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if (!this.contains(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.collections.isEmpty() || this.size() == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new MultiIterator<T>(this.collections);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int size = 0;

        for (Collection<T> collection : this.collections) {
            size += collection.size();
        }

        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.size()];
        this.fillArray(result);
        return result;
    }

    @Override
    public <E> E[] toArray(E[] target) {
        E[] result;

        int requiredSize = this.size();
        if (target.length < requiredSize) {
            result = Arrays.copyOf(target, requiredSize);
        } else {
            result = target;
        }

        this.fillArray(result);
        return result;
    }

    private void fillArray(Object[] result) {
        int index = 0;
        for (Collection<T> collection : this.collections) {
            for (T element : collection) {
                result[index++] = element;
            }
        }

        // fill remaining slots with NULL
        for (; index < result.length; index++) {
            result[index] = null;
        }
    }
}
