package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public interface IPartialPlanScheduling {

	void clear();

	void addPlan(IScheduling scheduling);

	int planCount();

	IPartialPlanScheduling clone();

	IScheduling nextPlan();

	void removeCurrent();

}
