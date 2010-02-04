package de.uniol.inf.is.odysseus.planmanagement.optimization.query;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

/**
 * Describes an object which optimizes a single query. Used for OSGi-services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IQueryOptimizer {
	/**
	 * Optimizes a single query and builds the physical plan.
	 * 
	 * @param sender
	 *            Optimize requester which provides informations for the
	 *            optimization.
	 * @param query
	 *            The query that should be optimized.
	 * @param parameters
	 *            Parameter that provide additional information for the
	 *            optimization (e. g. should a rewrite be used).
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 */
	public void optimizeQuery(IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters) throws QueryOptimizationException;
	
	/**
	 * Optimizes a single query and builds the physical plan.
	 * 
	 * @param sender
	 *            Optimize requester which provides informations for the
	 *            optimization.
	 * @param query
	 *            The query that should be optimized.
	 * @param parameters
	 *            Parameter that provide additional information for the
	 *            optimization (e. g. should a rewrite be used).
	 *            
	 * @param rulesToUse Contains the names of the rules to be used for restructuring.
	 *            Other rules will not be used.
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 */
	public void optimizeQuery(IQueryOptimizable sender, IEditableQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse) throws QueryOptimizationException;
}