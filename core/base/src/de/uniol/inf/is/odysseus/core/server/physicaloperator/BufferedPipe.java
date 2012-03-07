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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.monitoring.StaticValueMonitoringData;

/**
 * @author Jonas Jacobi
 */
public class BufferedPipe<T extends IClone> extends AbstractIterablePipe<T, T>
		implements IBuffer<T> {

	volatile protected static Logger _logger = null;

	synchronized protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(BufferedPipe.class);
		}
		return _logger;
	}

	protected LinkedList<T> buffer = new LinkedList<T>();
	protected Lock transferLock = new ReentrantLock();
	protected AtomicReference<PointInTime> heartbeat = new AtomicReference<PointInTime>();

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

	@Override
	final protected void process_open() throws OpenFailedException {
		// super.process_open();
		this.buffer = new LinkedList<T>();
	}

	@Override
	public boolean hasNext() {
		if (!isOpen()) {
			getLogger()
					.error("hasNext call on not opened buffer! " + this + " "
							+ buffer);
			return false;
		}

		return !buffer.isEmpty() || this.heartbeat.get() != null;
	}

	@Override
	public void transferNext() {
		transferLock.lock();
		try {
			if (!this.buffer.isEmpty()) {
				// the transfer might take some time, so pop element first and
				// release lock on buffer instead of transfer(buffer.pop())
				T element;
				synchronized (this.buffer) {
					element = buffer.pop();
				}
				// logger.debug(this+" transferNext() "+element);
				transfer(element);
				if (isDone()) {
					propagateDone();
				}
			} else {
				sendPunctuation(heartbeat.getAndSet(null));
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
		synchronized (this.buffer) {
			this.buffer.add(object);
			this.heartbeat.set(null);
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
	public BufferedPipe<T> clone() {
		return new BufferedPipe<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.heartbeat.set(timestamp);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof BufferedPipe)) {
			return false;
		}
		BufferedPipe bp = (BufferedPipe) ipo;
		if (this.hasSameSources(bp)) {
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
