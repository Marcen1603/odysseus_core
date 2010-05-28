package de.uniol.inf.is.odysseus.scheduler.strategy;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public interface IScheduling {
	public boolean schedule(long maxTime);
	public IPartialPlan getPlan();
}
