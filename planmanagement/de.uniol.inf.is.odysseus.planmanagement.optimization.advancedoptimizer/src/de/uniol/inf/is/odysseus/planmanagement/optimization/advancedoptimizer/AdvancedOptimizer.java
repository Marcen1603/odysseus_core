package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanExecutionCostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;

/**
 * 
 * @author Tobias Witt
 *
 */
public class AdvancedOptimizer extends AbstractOptimizer {
	
	private static final int MAX_CONCURRENT_OPTIMIZATIONS = 2;
	
	private IAdvancedExecutor executor;
	private Map<Integer, PlanMigrationContext> optimizationContext;
	
	public AdvancedOptimizer() {
		this.optimizationContext = new HashMap<Integer, PlanMigrationContext>();
	}
	
	// FIXME: zyklische abhaengigkeit
	// <reference bind="bindExecutor" cardinality="1..1" interface="de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor" name="IAdvancedExecutor" policy="dynamic"/>
	// reoptimize bietet kein IQueryOptimizable bzw. executor, daher kommt IQueryOptimizer nicht an den compiler
	public void bindExecutor(IAdvancedExecutor executor){
		this.executor = executor;
	}

	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> queries, OptimizeParameter parameter)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IEditableQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameter);
			}

			List<IEditableQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IEditableExecutionPlan newExecutionPlan = this.planOptimizer
					.optimizePlan(sender, parameter, newPlan);

			return this.planMigrationStrategie.migratePlan(sender,
					newExecutionPlan);
		}
		return sender.getEditableExecutionPlan();
	}
	
	@Override
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> queries, OptimizeParameter parameter, Set<String> rulesToUse)
			throws QueryOptimizationException {
		if (!queries.isEmpty()) {
			for (IEditableQuery editableQuery : queries) {
				this.queryOptimizer.optimizeQuery(sender, editableQuery,
						parameter, rulesToUse);
			}

			List<IEditableQuery> newPlan = sender.getRegisteredQueries();
			newPlan.addAll(queries);

			IEditableExecutionPlan newExecutionPlan = this.planOptimizer
					.optimizePlan(sender, parameter, newPlan);

			return this.planMigrationStrategie.migratePlan(sender,
					newExecutionPlan);
			
		}
		return sender.getEditableExecutionPlan();
	}

	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan, OptimizeParameter parameter)
			throws QueryOptimizationException {
		ArrayList<IEditableQuery> newPlan = new ArrayList<IEditableQuery>(
				sender.getRegisteredQueries());
		newPlan.remove(removedQuery);

		IEditableExecutionPlan newExecutionPlan = this.planOptimizer
				.optimizePlan(sender, parameter, newPlan);

		return this.planMigrationStrategie
				.migratePlan(sender, newExecutionPlan);
	}
	
	@Override
	public IExecutionPlan reoptimize(IEditableQuery sender,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		this.logger.info("Start reoptimize query ID "+sender.getID());
		
		// optimization lock on query
		if (this.optimizationContext.containsKey(sender.getID())) {
			this.logger.warn("Aborted reoptimization. Query with ID "+sender.getID()+" is currently getting optimized.");
			return executionPlan;
		} else if (this.optimizationContext.size() >= MAX_CONCURRENT_OPTIMIZATIONS) {
			// TODO: evtl. spaeters triggern der Optimierungsanforderung
			this.logger.warn("Aborted reoptimization. There are currently "+this.optimizationContext.size()+" optimizations running.");
			return executionPlan;
		}
		PlanMigrationContext context = new PlanMigrationContext(sender);
		this.optimizationContext.put(sender.getID(), context);
		
		try {
			// build alternative physical plans
			Map<IPhysicalOperator,ILogicalOperator> alternatives = this.queryOptimizer.createAlternativePlans(
					(IQueryOptimizable)this.executor, sender, 
					new OptimizeParameter(ParameterDoRestruct.TRUE), null);
			
			// TODO: set parameters to CostCalculator
			
			// pick out optimal plan by cost analysis
			IPhysicalOperator newPlan = new PlanExecutionCostCalculator().pickBest(alternatives.keySet());
			
			// TODO: calculate migration overhead cost
			
			// TODO: pick near optimal plan with low migration cost
			context.setRoot(newPlan);
			context.setLogicalPlan(alternatives.get(newPlan));
			
			// TODO: pick optimal migration strategy
			
			// TODO: possibly need to drain buffers and remove them
			// stop scheduling
			sender.stop();
			
			// start migration to new plan 
			this.logger.info("Start migration to new physical plan (query ID "+sender.getID()+")");
			this.planMigrationStrategie.migrateQuery(this, sender, newPlan);
			
			// wait for migration end callback
			this.logger.info("Plan migration running (query ID "+sender.getID()+")");
			
		} catch (Exception e) {
			//this.optimizationContext.remove(sender.getID());
			this.logger.warn("Reoptimization failed. (query ID "+sender.getID()+")",e);
		}
		
		return executionPlan;
	}

	@Override
	public void handleFinishedMigration(IEditableQuery query) {
		PlanMigrationContext context = this.optimizationContext.get(query.getID());

		try {
			// set new logical plan
			query.setLogicalPlan(context.getLogicalPlan());

			// reapply buffers to plan
			IBufferPlacementStrategy bufferPlacementStrategy = query
					.getBuildParameter().getBufferPlacementStrategy();
			if (bufferPlacementStrategy != null) {
				bufferPlacementStrategy.addBuffers(context.getRoot());
			}

			// set and initialize new physical plan
			query.initializePhysicalPlan(context.getRoot());

			// continue scheduling
			query.start();

			// remove lock and context
			this.optimizationContext.remove(query.getID());
			this.logger.info("Finished plan migration (query ID "
					+ query.getID() + ")");

		} catch (Exception e) {
			// this.optimizationContext.remove(sender.getID());
			this.logger.warn("Reoptimization failed. (query ID "
					+ query.getID() + ")", e);
		}
	}

}
