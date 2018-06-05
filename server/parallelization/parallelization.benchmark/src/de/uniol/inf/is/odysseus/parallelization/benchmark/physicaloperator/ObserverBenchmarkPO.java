/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.parallelization.benchmark.data.BenchmarkPOObservable;

/**
 * Physical operator for ObserverBenchmark. Do evaluations until goal is reached
 * and inform all observers
 * 
 * @author ChrisToenjesDeye
 *
 * @param <T>
 */
public class ObserverBenchmarkPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	// we need a delegate, because it is not possible to inherit from Observable
	// (no multi inheritance in java)
	private BenchmarkPOObservable<T> delegate;

	public ObserverBenchmarkPO() {
		super();
		this.delegate = new BenchmarkPOObservable<T>();
	}

	public ObserverBenchmarkPO(ObserverBenchmarkPO<T> observerCounterPO) {
		super();
		this.delegate = new BenchmarkPOObservable<T>();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// do evaluation and transfer object
		delegate.evaluate(object);
		transfer(object);
	}

	@Override
	protected void process_done() {
		delegate.evaluationDone();
		super.process_done();
	}

}
