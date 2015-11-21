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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} for lines in a file.<br>
 * <br>
 * This iterator will use a {@link BufferedReader} to read all lines in a file.<br>
 * Each call to {@link #next()} will return one line until no more lines are available.<br>
 * The file will be closed when the last line was read or this object is garbage collected (which ever happens first).<br>
 * <br>
 * This class also implements {@link Iterable}, so it can be used directly in for statements. <br>
 * <br>
 * The {@link #remove()} method is not supported and will throw an {@link UnsupportedOperationException}. <br>
 * <br>
 * Usage:
 * 
 * <pre>
 * for (String line : new FileLineIterator(fileName)) {
 *     System.out.println(line);
 * }
 * </pre>
 */
public class FileLineIterator implements Iterator<String>, Iterable<String> {

    private BufferedReader bufferedReader;

    private String nextLine;
    private boolean finished;

    public FileLineIterator(String filePath) throws FileNotFoundException {
        super();

        this.bufferedReader = new BufferedReader(new FileReader(filePath));
    }

    @Override
    public boolean hasNext() {
        if (this.nextLine != null) {
            return true;
        }

        if (this.finished) {
            return false;
        }

        try {
            this.nextLine = this.bufferedReader.readLine();
            this.finished = this.nextLine == null;

            if (this.finished) {
                this.bufferedReader.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read from file", e);
        }

        return !this.finished;
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public String next() {
        if (this.nextLine == null && !this.hasNext()) {
            throw new NoSuchElementException("No more lines available");
        }

        String line = this.nextLine;
        this.nextLine = null;
        return line;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove lines from a file.");
    }

    @Override
    protected void finalize() throws Throwable {
        this.bufferedReader.close();

        super.finalize();
    }
}
