package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simplemigrationcostmodel;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;

/**
 * 
 * @author Tobias Witt
 *
 */
public class PlanMigrationCostCalculator implements ICostCalculator<PlanMigration> {

	@Override
	public ICost<PlanMigration> calculateCost(PlanMigration candidate) {
		// TODO Auto-generated method stub
		return new PlanMigrationCost();
	}

	@Override
	public PlanMigration pickBest(Collection<PlanMigration> candidates) {
		// TODO Auto-generated method stub
		return candidates.iterator().next();
	}

	@Override
	public List<PlanMigration> pickBest(Collection<PlanMigration> candidates,
			int num) {
		// TODO Auto-generated method stub
		return null;
	}

}
