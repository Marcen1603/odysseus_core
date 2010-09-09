package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

/**
 * Cost model that provides rating information for it's {@link ICostCalculator}.
 * 
 * @author Tobias Witt
 * 
 */
public interface IPlanExecutionCostModel {
	/**
	 * 
	 * @return {@link ICostCalculator} for rating execution cost of plans.
	 */
	public ICostCalculator<IPhysicalOperator> getCostCalculator();
}
