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

	private static Map<Class<? extends ILogicalOperator>, List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>>> strategies = new HashMap<Class<? extends ILogicalOperator>, List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>>>();

	public static void registerStrategy(
			IMultithreadedTransformationStrategy<?> multithreadedTransformationStrategy) {
		LOG.debug("Register new MultithreadedTransformationStrategy "
				+ multithreadedTransformationStrategy.getName());
		Class<? extends ILogicalOperator> operatorType = multithreadedTransformationStrategy
				.getOperatorType();
		if (strategies.containsKey(operatorType)) {
			strategies.get(operatorType).add(
					multithreadedTransformationStrategy);
		} else {
			List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = new ArrayList<IMultithreadedTransformationStrategy<? extends ILogicalOperator>>();
			strategiesForOperator.add(multithreadedTransformationStrategy);
			strategies.put(operatorType, strategiesForOperator);
		}
	}

	public static void unregisterStrategy(
			IMultithreadedTransformationStrategy<?> multithreadedTransformationStrategy) {
		LOG.debug("Remove MultithreadedTransformationStrategy "
				+ multithreadedTransformationStrategy.getName());
		Class<? extends ILogicalOperator> operatorType = multithreadedTransformationStrategy
				.getOperatorType();
		if (strategies.containsKey(operatorType)) {
			List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = strategies
					.get(operatorType);
			strategiesForOperator.remove(multithreadedTransformationStrategy);
			if (strategiesForOperator.isEmpty()) {
				strategies.remove(operatorType);
			}
		}
	}

	public static List<IMultithreadedTransformationStrategy<? extends ILogicalOperator>> getStrategiesForOperator(
			Class<? extends ILogicalOperator> operatorType) {
		if (!strategies.containsKey(operatorType)) {
			LOG.error("MultithreadedTransformationStrategy for OperatorName "
					+ operatorType.getName() + " does not exist");
			return null;
		} else {
			return strategies.get(operatorType);
		}
	}

	public static List<Class<? extends ILogicalOperator>> getValidTypes() {
		List<Class<? extends ILogicalOperator>> validTypes = new ArrayList<Class<? extends ILogicalOperator>>();
		validTypes.addAll(strategies.keySet());
		return validTypes;
	}
}
