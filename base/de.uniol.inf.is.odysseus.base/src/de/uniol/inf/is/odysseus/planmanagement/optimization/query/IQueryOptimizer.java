package de.uniol.inf.is.odysseus.planmanagement.optimization.query;

import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

/**
 * Describes an object which optimizes a single query. Used for OSGi-services.
 * 
 * @author Wolf Bauer, Tobias Witt
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
	public void optimizeQuery(IQueryOptimizable sender, IQuery query,
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
	public void optimizeQuery(IQueryOptimizable sender, IQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse) throws QueryOptimizationException;
	
	/**
	 * Adds buffers corresponding to the query's
	 * {@link IBufferPlacementStrategy} and initializes the physical plan.
	 * 
	 * @param query
	 *            The query that should be post-initialized.
	 * @param physicalPlan
	 *            The physical plan to set.
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 * @throws OpenFailedException
	 *             An exception during plan initializing.
	 */
	public void postTransformationInit(IQuery query,
			IPhysicalOperator physicalPlan) throws QueryOptimizationException,
			OpenFailedException;
	
	/**
	 * Creates several alternative physical plans for a running query.
	 * 
	 * @param sender
	 *            Optimize requester which provides informations for the
	 *            optimization.
	 * @param query
	 *            The query that should be optimized.
	 * @param parameters
	 *            Parameter that provide additional information for the
	 *            optimization (e. g. should a rewrite be used).
	 * @param rulesToUse
	 *            Contains the names of the rules to be used for restructuring.
	 *            Other rules will not be used.
	 * @return Map of alternative physical plans and corresponding logical
	 *         plans, may not contain the same plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 */
	public Map<IPhysicalOperator, ILogicalOperator> createAlternativePlans(IQueryOptimizable sender, IQuery query,
			OptimizeParameter parameters, Set<String> rulesToUse) throws QueryOptimizationException;
}