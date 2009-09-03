package de.uniol.inf.is.odysseus.scheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public interface IScheduler extends IErrorEventHandler {

	public void startScheduling();

	public void stopScheduling();

	public void setTimeSlicePerStrategy(long time);

	public boolean isRunning();
	
	public 	void setSources(List<IIterableSource<?>> sources);
	
	public List<IIterableSource<?>> getSources();
	
	public void setPartialPlans(List<IPartialPlan> partialPlans);
	
	public List<IPartialPlan> getPartialPlans();
}
