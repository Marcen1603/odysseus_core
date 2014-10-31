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
package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.state.SlidingElementWindowTIPOState;

public class SlidingElementWindowTIPO<T extends IStreamObject<ITimeInterval>>
		extends AbstractWindowTIPO<T> implements IStatefulPO {

	Logger LOG = LoggerFactory.getLogger(SlidingElementWindowTIPO.class);

	private IGroupProcessor<T, T> groupProcessor = new NoGroupProcessor<T, T>();

	private ITransferArea<T, T> transferArea = new TITransferArea<>();

	private Map<Long, List<T>> buffers = new HashMap<>();
	
	boolean forceElement = true;
	private final long advance;
	private PointInTime lastTs = null;

	public SlidingElementWindowTIPO(AbstractWindowAO ao) {
		super(ao);
		advance = windowAdvance > 0 ? windowAdvance : 1;
		if (windowSize < advance) {
			throw new IllegalArgumentException(
					"Sorry. Size < Advance currently not implemented!");
		}
	}

	public void setGroupProcessor(IGroupProcessor<T, T> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_open() {
		buffers.clear();
		groupProcessor.init();
		transferArea.init(this, 1);
	}

	@Override
	protected void process_next(T object, int port) {
		synchronized (buffers) {
			lastTs = object.getMetadata().getStart();
			long bufferId = groupProcessor.getGroupID(object);
			List<T> buffer = buffers.get(bufferId);
			if (buffer == null) {
				buffer = new LinkedList<T>();
				buffers.put(bufferId, buffer);
			}
			buffer.add(object);
			process(buffer, bufferId, lastTs);
		}
		// Do not call:
		// transferArea.newElement(object, port);
		// Determine min element in transferBuffer and send hearbeat
	}

	private PointInTime getMinTs() {
		// MinTs is the oldest element of all buffers
		// because the buffers are sorted by time, only the first element has to
		// be treated
		if (buffers.size() == 0) {
			return null;
		}
		PointInTime min = null;
		for (List<T> buffer : buffers.values()) {
			T e = buffer.get(0);
			PointInTime test = e.getMetadata().getStart();
			if (min == null || min.after(test)) {
				min = test;
			}
		}
		return min;
	}

	private void process(List<T> buffer, Long bufferId, PointInTime ts) {
		synchronized (buffer) {
			// test if buffer has reached limit
			if (buffer.size() == this.windowSize + 1) {

				long elemsToSend = advance;
				// TODO: Problem: Window size smaller than advance
				// if (windowSize < windowAdvance) {
				// elemsToSend = windowSize;
				// }

				transferBuffer(buffer, elemsToSend, ts);

				// We need to determine the oldest element in all buffers and
				// send a punctuation to the transfer area
				ping();

				if (buffer.size() == 0) {
					buffers.remove(bufferId);
				}
			}
		}
	}

	private void transferBuffer(List<T> buffer, long numberofelements,
			PointInTime ts) {
		synchronized (buffer) {
			Iterator<T> bufferIter = buffer.iterator();

			PointInTime start = null;
			if (usesSlideParam) {
				// keep start time of first element
				T elem = buffer.get(0);
				start = elem.getMetadata().getStart();
			}
			for (int i = 0; i < numberofelements; i++) {
				T toReturn = bufferIter.next();
				bufferIter.remove();
				// If slide param is used give all elements of the window
				// the same start timestamp
				if (usesSlideParam) {
					toReturn.getMetadata().setStart(start);
				}
				// We can produce tuple with no validity --> Do not send them
				if (toReturn.getMetadata().getStart().before(ts)) {
					toReturn.getMetadata().setEnd(ts);
					transferArea.transfer(toReturn);
				}
			}
		}
	}

	public void ping() {
		PointInTime minTs = getMinTs();
		if (minTs != null) {
			transferArea.newHeartbeat(minTs, 0);
		}
	}

	@Override
	protected void process_done() {
		synchronized (buffers) {
			for (List<T> b : buffers.values()) {
                if (b.size() > 0) {
                	// Transfer elements in buffers
                	// use lastTs+1 as timestamp for the last element (else the last element
                	// will be removed)
                    transferBuffer(b, b.size(), lastTs.plus(1));
                    for (T t : b) {
                        transferArea.transfer(t);
                    }
                }
			}
		}
		transferArea.newHeartbeat(lastTs, 0);
	}

	@Override
	public void process_close() {
		process_done();
		synchronized (buffers) {
			for (List<T> b : buffers.values()) {
				b.clear();
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferArea.sendPunctuation(punctuation, port);
	}

	@Override
	public long getElementsStored1() {
		long size = 0;
		Collection<List<T>> bufs = buffers.values();
		for (List<T> b : bufs) {
			size += b.size();
		}
		return size;
	}

	@Override
	public AbstractPipe<T, T> clone() {
		throw new IllegalArgumentException("Currently not implemented!");
	}

	@Override
	public Serializable getState() {
		SlidingElementWindowTIPOState<T> state = new SlidingElementWindowTIPOState<T>();
		state.setBuffers(buffers);
		state.setGroupProcessor(groupProcessor);
		state.setLastTs(lastTs);
		state.setTransferArea(transferArea);
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable serializable) {
		try {
			SlidingElementWindowTIPOState<T> state = (SlidingElementWindowTIPOState<T>) serializable;
			buffers = state.getBuffers();
			groupProcessor = state.getGroupProcessor();
			lastTs = state.getLastTs();
			transferArea = state.getTransferArea();
			transferArea.setTransfer(this);			
		} catch (Throwable T) {
			LOG.error("The serializable state to set for the SlidingElementWindowTIPO is not a valid SlidingElementWindowTIPOState!");
		}
	}

}
