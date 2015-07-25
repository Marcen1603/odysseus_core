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
package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;

/**
 * this class represents the result of the intialization of the current query in
 * benchmarker UI. COntains strategies for inter operator parallelization
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkInitializationResult {

	private Map<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>> strategiesForOperator = new HashMap<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>>();

	public Map<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>> getStrategiesForOperator() {
		return strategiesForOperator;
	}

	public List<IParallelTransformationStrategy<? extends ILogicalOperator>> getStrategiesForOperator(
			ILogicalOperator operator) {
		return strategiesForOperator.get(operator);
	}

	/**
	 * adds a list of strategies for an specific operator 
	 * 
	 * @param operator
	 * @param strategies
	 */
	public void setStrategiesForOperator(
			ILogicalOperator operator,
			List<IParallelTransformationStrategy<? extends ILogicalOperator>> strategies) {
		if (!strategiesForOperator.containsKey(operator)) {
			strategiesForOperator
					.put(operator,
							new ArrayList<IParallelTransformationStrategy<? extends ILogicalOperator>>());
		}
		List<IParallelTransformationStrategy<? extends ILogicalOperator>> list = strategiesForOperator
				.get(operator);
		list.addAll(strategies);
	}
}
