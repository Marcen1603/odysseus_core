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

	@Override
	public ICost<PlanMigration> calculateCost(PlanMigration candidate) {
		// TODO Vergleich erst sinnvoll, wenn mehrere Planmigrationsstrategien verfuegbar sind.
		ICost<PlanMigration> cost = new StandardPlanMigrationCost();
		cost.setScore(100);
		return cost;
	}

}
