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
package de.uniol.inf.is.odysseus.planmanagement.optimization.standardoptimizer;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * Holds context data for a plan migration. Used by {@link StandardOptimizer} to
 * access data on migration finished callback.
 * 
 * @author Tobias Witt
 * 
 */
class PlanMigrationContext {

	private IPhysicalOperator root;
	private ILogicalOperator logicalPlan;
	private IQuery query;
	private IPlanMigrationStrategy strategy;
	private IOptimizable sender;

	public PlanMigrationContext(IQuery query) {
		this.query = query;
	}

	IPhysicalOperator getRoot() {
		return root;
	}

	void setRoot(IPhysicalOperator root) {
		this.root = root;
	}

	ILogicalOperator getLogicalPlan() {
		return logicalPlan;
	}

	void setLogicalPlan(ILogicalOperator logicalPlan) {
		this.logicalPlan = logicalPlan;
	}

	IQuery getQuery() {
		return query;
	}

	void setQuery(IQuery query) {
		this.query = query;
	}

	void setStrategie(IPlanMigrationStrategy strategy) {
		this.strategy = strategy;
	}

	IPlanMigrationStrategy getStrategy() {
		return strategy;
	}

	public void setSender(IOptimizable sender) {
		this.sender = sender;
	}

	public IOptimizable getSender() {
		return sender;
	}

}
