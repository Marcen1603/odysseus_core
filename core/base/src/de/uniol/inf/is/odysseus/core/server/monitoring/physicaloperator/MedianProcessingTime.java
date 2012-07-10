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
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.event.POEventType;

public class MedianProcessingTime extends AbstractMonitoringData<Double> implements IPOEventListener {

	private static double granularity;
	private static final int MAX_DATA = 50;
	private long start1 = 0;
	private long end1 = 0;
	
	private long runSum = 0;
	private long runCount = 0;
	
	static {
		long first = System.nanoTime();
		while( true ) {
			double second = System.nanoTime();
			if( second > first ) {
				granularity = second - first;
				break;
			}
		}
	}
	
	private List<Long> runTimes = new LinkedList<Long>();
	private List<Long> sorted = new LinkedList<Long>();

	public MedianProcessingTime(IPhysicalOperator target) {
		super(target, MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
		setTarget(target);
	}

	public MedianProcessingTime(MedianProcessingTime avgProcessingTime) {
		super(avgProcessingTime);
		this.start1 = avgProcessingTime.start1;
		this.end1 = avgProcessingTime.end1;
		this.runSum = avgProcessingTime.runSum;
		this.runTimes = avgProcessingTime.runTimes;
		this.runCount = avgProcessingTime.runCount;
	}

	public void setTarget(IPhysicalOperator target) {
		super.setTarget(target);
		target.subscribe(this, POEventType.ProcessInit);
		target.subscribe(this, POEventType.PushInit);
	}

	@Override
	public void eventOccured(IEvent<?, ?> poEvent, long eventNanoTime) {
		if (poEvent.getEventType().equals(POEventType.ProcessInit)) {
			start1 = eventNanoTime;
		} else if (poEvent.getEventType().equals(POEventType.PushInit)) {
			end1 = eventNanoTime;
			
			Long lastRun = new Long( end1 - start1 );
			if( lastRun < granularity )
				lastRun = (long) granularity;
			
			runTimes.add(lastRun);
			runSum += lastRun;
			runCount++;

			// add new value in sorted list
			if (!sorted.isEmpty()) {
				ListIterator<Long> iterator = sorted.listIterator();
				boolean added = false;
				while (iterator.hasNext()) {
					Long v = iterator.next();
					if (v >= lastRun) {
						iterator.previous();
						iterator.add(lastRun);
						iterator.next();
						added = true;
						break;
					}
				}
				// end of list?
				if (!added && !iterator.hasNext())
					sorted.add(lastRun);

			} else {
				// list is empty
				sorted.add(lastRun);
			}

			
			if( runTimes.size() > MAX_DATA ) {
				Long v = runTimes.remove(0);
				runSum -= v;
				sorted.remove(v);
			}
		}
	}

	@Override
	public Double getValue() {
		if ( !sorted.isEmpty() && runTimes.size() > MAX_DATA / 2 )
			return new Double(sorted.get(sorted.size() / 2));
		return null;
	}

	@Override
	public void reset() {
		start1 = 0;
		end1 = 0;
		runCount = 0;
		runTimes = new LinkedList<Long>();
	}

	@Override
	public MedianProcessingTime clone() {
		return new MedianProcessingTime(this);
	}

}
