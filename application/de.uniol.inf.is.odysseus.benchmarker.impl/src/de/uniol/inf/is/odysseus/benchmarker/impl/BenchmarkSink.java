/**
 * 
 */
package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Jonas Jacobi
 */
class BenchmarkSink<M extends ILatency> extends
		AbstractSink<RelationalTuple<M>> {

	private Lock lock = new ReentrantLock();
	private BlockingQueue<IBenchmarkResult<M>> resultQueue = new ArrayBlockingQueue<IBenchmarkResult<M>>(1);
	private IBenchmarkResult<M> result;
	private long resultsToRead = -1;
	public int counter = 0;

	public BenchmarkSink(IBenchmarkResult<M> result, long resultsToRead) {
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
			addToResult(object);
		} else {
			lock.lock();
			inputDone();
			done(port);
			lock.unlock();
		}
	}

	protected void addToResult(RelationalTuple<M> object) {
		result.add(object.getMetadata());
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

	public IBenchmarkResult<M> waitForResult() throws InterruptedException {
		return resultQueue.take();
	}
	
	@Override
	public BenchmarkSink<M> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}


}