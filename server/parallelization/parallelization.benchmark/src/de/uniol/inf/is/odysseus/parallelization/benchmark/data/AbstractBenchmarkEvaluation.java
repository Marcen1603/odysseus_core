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
package de.uniol.inf.is.odysseus.parallelization.benchmark.data;

import java.util.Observable;

/**
 * Abstract class for benchmark evaluations. This evaluations works only with
 * observers (if the evaluatin goal is reached, all observers are informed)
 * 
 * @author ChrisToenjesDeye
 *
 */
public abstract class AbstractBenchmarkEvaluation implements
		IBenchmarkEvaluation {

	private IBenchmarkObserver benchmarkObserver;

	public AbstractBenchmarkEvaluation(IBenchmarkObserver benchmarkObserver) {
		this.benchmarkObserver = benchmarkObserver;
	}

	/**
	 * informs all registered observers
	 */
	public void updateObserver(Observable observable, Object arg) {
		benchmarkObserver.update(observable, arg);
	}
}
