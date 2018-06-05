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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * Delegate class needed in benchmarkObserverPO. This delegate is needed because
 * java does not support multi inheritance
 * 
 * @author ChrisToenjesDeye
 */
public class BenchmarkPOObservable<T extends IStreamObject<?>> extends
		Observable {
	private List<IBenchmarkEvaluation> evaluations = new ArrayList<IBenchmarkEvaluation>();

	public BenchmarkPOObservable() {
		// get all observers from registry
		BenchmarkObserverRegistry registry = BenchmarkObserverRegistry
				.getInstance();
		// get the evaluation from every benchmarkObserver
		for (IBenchmarkObserver benchmarkObserver : registry.getObserverMap()
				.values()) {
			evaluations.add(benchmarkObserver.getBenchmarkEvaluation());
		}
	}

	/**
	 * evaluates every registered evaluation (e.g. counting of elements)
	 * 
	 * @param data
	 *            stream object
	 */
	public void evaluate(T object) {
		for (IBenchmarkEvaluation benchmarkEvaluation : evaluations) {
			benchmarkEvaluation.evaluate(this, object);
		}
	}

	/**
	 * if the processing of data stream elements is done before the evaluation
	 * is done, this method is called.
	 */
	public void evaluationDone() {
		for (IBenchmarkEvaluation benchmarkEvaluation : evaluations) {
			benchmarkEvaluation.evaluationDone(this);
		}
	}
}
