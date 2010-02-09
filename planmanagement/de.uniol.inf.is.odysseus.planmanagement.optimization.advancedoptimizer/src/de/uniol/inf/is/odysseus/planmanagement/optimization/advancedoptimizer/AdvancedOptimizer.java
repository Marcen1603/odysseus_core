package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.MigrationHelper;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanExecutionCostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.parameter.ParameterDoRestruct;

/**
 * 
 * @author Tobias Witt
 *
 */
public class AdvancedOptimizer extends AbstractOptimizer {
	
	private IAdvancedExecutor executor;
	
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

			return newExecutionPlan;
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

			return newExecutionPlan;
			
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

		return newExecutionPlan;
	}


	@Override
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IExecutionPlan reoptimize(IEditableQuery sender,
			IEditableExecutionPlan executionPlan)
			throws QueryOptimizationException {
		this.logger.info("Start reoptimize query ID "+sender.getID());
		
		// TODO: optimization lock on query
		
		try {
			// build alternative physical plans
			Collection<IPhysicalOperator> alternatives = this.queryOptimizer.createAlternativePlans(
					(IQueryOptimizable)this.executor, sender, 
					new OptimizeParameter(ParameterDoRestruct.TRUE), null);
			
			// TODO: set parameters to CostCalculator
			
			// pick out optimal plan by cost analysis
			IPhysicalOperator newPlan = PlanExecutionCostCalculator.pickOptimalPlan(alternatives);
			
			// TODO: calculate migration overhead cost
			
			// TODO: pick near optimal plan with low migration cost
			
			// TODO: pick optimal migration strategy
			
			// start migration to new plan 
			sender = this.planMigrationStrategie.migrateQuery(sender, newPlan);
			
			// TODO: migration end callback
			
			// TODO: executionPlan aktualisieren
			
		} catch (Exception e) {
			this.logger.warn("Reoptimization failed.",e);
		}
		
		return executionPlan;
	}

}
