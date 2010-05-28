package de.uniol.inf.is.odysseus.scheduler.strategy;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;

public interface IScheduling {
	public boolean schedule(long maxTime);
	public IPartialPlan getPlan();
	public void addSchedulingEventListener(ISchedulingEventListener eventListener);
	public void removeSchedulingEventListener(ISchedulingEventListener eventListener);
}
