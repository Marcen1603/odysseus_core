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

import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatencyTimeInterval;
import de.uniol.inf.is.odysseus.latency.Latency;

/**
 * @author Jonas Jacobi
 */
public class IntervalLatency extends TimeInterval implements ILatency, ILatencyTimeInterval {

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		ITimeInterval.class, ILatency.class
	};
	
	static final SDFSchema schema;
	static{
		schema = SDFSchemaFactory.createNewSchema(TimeInterval.schema, Latency.schema);
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
	
	private static final long serialVersionUID = -3129934770814427153L;
	private final ILatency latency;
	
	public IntervalLatency() {
		super();
		latency = new Latency();
	}
	

	public IntervalLatency(IntervalLatency clone) {
		super(clone);
		this.latency = clone.latency.clone();
	}
	
	@Override
	public void fillValueList(List<Tuple<?>> values) {
		super.fillValueList(values);
		latency.fillValueList(values);
	}
	
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

	@Override
	public IntervalLatency clone() {
		return new IntervalLatency(this);
	}
	
	@Override
	public String toString() {
		return "( i= " +super.toString() + " | " + " l="+ this.latency+ ")";
	}

	@Override
	public String csvToString(WriteOptions options) {
		return super.csvToString(options)+options.getDelimiter()+this.latency.csvToString(options);
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter)+delimiter+this.latency.getCSVHeader(delimiter);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}
	
	@Override
	public String getName() {
		return "IntervalLatency";
	}
	
}
