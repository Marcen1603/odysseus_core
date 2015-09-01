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
package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;

/**
 * Registry for easy extension and integration of new strategies for inter
 * operator parallelization
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelTransformationStrategyRegistry {

	private static Logger LOG = LoggerFactory
			.getLogger(ParallelTransformationStrategyRegistry.class);

	// strategies for logical operator
	private static Map<Class<ILogicalOperator>, List<IParallelTransformationStrategy<ILogicalOperator>>> strategiesForLogicalOperator = new HashMap<Class<ILogicalOperator>, List<IParallelTransformationStrategy<ILogicalOperator>>>();

	// strategies by name
	private static Map<String, IParallelTransformationStrategy<ILogicalOperator>> strategiesByName = new HashMap<String, IParallelTransformationStrategy<ILogicalOperator>>();

	/**
	 * registers a new strategy (OSGI method)
	 * 
	 * @param parallelTransformationStrategy
	 */
	public static void registerStrategy(
			IParallelTransformationStrategy<ILogicalOperator> parallelTransformationStrategy) {
		LOG.debug("Register new ParallelTransformationStrategy "
				+ parallelTransformationStrategy.getName());

		// strategies by logical operator
		Class<ILogicalOperator> operatorType = parallelTransformationStrategy
				.getOperatorType();
		if (strategiesForLogicalOperator.containsKey(operatorType)) {
			strategiesForLogicalOperator.get(operatorType).add(
					parallelTransformationStrategy);
		} else {
			List<IParallelTransformationStrategy<ILogicalOperator>> strategiesForOperator = new ArrayList<IParallelTransformationStrategy<ILogicalOperator>>();
			strategiesForOperator.add(parallelTransformationStrategy);
			strategiesForLogicalOperator.put(operatorType,
					strategiesForOperator);
		}

		// strategies by name
		if (!strategiesByName.containsKey(parallelTransformationStrategy
				.getName().toLowerCase())) {
			strategiesByName.put(parallelTransformationStrategy.getName()
					.toLowerCase(), parallelTransformationStrategy);
		}
	}

	/**
	 * unregisters an existing strategy (OSGI method)
	 * 
	 * @param parallelTransformationStrategy
	 */
	public static void unregisterStrategy(
			IParallelTransformationStrategy<ILogicalOperator> parallelTransformationStrategy) {
		LOG.debug("Remove ParallelTransformationStrategy "
				+ parallelTransformationStrategy.getName());

		// strategies by logical operator
		Class<? extends ILogicalOperator> operatorType = parallelTransformationStrategy
				.getOperatorType();
		if (strategiesForLogicalOperator.containsKey(operatorType)) {
			List<IParallelTransformationStrategy<ILogicalOperator>> strategiesForOperator = strategiesForLogicalOperator
					.get(operatorType);
			strategiesForOperator.remove(parallelTransformationStrategy);
			if (strategiesForOperator.isEmpty()) {
				strategiesForLogicalOperator.remove(operatorType);
			}
		}

		// strategies by name
		if (strategiesByName.containsKey(parallelTransformationStrategy
				.getName().toLowerCase())) {
			strategiesByName.remove(parallelTransformationStrategy.getName()
					.toLowerCase());
		}
	}

	/**
	 * returns a list of strategies for a given operator type
	 * 
	 * @param operatorType
	 * @return list of strategies
	 */
	public static List<IParallelTransformationStrategy<ILogicalOperator>> getStrategiesForOperator(
			Class<? extends ILogicalOperator> operatorType) {
		if (!strategiesForLogicalOperator.containsKey(operatorType)) {
			LOG.error("ParallelTransformationStrategy for OperatorName "
					+ operatorType.getName() + " does not exist");
			return null;
		} else {
			return strategiesForLogicalOperator.get(operatorType);
		}
	}

	/**
	 * returns a strategy with the given name if exists
	 * 
	 * @param name
	 * @return
	 */
	public static IParallelTransformationStrategy<ILogicalOperator> getStrategiesByName(
			String name) {
		if (strategiesByName.containsKey(name.toLowerCase())) {
			return strategiesByName.get(name.toLowerCase());
		}
		return null;
	}

	/**
	 * returns a list of valid logical operator types, which support a
	 * parallelization strategy
	 * 
	 * @return
	 */
	public static List<Class<? extends ILogicalOperator>> getValidTypes() {
		List<Class<? extends ILogicalOperator>> validTypes = new ArrayList<Class<? extends ILogicalOperator>>();
		validTypes.addAll(strategiesForLogicalOperator.keySet());
		return validTypes;
	}

	/**
	 * returns a list of valid strategy names
	 * 
	 * @return
	 */
	public static List<String> getValidStrategyNames() {
		List<String> validNames = new ArrayList<String>();
		validNames.addAll(strategiesByName.keySet());
		return validNames;
	}

	/**
	 * checks if the name of a strategy is valid
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isValidStrategyName(String name) {
		return strategiesByName.containsKey(name.toLowerCase());
	}
}
