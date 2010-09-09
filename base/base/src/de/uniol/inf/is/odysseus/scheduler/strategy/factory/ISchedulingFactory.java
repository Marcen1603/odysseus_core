package de.uniol.inf.is.odysseus.scheduler.strategy.factory;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

/**
 * Describes a Factory for creating specific {@link IScheduling}. Used
 * for OSGi services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulingFactory {
	/**
	 * Create a specific {@link IScheduling}. Initialize it with a
	 * physical plan which should be scheduled with a priority.
	 * 
	 * @param plan
	 *            Physical plan which should be scheduled.
	 * @param priority
	 *            Priority with which physical plan which should be scheduled.
	 * @return New created a specific {@link IScheduling}.
	 */
	public IScheduling create(IPartialPlan plan, int priority);

	/**
	 * ID of this factory. Should be unique.
	 * 
	 * @return ID of this factory. Should be unique.
	 */
	public String getName();
}
