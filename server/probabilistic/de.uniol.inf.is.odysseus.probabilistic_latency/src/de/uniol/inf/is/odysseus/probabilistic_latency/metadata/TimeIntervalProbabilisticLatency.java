/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic_latency.metadata;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticTimeInterval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimeIntervalProbabilisticLatency extends ProbabilisticTimeInterval implements ILatency, ILatencyTimeIntervalProbabilistic {
    /**
	 * 
	 */
    private static final long serialVersionUID = 4833031661270663461L;
    /** Included classes. */
    @SuppressWarnings("unchecked")
    public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, ILatency.class, IProbabilistic.class };
    /** The {@link ILatency} instance. */
    private final ILatency latency;

    /**
     * Creates a new {@link TimeIntervalProbabilisticLatency} instance.
     */
    public TimeIntervalProbabilisticLatency() {
        super();
        this.latency = new Latency();
    }

    /**
     * Clone constructor.
     * 
     * @param clone
     *            The object to clone from
     */
    public TimeIntervalProbabilisticLatency(final TimeIntervalProbabilisticLatency clone) {
        super(clone);
        this.latency = clone.latency.clone();
    }

    @Override
    public final long getLatency() {
        return this.latency.getLatency();
    }

    @Override
    public final long getMaxLatency() {
        return this.latency.getMaxLatency();
    }

    @Override
    public final long getLatencyEnd() {
        return this.latency.getLatencyEnd();
    }

    @Override
    public final long getLatencyStart() {
        return this.latency.getLatencyStart();
    }

    @Override
    public final long getMaxLatencyStart() {
        return this.latency.getMaxLatencyStart();
    }

    @Override
    public final void setLatencyEnd(final long timestamp) {
        this.latency.setLatencyEnd(timestamp);
    }

    @Override
    public final void setMinLatencyStart(final long timestamp) {
        this.latency.setMinLatencyStart(timestamp);
    }

    @Override
    public final void setMaxLatencyStart(final long timestamp) {
        this.latency.setMaxLatencyStart(timestamp);
    }

    @Override
    public final TimeIntervalProbabilisticLatency clone() {
        return new TimeIntervalProbabilisticLatency(this);
    }

    @Override
    public final String toString() {
        return "( i= " + super.toString() + " | " + " l=" + this.latency + ")";
    }

    @Override
    public final String csvToString(WriteOptions options) {
        return super.csvToString(options) + options.getDelimiter()
                + this.latency.csvToString(options);
    }

    @Override
    public final String getCSVHeader(final char delimiter) {
        return super.getCSVHeader(delimiter) + "+delimiter+" + this.latency.getCSVHeader(delimiter);
    }

    @Override
    public final Class<? extends IMetaAttribute>[] getClasses() {
        return TimeIntervalProbabilisticLatency.CLASSES;
    }

    @Override
    public String getName() {
        return "TimeIntervalProbabilisticLatency";
    }
}
