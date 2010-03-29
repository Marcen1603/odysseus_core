package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardplanmigrationcostmodel;

import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICost;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.PlanMigration;

/**
 * 
 * @author Tobias Witt
 *
 */
public class StandardPlanMigrationCost implements ICost<PlanMigration> {
	
	private int score;

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(ICost<PlanMigration> o) {
		return o.getScore() - this.getScore();
	}

}
