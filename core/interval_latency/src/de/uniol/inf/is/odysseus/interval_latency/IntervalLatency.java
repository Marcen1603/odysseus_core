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

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.Latency;

/**
 * @author Jonas Jacobi
 */
public class IntervalLatency extends TimeInterval implements ILatency {

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
	public final long getLatency() {
		return latency.getLatency();
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
	public final void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setLatencyStart(long timestamp) {
		latency.setLatencyStart(timestamp);
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
	public String csvToString() {
		return super.csvToString()+";"+this.latency.csvToString();
	}
	
	@Override
	public String getCSVHeader() {
		return super.getCSVHeader()+";"+this.latency.getCSVHeader();
	}
	
}
