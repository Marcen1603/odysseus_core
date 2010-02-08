package de.uniol.inf.is.odysseus.planmanagement.optimization.advancedoptimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.AbstractOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.MigrationHelper;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

/**
 * 
 * @author Tobias Witt
 *
 */
public class AdvancedOptimizer extends AbstractOptimizer {

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
		
		// TODO: alternative logische Plaene erzeugen
		sender.getLogicalPlan();
		// TODO: daraus physische Plaene erzeugen
		sender.getRoot();
		// TODO: Plaene mit Kostenmodell bewerten
		
		// more optimal physical plan
		IPhysicalOperator newPlan = null;
		try {
			// testweise gleicher plan
			newPlan = MigrationHelper.clonePhysicalPlan(sender.getRoot());
		} catch (Exception e) { // CloneNotSupportedException
			this.logger.warn("Reoptimization failed. Could not copy physical plan.",e);
			return executionPlan;
		}
		
		sender = this.planMigrationStrategie.migrateQuery(sender, newPlan);
		
		// TODO: executionPlan aktualisieren
		
		return executionPlan;
	}

}
