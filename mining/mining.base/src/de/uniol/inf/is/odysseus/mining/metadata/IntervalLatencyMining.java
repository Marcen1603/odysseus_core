/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.metadata;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.latency.Latency;

public class IntervalLatencyMining extends TimeInterval implements ILatency, IMiningMetadata, Serializable {


	private static final long serialVersionUID = -4481993965683442055L;
	private ILatency latency;
	private IMiningMetadata mining;

	public IntervalLatencyMining(){
		super(PointInTime.getInfinityTime());
		this.latency = new Latency();
		this.mining = new MiningMetadata();
	}	

	public IntervalLatencyMining(IntervalLatencyMining ilm) {
		super(ilm.getStart().clone());
		this.latency = ilm.latency.clone();
		this.mining = (IMiningMetadata) ilm.mining.clone();
	}

	@Override
	public boolean isCorrected() {
		return this.mining.isCorrected();
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.latency.setLatencyStart(timestamp);
		
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public long getLatencyStart() {
		return this.latency.getLatencyStart();
	}

	@Override
	public long getLatencyEnd() {
		return this.latency.getLatencyEnd();
	}

	@Override
	public long getLatency() {
		return this.latency.getLatency();
	}
	
	@Override
    public IntervalLatencyMining clone(){
		return new IntervalLatencyMining(this);
	}
	
	@Override
	public String toString() {
		return "\t( INTERVALL= " + super.toString() + ";\t LATENCY=" + this.latency + ";\t MINING=" + this.mining + ")";
	}

	@Override
	public boolean isDetected() {
		return this.mining.isDetected();
	}

	@Override
	public boolean isCorrectedAttribute(String attribute) {
		return this.mining.isCorrectedAttribute(attribute);
	}

	@Override
	public void setCorrectedAttribute(String attribute, boolean corrected) {
		this.mining.setCorrectedAttribute(attribute, corrected);	
	}

	@Override
	public boolean isDetectedAttribute(String attribute) {
		return this.mining.isDetectedAttribute(attribute);
	}

	@Override
	public void setDetectedAttribute(String attribute, boolean detected) {
		this.mining.setDetectedAttribute(attribute, detected);
	}	

}
