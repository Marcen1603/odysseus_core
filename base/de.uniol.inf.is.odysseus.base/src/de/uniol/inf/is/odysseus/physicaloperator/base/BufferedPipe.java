package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.monitoring.StaticValueMonitoringData;

/**
 * @author Jonas Jacobi
 */
public class BufferedPipe<T extends IClone> extends AbstractIterablePipe<T, T>
		implements IBuffer<T> {

	protected LinkedList<T> buffer = new LinkedList<T>();
	private Lock transferLock = new ReentrantLock();

	public BufferedPipe() {
		super();
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
	final public boolean hasNext() {
		if (!isOpen())
			return false;
		return !buffer.isEmpty();
	}

	@Override
	public void transferNext() {
		transferLock.lock();
		T element;
		// the transfer might take some time, so pop element first and release
		// lock on buffer instead of transfer(buffer.pop())
		synchronized (this.buffer) {
			element = buffer.pop();
		}
		transfer(element);
		if (isDone()) {
			propagateDone();
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

	@Override
	protected synchronized void process_next(T object, int port) {
		synchronized (this.buffer) {
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
		//FIXME fehler, weil ueber falsches objekt synchronisiert wird
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
	public BufferedPipe<T> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	
}
