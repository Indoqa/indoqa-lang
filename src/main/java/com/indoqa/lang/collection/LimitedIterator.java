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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LimitedIterator<T> implements Iterator<T> {

    private int count;
    private final int maxCount;
    private final Iterator<T> iterator;

    public LimitedIterator(Iterator<T> iterator, int maxCount) {
        this.iterator = iterator;
        this.maxCount = maxCount;
    }

    @Override
    public boolean hasNext() {
        return this.count < this.maxCount && this.iterator.hasNext();
    }

    @Override
    public T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }

        this.count++;
        return this.iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
