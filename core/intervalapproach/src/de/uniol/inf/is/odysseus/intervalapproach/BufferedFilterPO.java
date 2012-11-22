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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;

public class BufferedFilterPO<K extends ITimeInterval, R extends IStreamObject<K>> extends AbstractPipe<R, R> implements IHasMetadataMergeFunction<K>{

	private static final int BUFFERPORT = 0;

	private final IPredicate<? super R> predicate;
	private final List<R> buffer = new LinkedList<R>();
	private final long bufferTime;
	private final long deliverTime;
	private PointInTime deliverUntil = null;

	final IDataMergeFunction<R, K> dataMerge;
	final IMetadataMergeFunction<K> metadataMerge;

	private R trigger = null;

	public BufferedFilterPO(IPredicate<? super R> predicate, long bufferTime, long deliverTime, IDataMergeFunction<R, K> dataMerge, IMetadataMergeFunction<K> metadataMerge) {
		super();
		this.predicate = predicate.clone();
		this.bufferTime = bufferTime;
		this.deliverTime = deliverTime;
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
	}

	public BufferedFilterPO(BufferedFilterPO<K, R> pipe) {
		super(pipe);
		this.predicate = pipe.predicate.clone();
		this.bufferTime = pipe.bufferTime;
		this.deliverTime = pipe.deliverTime;
		this.dataMerge = pipe.dataMerge.clone();
		this.metadataMerge = pipe.metadataMerge.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		buffer.clear();
		deliverUntil = null;
	}

	@Override
	protected void process_done() {
		// TODO: What to do in this case? Empty buffer?
	}

	@Override
	protected void process_next(R object, int port) {
		synchronized (buffer) {
			if (port == BUFFERPORT) {
				buffer.add(object);
			} else {
				if (predicate.evaluate(object)) {
					// Set output time
					deliverUntil = PointInTime.plus(object.getMetadata().getStart(), deliverTime);
					// Set trigger object, that will be appended
					trigger = object;
				}
			}
			produceData(object.getMetadata().getStart());
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		produceData(punctuation.getTime());
		sendPunctuation(punctuation);
	}

	private void produceData(PointInTime timestamp) {
		synchronized (buffer) {
			// 1. Remove outdated values
			R elem;
			boolean bufferCleard = false;
			while (buffer.size() > 0 && !bufferCleard) {
				elem = buffer.get(0);
				if (elem.getMetadata().getStart().getMainPoint() + bufferTime < timestamp.getMainPoint()) {
					// Send filtered data to output port 1
					transfer(buffer.remove(0), 1);
					continue;
				} else {
					bufferCleard = true;
				}
			}

			// 2. Produce values if there are any
			if (deliverUntil != null && buffer.size() > 0) {
				R toTest = buffer.get(0);
				while (toTest != null && toTest.getMetadata().getStart().beforeOrEquals(deliverUntil)) {
					R toTransfer = dataMerge.merge(trigger, toTest, metadataMerge, Order.LeftRight);
					transfer(toTransfer);
					buffer.remove(0);
					toTest = buffer.size() > 0? buffer.get(0):null;
				}
			}
		}
	}


	@Override
	public BufferedFilterPO<K, R> clone() {
		return new BufferedFilterPO<K, R>(this);
	}

	public IMetadataMergeFunction<K> getMetadataMerge() {
		return metadataMerge;
	}

}
