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
 * Represents the estimation for a plan migration.
 * 
 * @author merlin (Nach Vorbild von Timo Michelsen)
 *
 */
public class PlanMigrationEstimation {

	private PlanMigration migration;
	private IPlanMigrationDetailCost detailCost = null;
	
	public PlanMigrationEstimation(PlanMigration migration) {
		if(migration == null) {
			throw new IllegalArgumentException("Planmigration is null");
		}
		
		this.migration = migration;
	}
	
	public IPlanMigrationDetailCost getDetailCost() {
		return this.detailCost;
	}
	
	public void setDetailCost(IPlanMigrationDetailCost detailCost) {
		this.detailCost = detailCost;
	}
	
	public PlanMigration getPlanMigration() {
		return this.migration;
	}
}
