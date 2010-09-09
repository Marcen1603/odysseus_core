package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;

/**
 * @author Wolf Bauer, Jonas Jacobi
 */
abstract public class OldStandardOptimizer extends AbstractOptimizer {
	
//	//private Logger logger = LoggerFactory.getLogger(StandardOptimizer.class);
//
//	@Override
//	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
//			List<IQuery> queries, OptimizeParameter parameter)
//			throws QueryOptimizationException {
//		if (!queries.isEmpty()) {
//			for (IQuery editableQuery : queries) {
//				this.queryOptimizer.optimizeQuery(sender, editableQuery,
//						parameter);
//			}
//
//			List<IQuery> newPlan = sender.getRegisteredQueries();
//			newPlan.addAll(queries);
//
//			IExecutionPlan newExecutionPlan = this.planOptimizer
//					.optimizePlan(sender, parameter, newPlan);
//
//			return this.planMigrationStrategy.migratePlan(sender,
//					newExecutionPlan);
//		}
//		return sender.getEditableExecutionPlan();
//	}
//	
//	@Override
//	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
//			List<IQuery> queries, OptimizeParameter parameter, Set<String> rulesToUse)
//			throws QueryOptimizationException {
//		if (!queries.isEmpty()) {
//			for (IQuery editableQuery : queries) {
//				this.queryOptimizer.optimizeQuery(sender, editableQuery,
//						parameter, rulesToUse);
//			}
//
//			List<IQuery> newPlan = sender.getRegisteredQueries();
//			newPlan.addAll(queries);
//
//			IExecutionPlan newExecutionPlan = this.planOptimizer
//					.optimizePlan(sender, parameter, newPlan);
//
//			return this.planMigrationStrategy.migratePlan(sender,
//					newExecutionPlan);
//			
//		}
//		return sender.getEditableExecutionPlan();
//	}
//
//	@Override
//	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
//			T sender, IQuery removedQuery,
//			IExecutionPlan executionPlan, OptimizeParameter parameter)
//			throws QueryOptimizationException {
//		logger.debug("preQueryRemoveOptimization");
//		ArrayList<IQuery> newPlan = new ArrayList<IQuery>(
//				sender.getRegisteredQueries());
//		newPlan.remove(removedQuery);
//		logger.debug("preQueryRemoveOptimization optimize Plan");
//
//		IExecutionPlan newExecutionPlan = this.planOptimizer
//				.optimizePlan(sender, parameter, newPlan);
//
//		logger.debug("preQueryRemoveOptimization migrate");
//		
//		return this.planMigrationStrategy
//				.migratePlan(sender, newExecutionPlan);
//	}
//
//	@Override
//	public void handleFinishedMigration(IQuery query) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public IExecutionPlan preQueryMigrateOptimization(IOptimizable sender,
//			OptimizeParameter parameter) throws QueryOptimizationException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
