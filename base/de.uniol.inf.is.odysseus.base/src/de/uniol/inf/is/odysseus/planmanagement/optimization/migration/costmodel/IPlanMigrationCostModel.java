package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

/**
 * Cost model that provides rating information for it's {@link ICostCalculator}.
 * 
 * @author Tobias Witt
 *
 */
public interface IPlanMigrationCostModel {
	/**
	 * 
	 * @return {@link ICostCalculator} for rating plan migration cost.
	 */
	public ICostCalculator<PlanMigration> getCostCalculator();
}
