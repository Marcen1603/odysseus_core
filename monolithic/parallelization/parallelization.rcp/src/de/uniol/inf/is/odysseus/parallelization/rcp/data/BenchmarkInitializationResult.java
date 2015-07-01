package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;

public class BenchmarkInitializationResult {

	private Map<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>> strategiesForOperator = new HashMap<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>>();

	public Map<ILogicalOperator, List<IParallelTransformationStrategy<? extends ILogicalOperator>>> getStrategiesForOperator() {
		return strategiesForOperator;
	}

	public List<IParallelTransformationStrategy<? extends ILogicalOperator>> getStrategiesForOperator(
			ILogicalOperator operator) {
		return strategiesForOperator.get(operator);
	}

	
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
