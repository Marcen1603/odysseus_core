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
package de.uniol.inf.is.odysseus.planmigration;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmigration.exception.MigrationException;

/**
 * Describes an object which migrates an old and a new execution plan. Used for
 * OSGi-services.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public interface IPlanMigrationStrategy extends IMigrationEventSource, IMigrationListener{
	/**
	 * Migrates an old and a new execution plan.
	 * 
	 * @param sender
	 *            Migration requester which provides informations for the
	 *            migration (e. g. the old execution plan).
	 * @param newExecutionPlan
	 *            the new created execution plan
	 * @return migrated execution plan that should be executed.
	 */
	public IExecutionPlan migratePlan(IPlanMigratable sender,
			IExecutionPlan newExecutionPlan);
	
	/**
	 * Migrates a query to a new physical plan.
	 * 
	 * @param sender
	 * 				Optimizer possibly receives a callback, when the migration has finished
	 * @param runningQuery
	 * 				installed query, was stopped before by optimizer
	 * @param newPlanRoot
	 * 				new physical plan to migrate to
	 * @throws QueryOptimizationException
	 */
	public void migrateQuery(IServerExecutor sender, IPhysicalQuery runningQuery, 
			List<IPhysicalOperator> newPlanRoot) throws QueryOptimizationException, MigrationException;
	
	public String getName();
	
	/**
	 * Check if a plan in the given query can be migrated using this strategy.
	 * 
	 * @param query Query containing the plan
	 * @return 
	 */
	public boolean isMigratable(IPhysicalQuery query);
}
