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

/**
 * Interface for an easy integration of new evaluations into ObserverBenchmarkPO
 * @author ChrisToenjesDeye
 *
 */
public interface IBenchmarkEvaluation {
	
	/**
	 * do a custom evaluation of the data stream elements
	 * @param observable the observable
	 * @param object the data stream elements
	 */
	<T extends IStreamObject<?>> void evaluate(BenchmarkPOObservable<?> observable, T object);
	
	/**
	 * If every element is processed, evaluation need to be aborted
	 */
	void evaluationDone(BenchmarkPOObservable<?> observable);
}
