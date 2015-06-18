package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.IMultithreadedTransformationStrategy;

public class MultithreadedTransformationStrategyRegistry {

	private static Logger LOG = LoggerFactory
			.getLogger(MultithreadedTransformationStrategyRegistry.class);

	// strategies for logical operator
	private static Map<Class<? extends ILogicalOperator>, List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>>> strategiesForLogicalOperator = new HashMap<Class<? extends ILogicalOperator>, List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>>>();

	// strategies by name
	private static Map<String, IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesByName = new HashMap<String, IMultithreadedTransformationStrategy<? extends ILogicalOperator>>();

	public static void registerStrategy(
			IMultithreadedTransformationStrategy<?> multithreadedTransformationStrategy) {
		LOG.debug("Register new MultithreadedTransformationStrategy "
				+ multithreadedTransformationStrategy.getName());

		// strategies by logical operator
		Class<? extends ILogicalOperator> operatorType = multithreadedTransformationStrategy
				.getOperatorType();
		if (strategiesForLogicalOperator.containsKey(operatorType)) {
			strategiesForLogicalOperator.get(operatorType).add(
					multithreadedTransformationStrategy);
		} else {
			List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = new ArrayList<IMultithreadedTransformationStrategy<? extends ILogicalOperator>>();
			strategiesForOperator.add(multithreadedTransformationStrategy);
			strategiesForLogicalOperator.put(operatorType,
					strategiesForOperator);
		}

		// strategies by name
		if (!strategiesByName.containsKey(multithreadedTransformationStrategy
				.getName().toLowerCase())) {
			strategiesByName.put(multithreadedTransformationStrategy.getName()
					.toLowerCase(), multithreadedTransformationStrategy);
		}
	}

	public static void unregisterStrategy(
			IMultithreadedTransformationStrategy<?> multithreadedTransformationStrategy) {
		LOG.debug("Remove MultithreadedTransformationStrategy "
				+ multithreadedTransformationStrategy.getName());

		// strategies by logical operator
		Class<? extends ILogicalOperator> operatorType = multithreadedTransformationStrategy
				.getOperatorType();
		if (strategiesForLogicalOperator.containsKey(operatorType)) {
			List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = strategiesForLogicalOperator
					.get(operatorType);
			strategiesForOperator.remove(multithreadedTransformationStrategy);
			if (strategiesForOperator.isEmpty()) {
				strategiesForLogicalOperator.remove(operatorType);
			}
		}

		// strategies by name
		if (strategiesByName.containsKey(multithreadedTransformationStrategy
				.getName().toLowerCase())) {
			strategiesByName.remove(multithreadedTransformationStrategy
					.getName().toLowerCase());
		}
	}

	public static List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> getStrategiesForOperator(
			Class<? extends ILogicalOperator> operatorType) {
		if (!strategiesForLogicalOperator.containsKey(operatorType)) {
			LOG.error("MultithreadedTransformationStrategy for OperatorName "
					+ operatorType.getName() + " does not exist");
			return null;
		} else {
			return strategiesForLogicalOperator.get(operatorType);
		}
	}

	public static IMultithreadedTransformationStrategy<? extends ILogicalOperator> getStrategiesByName(
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
