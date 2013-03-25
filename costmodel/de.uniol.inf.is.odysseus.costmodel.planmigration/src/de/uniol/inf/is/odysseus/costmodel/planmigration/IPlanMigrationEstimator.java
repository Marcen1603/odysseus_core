/**
 * 
 */
package de.uniol.inf.is.odysseus.costmodel.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;

/**
 * Represents the estimator for a plan migration based on a plan migration
 * strategy and a physical query.
 * 
 * @author merlin (Nach Vorbild von Timo Michelsen)
 * 
 */
public interface IPlanMigrationEstimator {

	/**
	 * Estimate the costs of the given PlanMigration. Uses the size of the plans
	 * and the execution costs of both plans. Everything is based on the plan
	 * migration strategy.
	 * 
	 * @param migration
	 *            Container containing all information about the plan migration
	 *            to estimate.
	 * 
	 * @return The estimation for this PlanMigration containing all information
	 *         about it.
	 */
	public PlanMigrationEstimation estimatePlanMigration(PlanMigration migration);
	
	
	public Class<? extends IPlanMigrationStrategy> getPlanMigrationStrategyClass();
}
