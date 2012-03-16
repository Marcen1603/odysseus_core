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
/**
 * 
 */
package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * @author Jonas Jacobi
 */
public class BenchmarkSink<M extends ILatency> extends
		AbstractSink<Tuple<M>> {

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
	protected synchronized void process_next(Tuple<M> object,
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

	protected void addToResult(Tuple<M> object) {
		result.add(object.getMetadata());
	}

	private void inputDone() {
		if (--counter == 0) {
			close();
			result.setEndTime(System.nanoTime());
			resultQueue.add(result);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
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
	
	public IBenchmarkResult<M> getResultObject() {
		return this.result;
	}
	
	@Override
	public BenchmarkSink<M> clone()  {
		return new BenchmarkSink<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

	public void stopRecording() {
		lock.lock();
		inputDone();
		lock.unlock();
	}

}