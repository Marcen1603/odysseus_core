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

package de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.standardmigrationruleengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionMigrationRuleEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

public class StandardPlanAdaptionMigrationRuleEngine implements
		IPlanAdaptionMigrationRuleEngine {

	private static final Logger LOG = LoggerFactory.getLogger(StandardPlanAdaptionMigrationRuleEngine.class);
	
	// TODO: werte finden/raten/schätzen
	@SuppressWarnings({ "rawtypes", "unchecked" })
	ICost<ILogicalOperator> minCostDifference = null;
	ICost<PlanMigration> maxMigrationCost = null;
	
	@Override
	public boolean evaluate(ICost<ILogicalOperator> costDifference,
			ICost<PlanMigration> migrationCosts) {
		if(this.minCostDifference == null || this.maxMigrationCost == null) {
			LOG.debug("Comparative costs are null");
			return false;
		}
//		int compared = costDifference.compareTo(this.minCostDifference);
//		compared += this.maxMigrationCost.compareTo(migrationCosts);
//		return compared > 0;
		// FIXME: das ist nur zu testzwecken
		return true;
	}

}
