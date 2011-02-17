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

import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;

/**
 * Defines the cost of an plan migration.
 * 
 * @author Tobias Witt
 *
 */
public class StandardPlanMigrationCostModel implements IPlanMigrationCostModel {

	@Override
	public ICostCalculator<PlanMigration> getCostCalculator() {
		return new StandardPlanMigrationCostCalculator(this);
	}
	
	StandardPlanMigrationCost getCost(PlanMigration migration) {
		// TODO Vergleich erst sinnvoll, wenn mehrere Planmigrationsstrategien verfuegbar sind.
		StandardPlanMigrationCost cost = new StandardPlanMigrationCost();
		if (migration.getStrategy().getClass().getSimpleName().equals("SimplePlanMigrationStrategy")) {
			cost.setScore(100);
		} else {
			// unknown
			cost.setScore(-1);
		}
		return cost;
	}

}
