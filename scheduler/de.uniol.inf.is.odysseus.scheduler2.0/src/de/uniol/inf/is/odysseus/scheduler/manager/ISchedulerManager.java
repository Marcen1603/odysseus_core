package de.uniol.inf.is.odysseus.scheduler.manager;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;

public interface ISchedulerManager extends IInfoProvider, IErrorEventHandler,
		IErrorEventListener {
	public void startScheduling() throws NoSchedulerLoadedException, OpenFailedException;

	public void stopScheduling() throws NoSchedulerLoadedException;

	public void setTimeSlicePerStrategy(long time)
			throws NoSchedulerLoadedException;

	public boolean isRunning() throws NoSchedulerLoadedException;

	public void refreshScheduling(IScheduleable executivePlan)
			throws NoSchedulerLoadedException;
	
	public void setSchedulerCount(int schedulerCount);
	
	public int getSchedulerCount();
	
	public Set<String> getScheduler();
	
	public Set<String> getSchedulingStrategy();
	
	//TODO: So nur sinnvoll, wenn nur ein Scheduler gleichzeitig ... sollte aber Standardfall sein ...
	public void setActiveScheduler(String activeScheduler,	String activeSchedulingStrategy, IScheduleable scheduleInfos);
	public String getActiveScheduler();
	public String getActiveSchedulingStrategy();
	
	
}
