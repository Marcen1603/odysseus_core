package de.uniol.inf.is.odysseus.planmanagement.optimization;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

/**
 * Defines an object which provides optimization methods. IOptimizer is the base
 * of the optimization module and combines all optimization services.
 * 
 * @author Jonas Jacobi, Wolf Bauer, Tobias Witt
 */
public interface IOptimizer extends IInfoProvider, IErrorEventHandler {
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
	public IEditableExecutionPlan preStartOptimization(IQuery queryToStart,
			IEditableExecutionPlan execPlan) throws QueryOptimizationException;

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
	public IEditableExecutionPlan preStopOptimization(IQuery queryToStop,
			IEditableExecutionPlan execPlan) throws QueryOptimizationException;

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
	public IExecutionPlan reoptimize(IOptimizable sender, IEditableQuery query,
			IEditableExecutionPlan executionPlan)
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
			IEditableExecutionPlan executionPlan)
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
	 * @param parameters
	 *            Parameter for the optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan,
			AbstractOptimizationParameter<?>... parameters)
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
	public <T extends IPlanOptimizable & IPlanMigratable> IExecutionPlan preQueryRemoveOptimization(
			T sender, IQuery removedQuery,
			IEditableExecutionPlan executionPlan, OptimizeParameter parameter)
			throws QueryOptimizationException;

	/**
	 * Initializes an optimization if queries should be added.
	 * 
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param newQueries
	 *            Queries which should be added.
	 * @param parameters
	 *            Parameter for the optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException;
	
	/**
	 * Initializes an optimization if queries should be added.
	 * 
	 * @param sender
	 *            Optimization request sender, which provides informations for
	 *            the optimization.
	 * @param newQueries
	 *            Queries which should be added.
	 * @param parameters
	 *            Parameter for the optimization.
	 * @param rulesToUse Contains the name of the rules to use during optimization.
	 *            Rules that are not contained in this set are not used during optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> queries,
			Set<String> rulesToUse,
			AbstractOptimizationParameter<?>... parameters)
			throws QueryOptimizationException;

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
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries, OptimizeParameter parameter)
			throws QueryOptimizationException;
	
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
	public IExecutionPlan preQueryAddOptimization(IOptimizable sender,
			List<IEditableQuery> newQueries, OptimizeParameter parameter, Set<String> rulesToUse)
			throws QueryOptimizationException;
	
	/**
	 * Handles a callback, when a plan migration has finished.
	 * 
	 * @param query
	 * 			Query that has finished a migration to a new plan.
	 */
	public void handleFinishedMigration(IEditableQuery query);
	
	/**
	 * 
	 * @return configuration of current {@link IOptimizer}
	 */
	public OptimizationConfiguration getConfiguration();
	
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
	public IExecutionPlan preQueryMigrateOptimization(IOptimizable sender,
			OptimizeParameter parameter) throws QueryOptimizationException;
}
