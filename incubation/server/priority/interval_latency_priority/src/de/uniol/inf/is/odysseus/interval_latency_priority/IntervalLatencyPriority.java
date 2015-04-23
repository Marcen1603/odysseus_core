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
package de.uniol.inf.is.odysseus.interval_latency_priority;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

public class IntervalLatencyPriority extends TimeInterval implements ILatency,
		IPriority, Serializable {

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] {
			ITimeInterval.class, ILatency.class, IPriority.class };

	private static final long serialVersionUID = -4924797905689073685L;
	
	static final SDFSchema schema;
	static{
		schema = SDFSchemaFactory.createNewSchema(TimeInterval.schema, Latency.schema, Priority.schema);
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	private ILatency latency;
	private IPriority prio;

	public IntervalLatencyPriority() {
		super(PointInTime.getInfinityTime());
		this.latency = new Latency();
		this.prio = new Priority();
	}

	public IntervalLatencyPriority(long start) {
		super(PointInTime.getInfinityTime());
		this.latency = new Latency();
		this.prio = new Priority();
		setMinLatencyStart(start);
	}

	public IntervalLatencyPriority(IntervalLatencyPriority original) {
		super(original);

		this.latency = original.latency.clone();
		this.prio = (IPriority) original.prio.clone();
	}
	
	@Override
	public void fillValueList(List<Tuple<?>> values) {
		super.fillValueList(values);
		latency.fillValueList(values);
		prio.fillValueList(values);
	}

	@Override
	public final long getLatencyEnd() {
		return this.latency.getLatencyEnd();
	}

	@Override
	public long getMaxLatencyStart() {
		return latency.getMaxLatencyStart();
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
	public final long getLatencyStart() {
		return this.latency.getLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setMinLatencyStart(long timestamp) {
		this.latency.setMinLatencyStart(timestamp);
	}

	@Override
	public void setMaxLatencyStart(long timestamp) {
		latency.setMaxLatencyStart(timestamp);
	}

	@Override
	public IntervalLatencyPriority clone() {
		return new IntervalLatencyPriority(this);
	}

	@Override
	public String toString() {
		return "( i= " + super.toString() + " ; " + " l=" + this.latency + ""
				+ " ; p=" + this.prio + ")";
	}

	@Override
	public String csvToString(WriteOptions options) {
		return super.csvToString(options)
				+ options.getDelimiter()
				+ this.latency.csvToString(options)
				+ options.getDelimiter()
				+ this.prio.csvToString(options);
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + "+delimiter+"
				+ this.latency.getCSVHeader(delimiter) + "+delimiter+"
				+ this.prio.getCSVHeader(delimiter);
	}

	@Override
	public final byte getPriority() {
		return this.prio.getPriority();
	}

	@Override
	public final void setPriority(byte priority) {
		this.prio.setPriority(priority);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return "IntervalLatencyPriority";
	}

}
