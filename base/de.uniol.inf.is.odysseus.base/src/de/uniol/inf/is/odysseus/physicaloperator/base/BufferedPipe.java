package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;
import de.uniol.inf.is.odysseus.util.LoggerHelper;

/**
 * @author Jonas Jacobi
 */
public class BufferedPipe<T extends IClone> extends AbstractIterablePipe<T, T>
		implements IBuffer<T> {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
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
		super.process_open();
		this.buffer = new LinkedList<T>();
	}

	@Override
	public boolean hasNext() {
		if (!isOpen()){
			getLogger().error("hasNext call on not opened buffer!");
			return false;
		}
			
		return !buffer.isEmpty() || this.heartbeat.get() != null;
	}

	@Override
	public void transferNext() {
		transferLock.lock();
		if (!this.buffer.isEmpty()) {
			// the transfer might take some time, so pop element first and
			// release lock on buffer instead of transfer(buffer.pop())
			T element;
			synchronized (this.buffer) {
				element = buffer.pop();
			}
			//logger.debug(this+" transferNext() "+element);
			transfer(element);
			if (isDone()) {
				propagateDone();
			}
		} else {
			sendPunctuation(heartbeat.getAndSet(null));
		}
		transferLock.unlock();
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

}
