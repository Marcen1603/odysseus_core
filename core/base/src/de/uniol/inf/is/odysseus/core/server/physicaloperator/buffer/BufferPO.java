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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterablePipe;

/**
 * This operator buffers elements. It buffers real stream elements and
 * punctuations. The top element is always a real stream element, punctuations
 * are send if they are on the top of the buffer.
 * 
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public class BufferPO<T extends IStreamObject<?>> extends
		AbstractIterablePipe<T, T> implements IBuffer<T> {

	volatile protected static Logger _logger = null;

	synchronized protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(BufferPO.class);
		}
		return _logger;
	}

	protected LinkedList<IStreamable> buffer = new LinkedList<>();
	private String buffername;

	public BufferPO() {
		super();
		final BufferPO<T> t = this;
		this.addMonitoringData("selectivity",
				new StaticValueMonitoringData<Double>(t, "selectivity", 1d));
	}

	public BufferPO(BufferPO<T> bufferedPipe) {
		super(bufferedPipe);
		buffer.addAll(bufferedPipe.buffer);
		final BufferPO<T> t = this;
		this.addMonitoringData("selectivity",
				new StaticValueMonitoringData<Double>(t, "selectivity", 1d));

	}

	public void setBufferName(String buffername) {
		this.buffername = buffername;
	}

	@Override
	protected void process_close() {
		synchronized (buffer) {
			buffer.clear();
		}
	}

	@Override
	final protected void process_open() throws OpenFailedException {
		// super.process_open();
		this.buffer.clear();
	}

	@Override
	public boolean hasNext() {

		// Its not possible
		// if (!isOpen()) {
		// getLogger()
		// .error("hasNext call on not opened buffer! " + this + " "
		// + buffer);
		// return false;
		// }

		return !buffer.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void transferNext() {
		if (!this.buffer.isEmpty()) {
			// the transfer might take some time, so pop element first and
			// release lock on buffer instead of transfer(buffer.pop())
			IStreamable element;
			synchronized (this.buffer) {
				element = buffer.pop();
			}

			if (element.isPunctuation()) {
				sendPunctuation((IPunctuation) element);
			} else {
				transfer((T) element);
			}

			// the top element of a buffer must always be
			// an real element, send punctuations immediately
			synchronized (buffer) {
				while (buffer.peek().isPunctuation()) {
					sendPunctuation((IPunctuation) buffer.pop());
				}
			}

			if (isDone()) {
				propagateDone();
			}
		}
	}

	@Override
	public boolean isDone() {
		boolean returnValue;
		synchronized (this.buffer) {
			returnValue = super.isDone() && this.buffer.isEmpty();
		}
		return returnValue;

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		synchronized (buffer) {
			this.buffer.add(object);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		boolean sendDirectly = false;
		synchronized (buffer) {
			// Punctuations on top of buffer should be send immediately
			// --> do not add punctuation to an empty buffer
			if (!buffer.isEmpty()) {
				this.buffer.add(punctuation);
			} else {
				sendDirectly = true;
			}
		}
		// Avoid Sending of punctuations is inside the buffer lock ...
		if (sendDirectly) {
			sendPunctuation(punctuation);
		}

	}

	@Override
	public int size() {
		return this.buffer.size();
	}

	public boolean isEmpty() {
		return this.buffer.isEmpty();
	}

	@Override
	public BufferPO<T> clone() {
		return new BufferPO<T>(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof BufferPO)) {
			return false;
		}
		BufferPO bp = (BufferPO) ipo;
		if (this.hasSameSources(bp) && this.buffername.equals(bp.buffername)) {
			return true;
		}
		return false;
	}

	@Override
	public IStreamable peek() {
		IStreamable p = null;
		synchronized (buffer) {
			p = this.buffer.peek();
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IMetaAttribute peekMetadata() {
		IStreamable p = null;
		synchronized (buffer) {
			p = buffer.peek();
		}
		IMetaAttribute meta = null;
		if (p != null & !p.isPunctuation()) {
			meta = ((T) p).getMetadata();
		}
		return meta;
	}

}
