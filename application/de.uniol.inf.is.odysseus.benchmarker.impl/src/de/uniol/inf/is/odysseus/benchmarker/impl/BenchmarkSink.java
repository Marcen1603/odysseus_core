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
public class BenchmarkSink<M extends ILatency> extends
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
	
	public BenchmarkSink(BenchmarkSink<M> old){
		this.lock = old.lock;
		this.resultQueue = old.resultQueue;
		this.result = old.result;
		this.resultsToRead = old.resultsToRead;
		this.counter = old.counter;
	}

	@Override
	public synchronized void process_done(int port) {
		lock.lock();
		inputDone();
		lock.unlock();
	}

	int i = 0;
	@Override
	protected synchronized void process_next(RelationalTuple<M> object,
			int port, boolean isReadOnly) {
		if (resultsToRead == -1 || result.size() < resultsToRead) {
			addToResult(object);
//			if(i % 20 == 0){
//				System.out.println("added result no " + i);
//			}
//			i++;
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
		return new BenchmarkSink<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}


}