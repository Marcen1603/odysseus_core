package de.uniol.inf.is.odysseus.scheduler.manager.singleschedulermanager;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.scheduler.manager.AbstractSchedulerManager;
import de.uniol.inf.is.odysseus.scheduler.manager.IScheduleable;

public class SingleSchedulerManager extends AbstractSchedulerManager implements
		IInfoProvider {

	private IScheduler activeScheduler;
	private String activeSchedulingStrategy;
	private String activeSchedulerString;
	
	@SuppressWarnings("unused")
	private void activate() {
		this.logger.info("Activate schedulererManager.");
		Set<String> schedulers = getScheduler();
		Set<String> strats = getSchedulingStrategy();
		
		if (schedulers!= null && strats!=null) {
			String defaultScheduler = schedulers.iterator().hasNext()? schedulers.iterator().next():null;
			String defaultStrat = 	strats.iterator().hasNext()? strats.iterator().next():null;
			setActiveScheduler(defaultScheduler,defaultStrat,null);		
		}
		this.logger.info("Active scheduler. " + this.activeScheduler.getClass());
	}

	@Override
	public void setActiveScheduler(String activeScheduler,
			String activeSchedulingStrategy, IScheduleable scheduleInfos) {
		try{
			this.activeScheduler = createScheduler(activeScheduler, activeSchedulingStrategy);
			this.activeScheduler.addErrorEventListener(this);
			this.activeSchedulingStrategy = activeSchedulingStrategy;
			this.activeSchedulerString = activeScheduler;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if (scheduleInfos != null){
			try {
				refreshScheduling(scheduleInfos);
			} catch (NoSchedulerLoadedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public String getInfos() {
		String infos = "<SchedulerManager class=\"" + this + "\"> ";

		infos += getInfoString(this.activeScheduler, "ActiveScheduler");

		for (String scheduler : getScheduler()) {
			infos += getInfoString(scheduler,
					"Additional SchedulerFactory: ");
		}

		infos += AppEnv.LINE_SEPERATOR + "</SchedulerManager> ";

		return infos;
	}

	@Override
	public boolean isRunning() throws NoSchedulerLoadedException {
		return this.activeScheduler.isRunning();
	}

	@Override
	public void setTimeSlicePerStrategy(long time)
			throws NoSchedulerLoadedException {
		this.activeScheduler.setTimeSlicePerStrategy(time);
	}

	@Override
	public void startScheduling() throws NoSchedulerLoadedException, OpenFailedException {
		if (!this.activeScheduler.isRunning()) {
			this.logger.debug("Start scheduling.");
			this.activeScheduler.startScheduling();
			this.logger.debug("Scheduling started.");
		}
	}

	@Override
	public void stopScheduling() throws NoSchedulerLoadedException {
		if (this.activeScheduler.isRunning()) {
			this.logger.debug("Stop scheduling.");
			this.activeScheduler.stopScheduling();
			this.logger.debug("Scheduling stopped.");
		}
	}

	@Override
	public void refreshScheduling(IScheduleable	scheduleInfos)
			throws NoSchedulerLoadedException {
		this.activeScheduler.setSources(scheduleInfos.getExecutionPlan().getSources());
		this.activeScheduler.setPartialPlans(scheduleInfos.getExecutionPlan().getPartialPlans());
	}

	@Override
	public void setSchedulerCount(int schedulerCount) {
		// do nothing use only one scheduler
	}

	@Override
	public String getActiveScheduler() {
		return activeSchedulerString;
	}

	@Override
	public String getActiveSchedulingStrategy() {
		// TODO Auto-generated method stub
		return activeSchedulingStrategy;
	}
}
