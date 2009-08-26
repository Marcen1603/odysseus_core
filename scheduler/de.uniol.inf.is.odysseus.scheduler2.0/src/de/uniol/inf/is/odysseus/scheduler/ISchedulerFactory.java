package de.uniol.inf.is.odysseus.scheduler;

import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingStrategyFactory;

public interface ISchedulerFactory {
	public IScheduler createScheduler(ISchedulingStrategyFactory schedulingStrategy);
	public String getName();
}
