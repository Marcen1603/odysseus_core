/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator is useful for out-of-order processing. It sends a watermark (as
 * heartbeats / punctuations) which lags behind the application time. Together
 * with the AssureOrder-operator this can be used to bring the elements back to
 * the right order.
 * 
 * Simple example: timespan = 10 minutes; When an element arrives at 12:00 (and
 * no other later element arrived before), a heartbeat with time 11:50 is send.
 * Now the AssureOrder-operator knows that it can transfer all elements until
 * 11:50 because it is guaranteed that no element will arrive earlier (later
 * incoming out-of-order elements could for example be dropped).
 * 
 * @author Cornelius Ludmann, Tobias Brandt, Marco Grawunder
 *
 */
public class WatermarkPO<R extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<R, R> {

	private static final Logger LOG = LoggerFactory.getLogger(WatermarkPO.class);

	// The time span to lag behind the application time
	private final long timespan;

	// The current watermark -> the current time minus the timespan
	private long watermark = 0;

	// If set to true, all elements that are older than any send watermark will be removed
	private final boolean removeOutdated;
	
	/**
	 * Creates a new watermark operator.
	 * 
	 * @param timespan The time span the watermarkt / heartbeats have to lag behind
	 */
	public WatermarkPO(TimeValueItem dragBegindTime, TimeUnit baseTimeUnit, boolean removeOutdated) {
		long timeValue = dragBegindTime.getTime();
		TimeUnit unit = dragBegindTime.getUnit();
		this.timespan = baseTimeUnit.convert(timeValue, unit);
		this.removeOutdated = removeOutdated;
	}

	@Override
	protected void process_next(R object, int port) {
		if (removeOutdated && object.getMetadata().getStart().getMainPoint() < watermark) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Removed outdated element " + object);
			}
		}else {
			transfer(object, port);
			// Send a water mark that lags behind the given amount of time
			sendWatermarkHeartbeat(object.getMetadata().getStart(), port);
		} 
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		/*
		 * do not redirect other heartbeats directly that would disturb the subsequent
		 * operators that use the heardbeats produced by this operator
		 */
		if (!punctuation.isHeartbeat()) {
			sendPunctuation(punctuation, port);
		}
		sendWatermarkHeartbeat(punctuation.getTime(), port);
	}

	/**
	 * Send a heartbeat that is the globally defined timespan behind
	 * 
	 * @param time current time, e.g. from another heartbeat or a tuple
	 * @param port the port from which the input came is used for the output as well
	 */
	private synchronized void sendWatermarkHeartbeat(PointInTime time, int port) {
		/*
		 * if the new element minus the timespan is older than our last watermark (out
		 * of order) we will use the watermark as we guarantee that the watermark is in
		 * order
		 */

		if (currentElementExpandsWatermark(time, watermark)) {
			watermark = calculateWatermarkFromCurrentElement(time);
			sendPunctuation(Heartbeat.createNewHeartbeat(watermark), port);
		}
	}
	
	private boolean currentElementExpandsWatermark(PointInTime currentTime, long watermark) {
		long watermarkFromCurrentElement = calculateWatermarkFromCurrentElement(currentTime);
		return watermarkFromCurrentElement > watermark;
	}
	
	private long calculateWatermarkFromCurrentElement(PointInTime currentTime) {
		return currentTime.getMainPoint() - timespan;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.watermark = Long.MIN_VALUE;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof WatermarkPO)) {
			return false;
		}
		WatermarkPO<?> other = (WatermarkPO<?>) ipo;
		return other.timespan == timespan;
	}
}
