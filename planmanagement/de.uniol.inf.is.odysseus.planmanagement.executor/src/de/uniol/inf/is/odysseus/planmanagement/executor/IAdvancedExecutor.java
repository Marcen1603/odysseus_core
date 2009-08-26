package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Set;

public interface IAdvancedExecutor extends IExecutor {
	public Set<String> getRegisteredBufferPlacementStrategies();
	public void setDefaultBufferPlacementStrategy(String strategy);
	public Set<String> getRegisteredSchedulingStrategyFactories();
	public Set<String> getRegisteredSchedulerFactories();
	public void setScheduler(String scheduler, String schedulerStrategy);
	public String getCurrentScheduler();
	public String getCurrentSchedulingStrategy();
}
