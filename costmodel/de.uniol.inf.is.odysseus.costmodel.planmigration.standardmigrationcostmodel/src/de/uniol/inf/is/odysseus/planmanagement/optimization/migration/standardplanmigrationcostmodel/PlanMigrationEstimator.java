/**
 * 
 */
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardplanmigrationcostmodel;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.planmigration.IPlanMigrationDetailCost;
import de.uniol.inf.is.odysseus.costmodel.planmigration.IPlanMigrationEstimator;
import de.uniol.inf.is.odysseus.costmodel.planmigration.PlanMigrationDetailCost;
import de.uniol.inf.is.odysseus.costmodel.planmigration.PlanMigrationEstimation;

/**
 * @author Merlin Wasmann
 * 
 */
public class PlanMigrationEstimator implements IPlanMigrationEstimator {

	private ICostModel<IPhysicalOperator> costmodel = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.costmodel.planmigration.IPlanMigrationEstimator
	 * #estimatePlanMigration
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement
	 * .optimization.planmigration.costmodel.PlanMigration)
	 */
	@Override
	public PlanMigrationEstimation estimatePlanMigration(PlanMigration migration) {
		// TODO: migrationsstrategien unterscheiden!
		PlanMigrationEstimation estimation = new PlanMigrationEstimation(
				migration);

		// estimate old plan
		List<IPhysicalOperator> plans = new ArrayList<IPhysicalOperator>();
		plans.add(migration.getOldPlan());
		OperatorCost<IPhysicalOperator> costOld = (OperatorCost<IPhysicalOperator>) this.costmodel
				.estimateCost(plans, true);
		// estimate new plan
		plans.clear();
		plans.add(migration.getNewPlan());
		OperatorCost<IPhysicalOperator> costNew = (OperatorCost<IPhysicalOperator>) this.costmodel
				.estimateCost(plans, true);

		// add old to new
		OperatorCost<IPhysicalOperator> parallel = (OperatorCost<IPhysicalOperator>) costOld
				.merge(costNew);

		IPlanMigrationDetailCost detailCost = new PlanMigrationDetailCost(
				migration, parallel.getMemCost(), parallel.getCpuCost(), 10000);
		
		estimation.setDetailCost(detailCost);
		return estimation;
	}

	public void bindCostModel(ICostModel<IPhysicalOperator> model) {
		this.costmodel = model;
	}

	public void unbindCostModel(ICostModel<IPhysicalOperator> model) {
		if (model.equals(this.costmodel)) {
			this.costmodel = null;
		}
	}

}
