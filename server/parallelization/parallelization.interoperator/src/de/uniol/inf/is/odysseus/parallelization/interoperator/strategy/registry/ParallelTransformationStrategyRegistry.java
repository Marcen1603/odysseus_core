package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;

public class ParallelTransformationStrategyRegistry {

	private static Logger LOG = LoggerFactory
			.getLogger(ParallelTransformationStrategyRegistry.class);

	// strategies for logical operator
	private static Map<Class<? extends ILogicalOperator>, List<IParallelTransformationStrategy<? extends ILogicalOperator>>> strategiesForLogicalOperator = new HashMap<Class<? extends ILogicalOperator>, List<IParallelTransformationStrategy<? extends ILogicalOperator>>>();

	// strategies by name
	private static Map<String, IParallelTransformationStrategy<? extends ILogicalOperator>> strategiesByName = new HashMap<String, IParallelTransformationStrategy<? extends ILogicalOperator>>();

	public static void registerStrategy(
			IParallelTransformationStrategy<?> parallelTransformationStrategy) {
		LOG.debug("Register new ParallelTransformationStrategy "
				+ parallelTransformationStrategy.getName());

		// strategies by logical operator
		Class<? extends ILogicalOperator> operatorType = parallelTransformationStrategy
				.getOperatorType();
		if (strategiesForLogicalOperator.containsKey(operatorType)) {
			strategiesForLogicalOperator.get(operatorType).add(
					parallelTransformationStrategy);
		} else {
			List<IParallelTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = new ArrayList<IParallelTransformationStrategy<? extends ILogicalOperator>>();
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

	public static void unregisterStrategy(
			IParallelTransformationStrategy<?> parallelTransformationStrategy) {
		LOG.debug("Remove ParallelTransformationStrategy "
				+ parallelTransformationStrategy.getName());

		// strategies by logical operator
		Class<? extends ILogicalOperator> operatorType = parallelTransformationStrategy
				.getOperatorType();
		if (strategiesForLogicalOperator.containsKey(operatorType)) {
			List<IParallelTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = strategiesForLogicalOperator
					.get(operatorType);
			strategiesForOperator.remove(parallelTransformationStrategy);
			if (strategiesForOperator.isEmpty()) {
				strategiesForLogicalOperator.remove(operatorType);
			}
		}

		// strategies by name
		if (strategiesByName.containsKey(parallelTransformationStrategy
				.getName().toLowerCase())) {
			strategiesByName.remove(parallelTransformationStrategy
					.getName().toLowerCase());
		}
	}

	public static List<IParallelTransformationStrategy<? extends ILogicalOperator>> getStrategiesForOperator(
			Class<? extends ILogicalOperator> operatorType) {
		if (!strategiesForLogicalOperator.containsKey(operatorType)) {
			LOG.error("ParallelTransformationStrategy for OperatorName "
					+ operatorType.getName() + " does not exist");
			return null;
		} else {
			return strategiesForLogicalOperator.get(operatorType);
		}
	}

	public static IParallelTransformationStrategy<? extends ILogicalOperator> getStrategiesByName(
			String name) {
		if (strategiesByName.containsKey(name.toLowerCase())) {
			return strategiesByName.get(name.toLowerCase());
		}
		return null;
	}

	public static List<Class<? extends ILogicalOperator>> getValidTypes() {
		List<Class<? extends ILogicalOperator>> validTypes = new ArrayList<Class<? extends ILogicalOperator>>();
		validTypes.addAll(strategiesForLogicalOperator.keySet());
		return validTypes;
	}

	public static List<String> getValidStrategyNames() {
		List<String> validNames = new ArrayList<String>();
		validNames.addAll(strategiesByName.keySet());
		return validNames;
	}

	public static boolean isValidStrategyName(String name) {
		return strategiesByName.containsKey(name.toLowerCase());
	}
}