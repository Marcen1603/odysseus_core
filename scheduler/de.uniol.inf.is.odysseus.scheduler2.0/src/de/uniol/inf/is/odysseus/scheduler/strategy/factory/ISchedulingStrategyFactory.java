package de.uniol.inf.is.odysseus.scheduler.strategy.factory;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;

/**
 * Describes a Factory for creating specific {@link ISchedulingStrategy}. Used
 * for OSGi services.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulingStrategyFactory {
	/**
	 * Create a specific {@link ISchedulingStrategy}. Initialize it with a
	 * physical plan which should be scheduled with a priority.
	 * 
	 * @param plan
	 *            Physical plan which should be scheduled.
	 * @param priority
	 *            Priority with which physical plan which should be scheduled.
	 * @return New created a specific {@link ISchedulingStrategy}.
	 */
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority);

	/**
	 * ID of this factory. Should be unique.
	 * 
	 * @return ID of this factory. Should be unique.
	 */
	public String getName();
}
