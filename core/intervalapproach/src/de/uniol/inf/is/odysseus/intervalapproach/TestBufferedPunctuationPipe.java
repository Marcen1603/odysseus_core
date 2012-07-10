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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterablePipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.util.LoggerHelper;

/**
 * This buffered pipe works the same way as BufferedPunctuationPipe despite of the fact
 * that punctuations will be sent after each stream element that is pushed to the following
 * operator.
 * 
 * @author Jonas Jacobi, Andr� Bolles
 */
public class TestBufferedPunctuationPipe<T extends IMetaAttributeContainer<M>, M extends ITimeInterval> extends AbstractIterablePipe<T, T>
		implements IBuffer<T> {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(BufferedPunctuationPipe.class);
		}
		return _logger;
	}
	
	protected LinkedList<T> buffer = new LinkedList<T>();
	protected Lock transferLock = new ReentrantLock();
	protected LinkedList<PointInTime> punctuations = new LinkedList<PointInTime>();

	public TestBufferedPunctuationPipe() {
		super();
		final TestBufferedPunctuationPipe<T, M> t = this;
		this.addMonitoringData("selectivity",
				new StaticValueMonitoringData<Double>(t, "selectivity", 1d));
	}

	public TestBufferedPunctuationPipe(TestBufferedPunctuationPipe<T, M> bufferedPunctuationPipe) {
		super(bufferedPunctuationPipe);
		buffer.addAll(bufferedPunctuationPipe.buffer);
		final TestBufferedPunctuationPipe<T, M> t = this;
		this.addMonitoringData("selectivity",
				new StaticValueMonitoringData<Double>(t, "selectivity", 1d));

	}

	@Override
	final protected void process_open() throws OpenFailedException {
		super.process_open();
		this.buffer = new LinkedList<T>();
	}

	@Override
	public boolean hasNext() {
		if (!isOpen()){
			getLogger().error("hasNext call on not opened buffer!");
			return false;
		}
			
		return !buffer.isEmpty() || !this.punctuations.isEmpty();
	}

	int v = 0;
	@Override
	public void transferNext() {
		transferLock.lock();
		if (this.sendElement()) {
			// the transfer might take some time, so pop element first and
			// release lock on buffer instead of transfer(buffer.pop())
			T element;
			synchronized (this.buffer) {
				element = buffer.pop();
			}
//			System.out.println("Transfer Element: " + v++);
			
			transfer(element);
			v++;
			if(v % 5 == 0){
				sendPunctuation(element.getMetadata().getStart());
			}
			if (isDone()) {
				propagateDone();
			}
		} else {
			sendPunctuation(this.punctuations.poll());
		}
		transferLock.unlock();
	}
	
	private boolean sendElement(){
		if(!this.buffer.isEmpty() && (this.punctuations.isEmpty() || 
				this.buffer.peek().getMetadata().getStart().beforeOrEquals(this.punctuations.peek()))){
			return true;
		}
		return false;
	}

	@Override
	public boolean isDone() {
		transferLock.lock();
		boolean returnValue;
		synchronized (this.buffer) {
			returnValue = super.isDone() && this.buffer.isEmpty();
		}
		transferLock.unlock();
		return returnValue;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	int i = 0;
	
	@Override
	protected void process_next(T object, int port) {
		synchronized (this.buffer) {
			i++;
			if(i % 20 == 0){
				LoggerHelper.getInstance(this.getName()).debug("Buffer size: " + this.size());
			}
			this.buffer.add(object);
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
	public void transferNextBatch(int count) {
		List<T> out;
		// FIXME fehler, weil ueber falsches objekt synchronisiert wird
		synchronized (this.buffer) {
			if (count == this.buffer.size()) {
				out = this.buffer;
				this.buffer = new LinkedList<T>();
			} else {
				out = new ArrayList<T>(count);
				if (count > size()) {
					throw new IllegalArgumentException(
							"cannot transfer more elements than size()");
				}
				for (int i = 0; i < count; ++i) {
					out.add(this.buffer.remove());
				}
			}
		}
		transfer(out);
		if (isDone()) {
			propagateDone();
		}
	}

	@Override
	public TestBufferedPunctuationPipe<T, M> clone() {
		return new TestBufferedPunctuationPipe<T, M>(this);
	}

	int p = 0;
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
//		System.out.println("Process Punc: " + p++);
		this.punctuations.addLast(timestamp);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof TestBufferedPunctuationPipe)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		TestBufferedPunctuationPipe bpp2 = (TestBufferedPunctuationPipe) ipo;
		if(this.hasSameSources(bpp2)) {
			return true;
		}
		return false;
	}

	@Override
	public T peek() {
		synchronized (this.buffer) {
			return this.buffer.peek();
		}
	}

}
