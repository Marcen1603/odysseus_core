package de.uniol.inf.is.odysseus.scheduler;

import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Describes an object which creates specific {@link IScheduler}. Each
 * {@link IScheduler} is initialized with a {@link ISchedulingFactory}.
 * A factory can be identified by a name. This name should be unique.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulerFactory {
	/**
	 * Creates a specific {@link IScheduler}. Each {@link IScheduler} is
	 * initialized with a {@link ISchedulingFactory}.
	 * 
	 * @param schedulingStrategy
	 *            {@link ISchedulingFactory} which will be used for
	 *            creating new {@link IScheduler}.
	 * @return A new specific {@link IScheduler} instance.
	 */
	public IScheduler createScheduler(
			ISchedulingFactory schedulingStrategy);

	/**
	 * Returns a name for this factory. This name should be unique.
	 * 
	 * @return Name of this factory.
	 */
	public String getName();
}
