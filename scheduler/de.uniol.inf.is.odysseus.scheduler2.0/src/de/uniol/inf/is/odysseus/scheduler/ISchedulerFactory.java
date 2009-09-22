package de.uniol.inf.is.odysseus.scheduler;

import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingStrategyFactory;

/**
 * Describes an object which creates specific {@link IScheduler}. Each
 * {@link IScheduler} is initialized with a {@link ISchedulingStrategyFactory}.
 * A factory can be identified by a name. This name should be unique.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulerFactory {
	/**
	 * Creates a specific {@link IScheduler}. Each {@link IScheduler} is
	 * initialized with a {@link ISchedulingStrategyFactory}.
	 * 
	 * @param schedulingStrategy
	 *            {@link ISchedulingStrategyFactory} which will be used for
	 *            creating new {@link IScheduler}.
	 * @return A new specific {@link IScheduler} instance.
	 */
	public IScheduler createScheduler(
			ISchedulingStrategyFactory schedulingStrategy);

	/**
	 * Returns a name for this factory. This name should be unique.
	 * 
	 * @return Name of this factory.
	 */
	public String getName();
}
