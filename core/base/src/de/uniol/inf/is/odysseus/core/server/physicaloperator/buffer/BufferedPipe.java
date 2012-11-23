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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
 * @author Jonas Jacobi
 */
public class BufferedPipe<T extends IStreamObject<?>> extends
		AbstractIterablePipe<T, T> implements IBuffer<T> {

	volatile protected static Logger _logger = null;

	synchronized protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(BufferedPipe.class);
		}
		return _logger;
	}

	protected LinkedList<IStreamable> buffer = new LinkedList<>();
	protected Lock transferLock = new ReentrantLock();
	private String buffername;

	public BufferedPipe() {
		super();
		final BufferedPipe<T> t = this;
		this.addMonitoringData("selectivity",
				new StaticValueMonitoringData<Double>(t, "selectivity", 1d));
	}

	public BufferedPipe(BufferedPipe<T> bufferedPipe) {
		super(bufferedPipe);
		buffer.addAll(bufferedPipe.buffer);
		final BufferedPipe<T> t = this;
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
		if (!isOpen()) {
			getLogger()
					.error("hasNext call on not opened buffer! " + this + " "
							+ buffer);
			return false;
		}

		return !buffer.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void transferNext() {
		transferLock.lock();
		try {
			if (!this.buffer.isEmpty()) {
				// the transfer might take some time, so pop element first and
				// release lock on buffer instead of transfer(buffer.pop())
				IStreamable element;
				synchronized (this.buffer) {
					element = buffer.pop();
				}
				if (element.isPunctuation()) {
					sendPunctuation((IPunctuation)element);
				} else {
					transfer((T)element);
				}
				if (isDone()) {
					propagateDone();
				}
			}
		} finally {
			transferLock.unlock();
		}
	}

	@Override
	public boolean isDone() {
		transferLock.lock();
		try {
			boolean returnValue;
			synchronized (this.buffer) {
				returnValue = super.isDone() && this.buffer.isEmpty();
			}
			return returnValue;
		} finally {
			transferLock.unlock();
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		transferLock.lock();
		this.buffer.add(object);
		transferLock.unlock();
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferLock.lock();
		this.buffer.add(punctuation);
		transferLock.unlock();
	}

	@Override
	public int size() {
		return this.buffer.size();
	}

	public boolean isEmpty() {
		return this.buffer.isEmpty();
	}

	@Override
	public BufferedPipe<T> clone() {
		return new BufferedPipe<T>(this);
	}



	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof BufferedPipe)) {
			return false;
		}
		BufferedPipe bp = (BufferedPipe) ipo;
		if (this.hasSameSources(bp) && this.buffername.equals(bp.buffername)) {
			return true;
		}
		return false;
	}

	@Override
	public IStreamable peek() {
		transferLock.lock();
		IStreamable p = this.buffer.peek();
		transferLock.unlock();
		return p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IMetaAttribute peekMetadata(){
		transferLock.lock();
		int i = buffer.size() -1;
		IStreamable p = null;
		// Find first non punctuation
		while (i>=0 && (p = this.buffer.get(i)).isPunctuation()  ){
			i--;
		}
		
		IMetaAttribute meta = null;
		if (p != null & !p.isPunctuation()){
			meta = ((T)p).getMetadata();
		}
		transferLock.unlock();
		return meta;		
	}
	
	
}
