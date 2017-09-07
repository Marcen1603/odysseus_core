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
package de.uniol.inf.is.odysseus.parallelization.rcp.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry.ParallelTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;

/**
 * this helper is needed for initialization of the currently selected query.
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkHelper {

	/**
	 * gets all possible parallelization options for the existing logical
	 * queries. the logical plan is searched for operator types that support
	 * inter operator parallelization. if there exists at least one operator,
	 * all possible strategies are detected
	 * 
	 * @param dataHandler
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getPossibleParallelizationOptions(
			BenchmarkDataHandler dataHandler) throws IllegalArgumentException {
		if (dataHandler.getBenchmarkInitializationResult().getLogicalQueries()
				.isEmpty()) {
			throw new IllegalArgumentException("Logical query not found");
		}

		List<ILogicalQuery> logicalQueries = dataHandler
				.getBenchmarkInitializationResult().getLogicalQueries();

		// for each logical query, search possible operators
		for (ILogicalQuery logicalQuery : logicalQueries) {
			ILogicalOperator logicalPlan = logicalQuery.getLogicalPlan().getRoot();
			Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
			List<Class<? extends ILogicalOperator>> validTypes = ParallelTransformationStrategyRegistry
					.getValidTypes();
			if (validTypes != null) {
				// if we have strategies, we need to add the logical operators
				// to
				// graph visitor
				set.addAll(validTypes);
				CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(
						set, true);
				GenericGraphWalker collectWalker = new GenericGraphWalker();
				collectWalker.prefixWalk(logicalPlan, collVisitor);

				// result of graph visitor contains all operators, which have
				// one or
				// more available strategies
				for (ILogicalOperator operatorForTransformation : collVisitor
						.getResult()) {
					if (operatorForTransformation.getUniqueIdentifier() != null) {

						List<IParallelTransformationStrategy<ILogicalOperator>> strategiesForOperator = ParallelTransformationStrategyRegistry
								.getStrategiesForOperator(operatorForTransformation
										.getClass());
						if (!strategiesForOperator.isEmpty()) {
							dataHandler.getBenchmarkInitializationResult()
									.setStrategiesForOperator(
											operatorForTransformation,
											strategiesForOperator);
						}
					}
				}
			}
		}
	}
}
