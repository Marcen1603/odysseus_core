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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TimeIntervalProbabilisticLatency extends TimeIntervalProbabilistic implements ILatency, ILatencyTimeIntervalProbabilistic {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4833031661270663461L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ITimeInterval.class, ILatency.class, IProbabilistic.class };

	private final ILatency latency;

	public TimeIntervalProbabilisticLatency() {
		super();
		this.latency = new Latency();
	}

	public TimeIntervalProbabilisticLatency(final TimeIntervalProbabilisticLatency clone) {
		super(clone);
		this.latency = clone.latency.clone();
	}

	@Override
	public final long getLatency() {
		return this.latency.getLatency();
	}

	@Override
	public long getMaxLatency() {
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
	public long getMaxLatencyStart() {
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
	public void setMaxLatencyStart(final long timestamp) {
		this.latency.setMaxLatencyStart(timestamp);
	}

	@Override
	public TimeIntervalProbabilisticLatency clone() {
		return new TimeIntervalProbabilisticLatency(this);
	}

	@Override
	public String toString() {
		return "( i= " + super.toString() + " | " + " l=" + this.latency + ")";
	}

	@Override
	public String csvToString(final char delimiter, final Character textSeperator, final NumberFormat floatingFormatter, final NumberFormat numberFormatter, final boolean withMetadata) {
		return super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter + this.latency.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
	}

	@Override
	public String getCSVHeader(final char delimiter) {
		return super.getCSVHeader(delimiter) + "+delimiter+" + this.latency.getCSVHeader(delimiter);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return TimeIntervalProbabilisticLatency.classes;
	}
}
