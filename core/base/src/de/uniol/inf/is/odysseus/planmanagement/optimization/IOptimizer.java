package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.IOptimizationSetting;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * Defines an object which provides optimization methods. IOptimizer is the base
 * of the optimization module and combines all optimization services.
 * 
 * @author Jonas Jacobi, Wolf Bauer, Tobias Witt
 */
public interface IOptimizer extends IInfoProvider, IErrorEventHandler {
	
	// ------------------------------------------------------------------------
	// (Re)Optimization-Methods
	// ------------------------------------------------------------------------
	
	/**
	 * Initializes an optimization if queries should be added.
	 * 
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param newQueries
	 *            Queries which should be added.
	 * @param parameter
	 *            Parameter for the optimization.
	 * @param rulesToUse Contains the name of the rules to use during optimization.
	 *            Rules that are not contained in this set are not used during optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan optimize(IOptimizable sender,
			List<IQuery> newQueries, OptimizationConfiguration parameter)
			throws QueryOptimizationException;
	
	IExecutionPlan optimize(IOptimizable sender,
			List<IQuery> newQueries, IOptimizationSetting... parameters)
			throws QueryOptimizationException;
	
	/**
	 * Initializes an optimization if a query requests a reoptimization.
	 * 
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param query
	 *            Query that requests a reoptimization.
	 * @param executionPlan
	 *            Current execution plan.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan reoptimize(IOptimizable sender, IQuery query,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException;

	/**
	 * Initializes an optimization if a plan requests a reoptimization.
	 * 
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param executionPlan
	 *            Current execution plan.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan reoptimize(IOptimizable sender,
			IExecutionPlan executionPlan)
			throws QueryOptimizationException;
	
	
	// ======================================================================
	// Buffer Placement and Configuration
	// ======================================================================
	/**
	 * Returns as a ID-list of all registered {@link IBufferPlacementStrategy}.
	 * 
	 * @return ID-list of all registered {@link IBufferPlacementStrategy}.
	 */
	public Set<String> getRegisteredBufferPlacementStrategies();

	/**
	 * Returns a {@link IBufferPlacementStrategy} for an ID. <code>null</code>
	 * if no {@link IBufferPlacementStrategy} is found.
	 * 
	 * @param strategy
	 *            ID of the searched {@link IBufferPlacementStrategy}.
	 * @return {@link IBufferPlacementStrategy} for an ID. <code>null</code> if
	 *         no {@link IBufferPlacementStrategy} is found.
	 */
	public IBufferPlacementStrategy getBufferPlacementStrategy(String strategy);
	
	/**
	 * 
	 * @return configuration of current {@link IOptimizer}
	 */
	public OptimizationConfiguration getConfiguration();

	// ============================================================================
	// Hooks to allow some action before a query start/stop/remove
	// ============================================================================
	/**
	 * Initializes an optimization if a query should be started.
	 * 
	 * @param queryToStart
	 *            Query that should be started.
	 * @param execPlan
	 *            Current execution plan.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan beforeQueryStart(IQuery queryToStart,
			IExecutionPlan execPlan) throws QueryOptimizationException;

	/**
	 * Initializes an optimization if a query should be stopped.
	 * 
	 * @param queryToStop
	 *            Query that should be stopped.
	 * @param execPlan
	 *            Current execution plan.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan beforeQueryStop(IQuery queryToStop,
			IExecutionPlan execPlan) throws QueryOptimizationException;

	
	/**
	 * Initializes an optimization if a query should be removed.
	 * 
	 * @param <T>
	 *            Type of the optimization request sender
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param removedQuery
	 *            Query that should be removed.
	 * @param executionPlan
	 *            Current execution plan.
	 * @param parameters
	 *            Parameter for the optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan beforeQueryRemove(
			T sender, IQuery removedQuery,
			IExecutionPlan executionPlan,
			IOptimizationSetting<?>... parameters)
			throws QueryOptimizationException;

	/**
	 * Initializes an optimization if a query should be removed.
	 * 
	 * @param <T>
	 *            Type of the optimization request sender
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param removedQuery
	 *            Query that should be removed.
	 * @param executionPlan
	 *            Current execution plan.
	 * @param parameter
	 *            Parameter for the optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan beforeQueryRemove(
			T sender, IQuery removedQuery,
			IExecutionPlan executionPlan, OptimizationConfiguration parameter)
			throws QueryOptimizationException;
	
	/**
	 * Initializes an optimization if queries should be migrated.
	 * 
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param parameter
	 *            Parameter for the optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 */
	public IExecutionPlan beforeQueryMigration(IOptimizable sender,
			OptimizationConfiguration parameter) throws QueryOptimizationException;
	
	/**
	 * Handles a callback, when a plan migration has finished.
	 * 
	 * @param query
	 * 			Query that has finished a migration to a new plan.
	 */
	public void handleFinishedMigration(IQuery query);
	

	



}
