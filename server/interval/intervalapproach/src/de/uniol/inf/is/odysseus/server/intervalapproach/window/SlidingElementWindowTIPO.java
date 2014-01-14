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
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;

public class SlidingElementWindowTIPO<T extends IStreamObject<ITimeInterval>>
		extends AbstractWindowTIPO<T> {

	Logger LOG = LoggerFactory.getLogger(SlidingElementWindowTIPO.class);

	private IGroupProcessor<T, T> groupProcessor = new NoGroupProcessor<T, T>();

	private ITransferArea<T, T> transferArea = new TITransferArea<>();

	final private Map<Long, List<T>> buffers = new HashMap<>();
	boolean forceElement = true;
	private final long advance;

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
			long bufferId = groupProcessor.getGroupID(object);
			List<T> buffer = buffers.get(bufferId);
			if (buffer == null) {
				buffer = new LinkedList<T>();
				buffers.put(bufferId, buffer);
			}
			buffer.add(object);
			process(buffer, object);
		}
		transferArea.newElement(object, port);
	}

	private void process(List<T> buffer, T object) {
		synchronized (buffer) {
			// test if buffer has reached limit
			if (buffer.size() == (this.windowSize + 1)) {

				long elemsToSend = advance;
				// TODO: Problem: Window size smaller than advance
				// if (windowSize < windowAdvance) {
				// elemsToSend = windowSize;
				// }

				transferBuffer(buffer, elemsToSend, object);

			}
		}
	}

	private void transferBuffer(List<T> buffer, long numberofelements, T object) {
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
				toReturn.getMetadata().setEnd(object.getMetadata().getStart());
				transferArea.transfer(toReturn);
			}
		}
	}

	@Override
	protected void process_done() {
		synchronized (buffers) {
			for (List<T> b : buffers.values()) {
				transferArea.transfer(b);
			}
		}
	}

	@Override
	public void process_close() {
		synchronized (buffers) {
			for (List<T> b : buffers.values()) {
				b.clear();
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferArea.sendPunctuation(punctuation, port);
		transferArea.newElement(punctuation, port);
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

}
