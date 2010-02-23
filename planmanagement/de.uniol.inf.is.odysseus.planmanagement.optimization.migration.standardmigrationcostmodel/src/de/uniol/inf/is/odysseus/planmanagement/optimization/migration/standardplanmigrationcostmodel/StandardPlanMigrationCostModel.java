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
		return new StandardPlanMigrationCostCalculator();
	}

}
