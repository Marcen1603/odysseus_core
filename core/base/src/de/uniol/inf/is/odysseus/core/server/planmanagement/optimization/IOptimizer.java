/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

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
	 * @param executionPlan 
	 * @param newQueries
	 *            Queries which should be added.
	 * @param parameter
	 *            Parameter for the optimization.
	 * @param rulesToUse Contains the name of the rules to use during optimization.
	 *            Rules that are not contained in this set are not used during optimization.
	 * @return List of added and translated queries, if any
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public List<IPhysicalQuery> optimize(IServerExecutor compiler, IExecutionPlan executionPlan, List<ILogicalQuery> newQueries, OptimizationConfiguration parameter, IDataDictionary dd)
			throws QueryOptimizationException;
	
	/**
	 * Initializes an optimization if a query requests a reoptimization.
	 * 
	 * @param query
	 *            Query that requests a reoptimization.
	 * @param executionPlan
	 *            Current execution plan.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public void reoptimize(IPhysicalQuery query,IExecutionPlan executionPlan)
			throws QueryOptimizationException;

	/**
	 * Initializes an optimization if a plan requests a reoptimization.
	 * 
	 * @param executionPlan
	 *            Current execution plan.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public void reoptimize(IExecutionPlan executionPlan)
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
	public void beforeQueryStart(IPhysicalQuery queryToStart,
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
	public void beforeQueryStop(IPhysicalQuery queryToStop,
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
	 * @param parameter
	 *            Parameter for the optimization.
	 * @return New optimized execution plan.
	 * @throws QueryOptimizationException
	 *             An exception occurred during optimization.
	 */
	public void beforeQueryRemove(IPhysicalQuery removedQuery,
			IExecutionPlan executionPlan, OptimizationConfiguration parameter, IDataDictionary dd)
			throws QueryOptimizationException;
	
	IQuerySharingOptimizer getQuerySharingOptimizer();

}
