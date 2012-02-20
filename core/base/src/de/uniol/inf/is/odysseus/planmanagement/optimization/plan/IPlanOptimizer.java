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
package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

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
			OptimizationConfiguration parameters, List<IPhysicalQuery> allQueries, IDataDictionary dd)
			throws QueryOptimizationException;
}