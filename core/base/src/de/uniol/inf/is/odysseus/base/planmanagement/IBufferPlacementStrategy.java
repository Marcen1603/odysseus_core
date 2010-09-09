package de.uniol.inf.is.odysseus.base.planmanagement;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;

/**
 * Describes a strategy for buffer placement in physical plans. A buffer is an
 * operator between two operators which stores data elements.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IBufferPlacementStrategy {
	/**
	 * Add buffer in a physical plan.
	 * 
	 * @param plan
	 *            Physical plan in which buffer should be placend.
	 */
	public void addBuffers(IPhysicalOperator plan);

	/**
	 * Returns the name of this strategy. This name should be unique.
	 * 
	 * @return The name of this strategy. This name should be unique.
	 */
	public String getName();
}
