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

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class TimeTracker {

    private long startTime;
    private Date startDate;
    private long lapStartTime;

    private Long endTime;

    private long count;

    private List<Long> lapTimes = new ArrayList<Long>();

    public TimeTracker() {
        super();

        this.reset();
    }

    public long getCount() {
        return this.count;
    }

    public long getElapsed(TimeUnit timeUnit) {
        return timeUnit.convert(this.getElapsedNanoseconds(), NANOSECONDS);
    }

    public long getLap(TimeUnit timeUnit) {
        return timeUnit.convert(this.getLapNanoseconds(), NANOSECONDS);
    }

    public List<Long> getLapTimes(TimeUnit timeUnit) {
        List<Long> result = new ArrayList<Long>(this.lapTimes.size());

        for (Long eachLapTime : this.lapTimes) {
            result.add(timeUnit.convert(eachLapTime, NANOSECONDS));
        }

        return result;
    }

    public long getRemaining(long total, TimeUnit timeUnit) {
        long remainingCount = total - this.count;
        long remainingNanoseconds = remainingCount * this.getNanosecondsPerCount();

        return timeUnit.convert(remainingNanoseconds, NANOSECONDS);
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public long getTimePerCount(TimeUnit timeUnit) {
        return timeUnit.convert(this.getNanosecondsPerCount(), NANOSECONDS);
    }

    public void increment() {
        this.count++;
    }

    public void lap() {
        this.lapTimes.add(this.getLapNanoseconds());

        this.lapStartTime = this.now();
    }

    public void reset() {
        this.startTime = this.now();
        this.startDate = new Date();

        this.lapStartTime = this.startTime;
        this.endTime = null;
        this.count = 0;
    }

    public void stop() {
        this.endTime = this.now();
    }

    private long getElapsedNanoseconds() {
        if (this.endTime == null) {
            return this.now() - this.startTime;
        }

        return this.endTime - this.startTime;
    }

    private long getLapNanoseconds() {
        if (this.endTime == null) {
            return this.now() - this.lapStartTime;
        }

        return this.endTime - this.lapStartTime;
    }

    private long getNanosecondsPerCount() {
        if (this.count == 0) {
            return 0;
        }

        return this.getElapsedNanoseconds() / this.count;
    }

    private long now() {
        return System.nanoTime();
    }
}
