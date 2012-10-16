/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringData;

public class MedianProcessingTime extends AbstractMonitoringData<Double> implements IPOEventListener {

	private static final int MAX_DATA = 80;
	private long startTimestampNano = -1;
	private long endTimestampNano = -1;
	private int counterCheck = 0;
	
	private long runSum = 0;
	
	private List<Long> runTimes = new LinkedList<Long>();
	private long initValue;
	private IPhysicalOperator subscribedTarget;

	public MedianProcessingTime(IPhysicalOperator target, long initValue) {
		super(target, MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
		subscribeToTarget(target);
		initValues(initValue);
	}

	public MedianProcessingTime(IPhysicalOperator target) {
		this(target, 5000000);
	}
	
	public MedianProcessingTime(MedianProcessingTime avgProcessingTime) {
		super(avgProcessingTime);
		subscribeToTarget((IPhysicalOperator)avgProcessingTime.getTarget());
		this.startTimestampNano = avgProcessingTime.startTimestampNano;
		this.endTimestampNano = avgProcessingTime.endTimestampNano;
		this.runSum = avgProcessingTime.runSum;
		this.runTimes = new LinkedList<Long>(avgProcessingTime.runTimes);
		this.initValue = avgProcessingTime.initValue;
		this.counterCheck = avgProcessingTime.counterCheck;
	}

	public void subscribeToTarget(IPhysicalOperator target) {
		if(subscribedTarget != null ) {
			subscribedTarget.unsubscribe(this, POEventType.ProcessInit);
			subscribedTarget.unsubscribe(this, POEventType.PushInit);
			subscribedTarget.unsubscribe(this, POEventType.ProcessDone);
		}
		subscribedTarget = target;
		subscribedTarget.subscribe(this, POEventType.ProcessInit);
		subscribedTarget.subscribe(this, POEventType.PushInit);
		subscribedTarget.subscribe(this, POEventType.ProcessDone);
	}

	@Override
	public void eventOccured(IEvent<?, ?> poEvent, long eventNanoTime) {
		synchronized(runTimes) {
			IEventType type = poEvent.getEventType();
			if (type.equals(POEventType.ProcessInit)) {
				if( counterCheck > 0) {
					throw new RuntimeException("ProcessInit without PushInit/ProcessDone before in Operator " + subscribedTarget);
				}
				startTimestampNano = eventNanoTime;
				counterCheck++;
			} else if (type.equals(POEventType.PushInit) || type.equals(POEventType.ProcessDone) ) {
				if( startTimestampNano == -1 ) {
					return;
				}
				endTimestampNano = eventNanoTime;
				counterCheck--;
				if( counterCheck > 0 ) {
					throw new RuntimeException(type.toString() + " without ProcessInit in Operator " + subscribedTarget);
				}
				
				Long lastRun = new Long( endTimestampNano - startTimestampNano );
				endTimestampNano = -1;
				startTimestampNano = -1;
				
				runTimes.add(lastRun);
				runSum += lastRun;
				
				Long v = runTimes.remove(0);
				runSum -= v;
			}
		}
	}

	@Override
	public Double getValue() {
		synchronized(runTimes) {
			return (double)runSum / MAX_DATA;
		}
	}

	@Override
	public void reset() {
		startTimestampNano = -1;
		endTimestampNano = -1;
		counterCheck = 0;
		initValues(this.initValue);
	}

	@Override
	public MedianProcessingTime clone() {
		return new MedianProcessingTime(this);
	}

	private void initValues(long initValue) {
		runTimes.clear();
		for( int i = 0; i < MAX_DATA; i++ ) {
			runTimes.add(initValue);
		}
		runSum = MAX_DATA * initValue;
		
		this.initValue = initValue;		
	}

}
