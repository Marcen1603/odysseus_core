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

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.costmodel.planmigration.IPlanMigrationEstimator;
import de.uniol.inf.is.odysseus.costmodel.planmigration.PlanMigrationCost;
import de.uniol.inf.is.odysseus.costmodel.planmigration.PlanMigrationEstimation;

/**
 * Defines the cost of an plan migration.
 * 
 * @author Tobias Witt
 * 
 */
public class StandardPlanMigrationCostModel implements IPlanMigrationCostModel {

	// FIXME: Änderungen sind beim revert verloren gegangen.

	IPlanMigrationEstimator estimator = null;

	@Override
	public ICost<PlanMigration> estimateCost(List<PlanMigration> operators,
			boolean onUpdate) {
		PlanMigration migration = operators.get(0);
		PlanMigrationEstimation estimation = this.estimator
				.estimatePlanMigration(migration);
		return new PlanMigrationCost(migration, estimation, estimation
				.getDetailCost().getMemoryCost(), estimation.getDetailCost()
				.getProcessorCost(), estimation.getDetailCost().getDuration());
	}

	@Override
	public ICost<PlanMigration> getMaximumCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICost<PlanMigration> getOverallCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICost<PlanMigration> getZeroCost() {
		// TODO Auto-generated method stub
		return null;
	}

	public void bindPlanMigrationEstimator(IPlanMigrationEstimator estimator) {
		this.estimator = estimator;
	}

	public void unbindPlanMigrationEstimator(IPlanMigrationEstimator estimator) {
		if (estimator.equals(this.estimator)) {
			this.estimator = null;
		}
	}

}
