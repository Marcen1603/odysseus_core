package de.uniol.inf.is.odysseus.scheduler.strategy.factory;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;

public interface ISchedulingStrategyFactory {
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority);
	public String getName();
}
