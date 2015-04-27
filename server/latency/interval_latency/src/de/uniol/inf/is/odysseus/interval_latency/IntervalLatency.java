/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.interval_latency;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
final public class IntervalLatency extends AbstractCombinedMetaAttribute implements ILatency, ITimeInterval {

	private static final long serialVersionUID = -3129934770814427153L;
	
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		ITimeInterval.class, ILatency.class
	};
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}
	
	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(classes.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(Latency.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}
	
	@Override
	public String getName() {
		return "IntervalLatency";
	}
	
	private final ILatency latency;
	private final ITimeInterval timeInterval;
	
	public IntervalLatency() {
		timeInterval = new TimeInterval();
		latency = new Latency();
	}
	
	public IntervalLatency(IntervalLatency clone) {
		this.timeInterval = clone.timeInterval.clone();
		this.latency = clone.latency.clone();
	}
	
	@Override
	public IntervalLatency clone() {
		return new IntervalLatency(this);
	}
	
	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		latency.retrieveValues(values);
	}
	
	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		latency.writeValue(values.get(1));
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return latency.getValue(0, index);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "( i= " +timeInterval.toString() + " | " + " l="+ this.latency+ ")";
	}
	
	@Override
	public String toString(PointInTime baseTime) {
		return "( i= " +timeInterval.toString(baseTime) + " | " + " l="+ this.latency+ ")";
	}

	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options)+options.getDelimiter()+this.latency.csvToString(options);
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter)+delimiter+this.latency.getCSVHeader(delimiter);
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

	
}
