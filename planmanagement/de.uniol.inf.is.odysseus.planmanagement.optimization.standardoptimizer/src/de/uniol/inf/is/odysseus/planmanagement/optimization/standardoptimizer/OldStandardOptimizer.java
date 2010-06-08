package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

/**
 * @author Wolf Bauer, Jonas Jacobi
 */
abstract public class OldStandardOptimizer extends AbstractOptimizer {
	
//	//private Logger logger = LoggerFactory.getLogger(StandardOptimizer.class);
//
//	@Override
//	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
//			List<IEditableQuery> queries, OptimizeParameter parameter)
//			throws QueryOptimizationException {
//		if (!queries.isEmpty()) {
//			for (IEditableQuery editableQuery : queries) {
//				this.queryOptimizer.optimizeQuery(sender, editableQuery,
//						parameter);
//			}
//
//			List<IEditableQuery> newPlan = sender.getRegisteredQueries();
//			newPlan.addAll(queries);
//
//			IEditableExecutionPlan newExecutionPlan = this.planOptimizer
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
//			List<IEditableQuery> queries, OptimizeParameter parameter, Set<String> rulesToUse)
//			throws QueryOptimizationException {
//		if (!queries.isEmpty()) {
//			for (IEditableQuery editableQuery : queries) {
//				this.queryOptimizer.optimizeQuery(sender, editableQuery,
//						parameter, rulesToUse);
//			}
//
//			List<IEditableQuery> newPlan = sender.getRegisteredQueries();
//			newPlan.addAll(queries);
//
//			IEditableExecutionPlan newExecutionPlan = this.planOptimizer
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
//			IEditableExecutionPlan executionPlan, OptimizeParameter parameter)
//			throws QueryOptimizationException {
//		logger.debug("preQueryRemoveOptimization");
//		ArrayList<IEditableQuery> newPlan = new ArrayList<IEditableQuery>(
//				sender.getRegisteredQueries());
//		newPlan.remove(removedQuery);
//		logger.debug("preQueryRemoveOptimization optimize Plan");
//
//		IEditableExecutionPlan newExecutionPlan = this.planOptimizer
//				.optimizePlan(sender, parameter, newPlan);
//
//		logger.debug("preQueryRemoveOptimization migrate");
//		
//		return this.planMigrationStrategy
//				.migratePlan(sender, newExecutionPlan);
//	}
//
//	@Override
//	public void handleFinishedMigration(IEditableQuery query) {
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
