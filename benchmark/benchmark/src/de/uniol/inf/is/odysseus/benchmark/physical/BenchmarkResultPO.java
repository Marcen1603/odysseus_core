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
/**
 * 
 */
package de.uniol.inf.is.odysseus.benchmark.physical;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.benchmark.result.IBenchmarkResult;
import de.uniol.inf.is.odysseus.benchmark.result.IBenchmarkResultFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class BenchmarkResultPO<M extends ILatency> extends
		AbstractPipe<Tuple<M>, IBenchmarkResult<M>> {

	private Lock lock = new ReentrantLock();
	private IBenchmarkResultFactory<M> resultFactory;
	private Map<Integer, IBenchmarkResult<M>> result = new HashMap<>();
	private long resultsToRead = -1;

	public BenchmarkResultPO(IBenchmarkResultFactory<M> resultFactory,
			long resultsToRead) {
		this.resultFactory = resultFactory;
		this.resultsToRead = resultsToRead;
	}

	public BenchmarkResultPO(BenchmarkResultPO<M> old) {
		this.lock = old.lock;
		this.resultFactory = old.resultFactory;
		this.resultsToRead = old.resultsToRead;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public synchronized void process_done(int port) {
		lock.lock();
		inputDone(port);
		lock.unlock();
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_close()
	 */
	@Override
	protected void process_close() {	
		
	}

	int i = 0;
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#prepare_close()
	 */
	@Override
	protected void prepare_close() {
		for(Integer port : result.keySet()){
			result.get(port).setEndTime(System.nanoTime());
			transfer(result.get(port), port);
		}
	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		if (isOpen()) {
			if (resultsToRead == -1 || result.get(port).size() < resultsToRead) {
				result.get(port).add(object.getMetadata());
			} else {
				lock.lock();
				inputDone(port);
				done(port);
				lock.unlock();
			}
		}
	}

	private void inputDone(int port) {
		result.get(port).setEndTime(System.nanoTime());
		transfer(result.get(port), port);
		close();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		result.clear();
		for (PhysicalSubscription<?> s : getSubscribedToSource()) {
			result.put(s.getSinkInPort(), resultFactory.createBenchmarkResult());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

	@Override
	public BenchmarkResultPO<M> clone() {
		return new BenchmarkResultPO<M>(this);
	}

}