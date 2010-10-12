package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * Describes an object which optimizes global plan. Used for OSGi-services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanOptimizer {
	/**
	 * Optimizes global plan and builds the new execution plan.
	 * 
	 * @param sender
	 *            Optimize requester which provides informations for the
	 *            optimization.
	 * @param allQueries
	 *            List of all queries that are registered.
	 * @param parameters
	 *            Parameter that provide additional information for the
	 *            optimization.
	 * @return Optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 */
	public IExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizationConfiguration parameters, List<IQuery> allQueries)
			throws QueryOptimizationException;
}