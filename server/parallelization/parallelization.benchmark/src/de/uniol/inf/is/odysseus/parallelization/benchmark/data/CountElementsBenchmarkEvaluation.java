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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class CountElementsBenchmarkEvaluation extends
		AbstractBenchmarkEvaluation {

	private int counter;
	private long startTimestamp;
	private long endTimestamp;
	private int numberOfElements;

	public CountElementsBenchmarkEvaluation(
			IBenchmarkObserver benchmarkObserver, int numberOfElements) {
		super(benchmarkObserver);
		this.numberOfElements = numberOfElements;
	}

	@Override
	public <T extends IStreamObject<?>> void evaluate(
			BenchmarkPOObservable<?> observable, T object) {
		if (counter > numberOfElements) {
			return;
		}

		if (counter == 0) {
			startTimestamp = System.currentTimeMillis();
		}

		if (counter < numberOfElements) {
			counter++;
			if (counter == numberOfElements) {
				endTimestamp = System.currentTimeMillis();
				long executionTime = endTimestamp - startTimestamp;
				updateObserver(observable, executionTime);
			}
		}

	}

	@Override
	public void evaluationDone(BenchmarkPOObservable<?> observable) {
		// update observer and tell them, that evaluation was done before number
		// of elements was reached
		updateObserver(observable, -1l);
	}

}
