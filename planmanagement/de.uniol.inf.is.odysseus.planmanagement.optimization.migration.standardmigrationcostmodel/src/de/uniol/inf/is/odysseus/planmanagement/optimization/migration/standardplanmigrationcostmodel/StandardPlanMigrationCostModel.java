package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardplanmigrationcostmodel;

import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanMigrationCostModel;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;

/**
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
