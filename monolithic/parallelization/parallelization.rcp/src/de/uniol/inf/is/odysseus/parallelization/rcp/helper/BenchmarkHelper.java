package de.uniol.inf.is.odysseus.parallelization.rcp.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.IParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry.ParallelTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkDataHandler;
import de.uniol.inf.is.odysseus.parallelization.rcp.data.BenchmarkInitializationResult;

public class BenchmarkHelper {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getPossibleParallelizationOptions(BenchmarkDataHandler dataHandler) throws IllegalArgumentException {
		if (dataHandler.getLogicalQueries().isEmpty()) {
			throw new IllegalArgumentException("Logical query not found");
		}
		
		List<ILogicalQuery> logicalQueries = dataHandler.getLogicalQueries();
		
		BenchmarkInitializationResult result = new BenchmarkInitializationResult();
		
		for (ILogicalQuery logicalQuery : logicalQueries) {
			ILogicalOperator logicalPlan = logicalQuery.getLogicalPlan();
			Set<Class<? extends ILogicalOperator>> set = new HashSet<>();
			List<Class<? extends ILogicalOperator>> validTypes = ParallelTransformationStrategyRegistry
					.getValidTypes();
			if (validTypes != null) {
				// if we have strategies, we need to add the logical operators to
				// graph visitor
				set.addAll(validTypes);
				CollectOperatorLogicalGraphVisitor<ILogicalOperator> collVisitor = new CollectOperatorLogicalGraphVisitor<>(
						set, true);
				GenericGraphWalker collectWalker = new GenericGraphWalker();
				collectWalker.prefixWalk(logicalPlan, collVisitor);
				
				// result of graph visitor contains all operators, which have one or
				// more available strategies
				for (ILogicalOperator operatorForTransformation : collVisitor.getResult()) {
					if (operatorForTransformation.getUniqueIdentifier() != null){
						
						List<IParallelTransformationStrategy<? extends ILogicalOperator>> strategiesForOperator = ParallelTransformationStrategyRegistry
								.getStrategiesForOperator(operatorForTransformation.getClass());
						if (!strategiesForOperator.isEmpty()) {
							result.setStrategiesForOperator(operatorForTransformation, strategiesForOperator);
						}					
					}
				}
			}
		}
		
		dataHandler.setBenchmarkInitializationResult(result);
	}
}
