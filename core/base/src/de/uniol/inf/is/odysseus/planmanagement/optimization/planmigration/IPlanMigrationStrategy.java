package de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

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
	public IExecutionPlan migratePlan(IPlanMigratable sender,
			IExecutionPlan newExecutionPlan);
	
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
			List<IPhysicalOperator> newPlanRoot) throws QueryOptimizationException;
	
	public String getName();
}
