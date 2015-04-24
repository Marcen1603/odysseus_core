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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
final public class TimeIntervalProbabilisticLatency extends AbstractMetaAttribute
		implements ILatency, ITimeInterval, IProbabilistic {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4833031661270663461L;
	/** Included classes. */
	@SuppressWarnings("unchecked")
	public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ITimeInterval.class, ILatency.class, IProbabilistic.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(
			CLASSES.length);
	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Latency.schema);
		schema.addAll(Probabilistic.schema);
	}

	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}

	private final ITimeInterval timeInterval;
	/** The {@link ILatency} instance. */
	private final ILatency latency;
	private final IProbabilistic probabilistic;

	/**
	 * Creates a new {@link TimeIntervalProbabilisticLatency} instance.
	 */
	public TimeIntervalProbabilisticLatency() {
		timeInterval = new TimeInterval();
		latency = new Latency();
		probabilistic = new Probabilistic();
	}

	/**
	 * Clone constructor.
	 * 
	 * @param clone
	 *            The object to clone from
	 */
	public TimeIntervalProbabilisticLatency(
			final TimeIntervalProbabilisticLatency clone) {
		this.timeInterval = clone.timeInterval.clone();
		this.latency = clone.latency.clone();
		this.probabilistic = clone.probabilistic.clone();
	}

	@Override
	public final TimeIntervalProbabilisticLatency clone() {
		return new TimeIntervalProbabilisticLatency(this);
	}

	@Override
	public String getName() {
		return "TimeIntervalProbabilisticLatency";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void fillValueList(List<Tuple<?>> values) {
		timeInterval.fillValueList(values);
		latency.fillValueList(values);
		probabilistic.fillValueList(values);
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return latency.getValue(0, index);
			case 2:
				return probabilistic.getValue(0, index);
		}
		return null;
	}

	@Override
	public final String toString() {
		return "( i= " + timeInterval.toString() + " | l=" + this.latency
				+ " | prob =" + probabilistic.toString() + ")";
	}

	@Override
	public String toString(PointInTime baseTime) {
		return "( i= " + timeInterval.toString(baseTime) + " | l="
				+ this.latency + " | prob =" + probabilistic.toString() + ")";
	}

	@Override
	public final String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options) + options.getDelimiter()
				+ this.latency.csvToString(options) + options.getDelimiter()
				+ probabilistic.csvToString(options);
	}

	@Override
	public final String getCSVHeader(final char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter
				+ this.latency.getCSVHeader(delimiter) + delimiter
				+ probabilistic.getCSVHeader(delimiter);
	}
	
	// ------------------------------------------------------------------------------
	// Delegates for timeInterval
	// ------------------------------------------------------------------------------
	
	@Override
	public PointInTime getStart() {
		return timeInterval.getStart();
	}

	@Override
	public PointInTime getEnd() {
		return timeInterval.getEnd();
	}

	@Override
	public void setStart(PointInTime point) {
		timeInterval.setStart(point);
	}

	@Override
	public void setEnd(PointInTime point) {
		timeInterval.setEnd(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		timeInterval.setStartAndEnd(start, end);
	}

	@Override
	public int compareTo(ITimeInterval o) {
		return timeInterval.compareTo(o);
	}
	
	// ------------------------------------------------------------------------------
	// Delegates for latency
	// ------------------------------------------------------------------------------

	
	@Override
	public final long getLatency() {
		return latency.getLatency();
	}
	
	@Override
	public long getMaxLatency() {
		return latency.getMaxLatency();
	}

	@Override
	public final long getLatencyEnd() {
		return latency.getLatencyEnd();
	}

	@Override
	public final long getLatencyStart() {
		return latency.getLatencyStart();
	}
	
	@Override
	public long getMaxLatencyStart() {
		return latency.getMaxLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setMinLatencyStart(long timestamp) {
		latency.setMinLatencyStart(timestamp);
	}
	
	@Override
	public void setMaxLatencyStart(long timestamp) {
		latency.setMaxLatencyStart(timestamp);
	}

	// ------------------------------------------------------------------------------
	// Delegates for Probabilistic
	// ------------------------------------------------------------------------------

	public double getExistence() {
		return probabilistic.getExistence();
	}

	public void setExistence(double existence) {
		probabilistic.setExistence(existence);
	}
	
	
	

}
