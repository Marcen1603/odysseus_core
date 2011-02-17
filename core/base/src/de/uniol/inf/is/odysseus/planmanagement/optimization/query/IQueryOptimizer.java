/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.planmanagement.optimization.query;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

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
			OptimizationConfiguration parameters) throws QueryOptimizationException;
	
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
	public void postTransformationInit(IQuery query,
			List<IPhysicalOperator> physicalPlan) throws QueryOptimizationException,
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
			OptimizationConfiguration parameters) throws QueryOptimizationException;
}