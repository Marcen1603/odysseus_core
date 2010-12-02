package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public interface IPartialPlanScheduling extends IClone{

	void clear();

	void addPlan(IScheduling scheduling);

	int planCount();

	IPartialPlanScheduling clone();

	IScheduling nextPlan();

	//TODO wieder auf removeCurrent umbauen, atm unpassende bennenung fuer RR
	void removePlan(IScheduling plan);

}
