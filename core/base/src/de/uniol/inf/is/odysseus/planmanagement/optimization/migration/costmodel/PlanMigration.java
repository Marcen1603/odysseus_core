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
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;

/**
 * PlanMigration identifies a combination of old and new physical plans and a
 * migration strategy. Can be compared to other {@link PlanMigration}s by using
 * the {@link IPlanMigrationCostModel}.
 * 
 * @author Tobias Witt
 * 
 */
public class PlanMigration {

	private IPhysicalOperator oldPlan;
	private IPhysicalOperator newPlan;
	private IPlanMigrationStrategy strategy;

	public PlanMigration(IPhysicalOperator oldPlan, IPhysicalOperator newPlan,
			IPlanMigrationStrategy strategy) {
		this.newPlan = newPlan;
		this.oldPlan = oldPlan;
		this.strategy = strategy;
	}

	public IPhysicalOperator getOldPlan() {
		return oldPlan;
	}

	public void setOldPlan(IPhysicalOperator oldPlan) {
		this.oldPlan = oldPlan;
	}

	public IPhysicalOperator getNewPlan() {
		return newPlan;
	}

	public void setNewPlan(IPhysicalOperator newPlan) {
		this.newPlan = newPlan;
	}

	public IPlanMigrationStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(IPlanMigrationStrategy strategy) {
		this.strategy = strategy;
	}

}
