package de.uniol.inf.is.odysseus.scheduler.strategy;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;

public interface IScheduling {
	public boolean schedule(long maxTime);
	public IPartialPlan getPlan();
	boolean isSchedulingPaused();
	boolean isSchedulingBlocked();
	public void addSchedulingEventListener(ISchedulingEventListener eventListener);
	public void removeSchedulingEventListener(ISchedulingEventListener eventListener);
	public boolean isSchedulable();
}
