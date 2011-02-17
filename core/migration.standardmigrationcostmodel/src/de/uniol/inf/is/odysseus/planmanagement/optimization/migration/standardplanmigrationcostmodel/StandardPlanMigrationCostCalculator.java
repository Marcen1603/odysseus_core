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
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardplanmigrationcostmodel;

import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.AbstractCostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;

/**
 * Calculates the cost of a plan migration.
 * 
 * @author Tobias Witt
 *
 */
public class StandardPlanMigrationCostCalculator extends AbstractCostCalculator<PlanMigration> {
	
	private StandardPlanMigrationCostModel model;
	
	public StandardPlanMigrationCostCalculator(StandardPlanMigrationCostModel model) {
		this.model = model;
	}

	@Override
	public ICost<PlanMigration> calculateCost(PlanMigration candidate) {
		return this.model.getCost(candidate);
	}

}
