/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;

public class AvgProcessingTime extends AbstractMonitoringData<Double> implements
		IPOEventListener {

	private long start = 0;
	private long lastRun = -1;
	private double runSum = 0;
	private long runCount = 0;

	public AvgProcessingTime(){
		super(MonitoringDataTypes.AVERAGE_PROCESSING_TIME.name);
	}
	
	public AvgProcessingTime(IPhysicalOperator target) {
		super(target, MonitoringDataTypes.AVERAGE_PROCESSING_TIME.name);
	}

	public AvgProcessingTime(AvgProcessingTime avgProcessingTime) {
		super(avgProcessingTime);
		this.start = avgProcessingTime.start;
		this.lastRun = avgProcessingTime.lastRun;
		this.runSum = avgProcessingTime.runSum;
		this.runCount = avgProcessingTime.runCount;
	}
	
	public void setTarget(IPhysicalOperator target) {
		super.setTarget(target);
		target.subscribe(this, POEventType.ProcessInit);
		target.subscribe(this, POEventType.ProcessDone);
	}

	@Override
	public void eventOccured(IEvent<?,?> poEvent) {
		if (poEvent.getEventType().equals(POEventType.ProcessInit)) {
			start = System.nanoTime();
		} else {
			lastRun = System.nanoTime() - start;
			runCount++;
			runSum += lastRun;
		}
	}

	@Override
	public Double getValue() {
		if (runCount > 0) {
			return runSum / runCount;
		}
		return null;
	}

	@Override
	public void reset() {
		start = 0;
		lastRun = -1;
		runSum = 0;
		runCount = 0;
	}

	@Override
	public AvgProcessingTime clone() {
		return new AvgProcessingTime(this);
	}

}
