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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Describes an object which optimizes a single query. Used for OSGi-services.
 * 
 * @author Wolf Bauer, Tobias Witt, Marco Grawunder
 * 
 */
public interface IQueryOptimizer {
	/**
	 * Translates and optimizes a single query
	 * 
	 * @param query
	 *            The query that should be optimized.
	 * @param parameters
	 *            Parameter that provide additional information for the
	 *            optimization (e. g. should a rewrite be used).
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 */
	public IPhysicalQuery optimizeQuery(IServerExecutor executor, ILogicalQuery query,
			OptimizationConfiguration parameters, IDataDictionary dd) throws QueryOptimizationException;
	
	/**
	 * Adds buffers corresponding to the query's
	 * {@link IBufferPlacementStrategy} and initializes the physical plan.
	 * 
	 * @param sender
	 *            Optimize requester which provides informations for the
	 *            optimization.
	 * @param query
	 *            The query that should be post-initialized.
	 * @param physicalPlan
	 *            The physical plan to set.
	 * @throws QueryOptimizationException
	 *             An exception occurred during the optimization.
	 * @throws OpenFailedException
	 *             An exception during plan initializing.
	 */
	public void postTransformationInit(IPhysicalQuery query) throws QueryOptimizationException,
			OpenFailedException;
}