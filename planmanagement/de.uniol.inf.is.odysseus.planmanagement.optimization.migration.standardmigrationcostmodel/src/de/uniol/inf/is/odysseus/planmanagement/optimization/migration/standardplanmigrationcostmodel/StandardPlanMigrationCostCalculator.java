package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardplanmigrationcostmodel;

import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.AbstractCostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;

/**
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
