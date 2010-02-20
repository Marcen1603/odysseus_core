package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie;

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
	private IPlanMigrationStrategie strategy;

	public PlanMigration(IPhysicalOperator oldPlan, IPhysicalOperator newPlan,
			IPlanMigrationStrategie strategy) {
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

	public IPlanMigrationStrategie getStrategy() {
		return strategy;
	}

	public void setStrategy(IPlanMigrationStrategie strategy) {
		this.strategy = strategy;
	}

}
