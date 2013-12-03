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

package de.uniol.inf.is.odysseus.costmodel.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * Represents the estimator for a plan migration based on a plan migration
 * strategy and a physical query.
 * 
 * @author merlin (Nach Vorbild von Timo Michelsen)
 * 
 */
public interface IPlanMigrationEstimator {

	/**
	 * Estimate the costs of the given PlanMigration. Uses the size of the plans
	 * and the execution costs of both plans. Everything is based on the plan
	 * migration strategy.
	 * 
	 * @param migration
	 *            Container containing all information about the plan migration
	 *            to estimate.
	 * 
	 * @return The estimation for this PlanMigration containing all information
	 *         about it.
	 */
	public PlanMigrationEstimation estimatePlanMigration(PlanMigration migration);
	
	
//	public Class<? extends IPlanMigrationStrategy> getPlanMigrationStrategyClass();
}
