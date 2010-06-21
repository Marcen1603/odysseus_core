package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public interface IPlanScheduling {

	void clear();

	void addPlan(IScheduling scheduling);

	int planCount();

	IPlanScheduling clone();

	IScheduling nextPlan();

	void removeCurrent();

}
