package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.List;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

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
	public IEditableExecutionPlan optimizePlan(IPlanOptimizable sender,
			OptimizeParameter parameters, List<IEditableQuery> allQueries)
			throws QueryOptimizationException;
}