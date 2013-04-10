/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * 
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionMigrationFuzzyRuleEngine {

	/**
	 * Evaluate if a migration is good or not with the given costs.
	 * 
	 * @param costDifference Cost difference between the oldPlan and the newPlan.
	 * @param migrationCosts Cost of the migration of the oldPlan to the newPlan.
	 * @return
	 */
	public boolean evaluate(ICost<ILogicalOperator> costDifference, ICost<PlanMigration> migrationCosts);
}
