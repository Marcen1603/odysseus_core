package de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;

/**
 * Describes an object which migrates an old and a new execution plan. Used for
 * OSGi-services.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public interface IPlanMigrationStrategy {
	/**
	 * Migrates an old and a new execution plan.
	 * 
	 * @param sender
	 *            Migration requester which provides informations for the
	 *            migration (e. g. the old execution plan).
	 * @param newExecutionPlan
	 *            the new created execution plan
	 * @return migrated execution plan that should be executed.
	 */
	public IEditableExecutionPlan migratePlan(IPlanMigratable sender,
			IEditableExecutionPlan newExecutionPlan);
	
	/**
	 * Migrates a query to a new physical plan.
	 * 
	 * @param sender
	 * 				Optimizer possibly receives a callback, when the migration has finished
	 * @param runningQuery
	 * 				installed query, was stopped before by optimizer
	 * @param newPlanRoot
	 * 				new physical plan to migrate to
	 * @throws QueryOptimizationException
	 */
	public void migrateQuery(IOptimizer sender, IQuery runningQuery, 
			IPhysicalOperator newPlanRoot) throws QueryOptimizationException;
	
	public String getName();
}
