/**
 * 
 */
package de.uniol.inf.is.odysseus.benchmarker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

class MySink<M extends ILatency & IPriority> extends
		AbstractSink<RelationalTuple<M>> {

	private Lock lock = new ReentrantLock();
	private BlockingQueue<BenchmarkResult> resultQueue = new ArrayBlockingQueue<BenchmarkResult>(1);
	private BenchmarkResult result;
	private long resultsToRead = -1;
	public int counter = 0;

	public MySink(BenchmarkResult result, long resultsToRead) {
		this.result = result;
		this.resultsToRead = resultsToRead;
	}

	@Override
	public synchronized void process_done(int port) {
		lock.lock();
		inputDone();
		lock.unlock();
	}

	@Override
	protected synchronized void process_next(RelationalTuple<M> object,
			int port, boolean isReadOnly) {
		if (resultsToRead == -1 || result.size() < resultsToRead) {
			result.add(object.getMetadata());
		} else {
			lock.lock();
			inputDone();
			done(port);
			lock.unlock();
		}
	}

	private void inputDone() {
		if (--counter == 0) {
			close();
			resultQueue.add(result);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
	}
	@Override
	protected void setInputPortCount(int ports) {
		super.setInputPortCount(ports);
		lock.lock();
		counter = ports;
		lock.unlock();
	}

	public BenchmarkResult waitForResult() throws InterruptedException {
		return resultQueue.take();
	}

}