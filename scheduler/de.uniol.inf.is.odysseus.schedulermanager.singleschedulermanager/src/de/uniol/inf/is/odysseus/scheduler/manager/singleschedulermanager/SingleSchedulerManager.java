package de.uniol.inf.is.odysseus.scheduler.manager.singleschedulermanager;

import java.util.Set;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.scheduler.manager.AbstractSchedulerManager;
import de.uniol.inf.is.odysseus.scheduler.manager.IScheduleable;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;

/**
 * Standard {@link ISchedulerManager} for odysseus. The manger uses OSGi
 * services for scheduling modules. Only one {@link IScheduler} is active at the
 * same time.
 * 
 * @author Wolf Bauer
 * 
 */
public class SingleSchedulerManager extends AbstractSchedulerManager implements
		IInfoProvider {

	/**
	 * The current active {@link IScheduler}. 
	 */
	private IScheduler activeScheduler;
	
	/**
	 * The current active scheduling strategy as id. 
	 */
	private String activeSchedulingStrategy;
	
	/**
	 * The current active scheduler as id. 
	 */
	private String activeSchedulerString;

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 */
	@SuppressWarnings("unused")
	private void activate() {
		this.logger.info("Activate schedulererManager.");
		Set<String> schedulers = getScheduler();
		Set<String> strats = getSchedulingStrategy();

		// create default scheduler
		if (schedulers != null && strats != null) {
			String defaultScheduler = schedulers.iterator().hasNext() ? schedulers
					.iterator().next()
					: null;
			String defaultStrat = strats.iterator().hasNext() ? strats
					.iterator().next() : null;
			setActiveScheduler(defaultScheduler, defaultStrat, null);
		}
		this.logger
				.info("Active scheduler. " + this.activeScheduler.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * setActiveScheduler(java.lang.String, java.lang.String,
	 * de.uniol.inf.is.odysseus.scheduler.manager.IScheduleable)
	 */
	@Override
	public void setActiveScheduler(String activeScheduler,
			String activeSchedulingStrategy, IScheduleable scheduleInfos) {
		try {
			// create a new scheduler an set the error listener
			this.activeScheduler = createScheduler(activeScheduler,
					activeSchedulingStrategy);
			this.activeScheduler.addErrorEventListener(this);
			this.activeSchedulingStrategy = activeSchedulingStrategy;
			this.activeSchedulerString = activeScheduler;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// refresh the scheduling
		if (scheduleInfos != null) {
			try {
				refreshScheduling(scheduleInfos);
			} catch (NoSchedulerLoadedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.IInfoProvider#getInfos()
	 */
	@Override
	public String getInfos() {
		String infos = "<SchedulerManager class=\"" + this + "\"> ";

		infos += getInfoString(this.activeScheduler, "ActiveScheduler");

		for (String scheduler : getScheduler()) {
			infos += getInfoString(scheduler, "Additional SchedulerFactory: ");
		}

		infos += AppEnv.LINE_SEPARATOR + "</SchedulerManager> ";

		return infos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#isRunning()
	 */
	@Override
	public boolean isRunning() throws NoSchedulerLoadedException {
		return this.activeScheduler.isRunning();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * setTimeSlicePerStrategy(long)
	 */
	@Override
	public void setTimeSlicePerStrategy(long time)
			throws NoSchedulerLoadedException {
		this.activeScheduler.setTimeSlicePerStrategy(time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#startScheduling
	 * ()
	 */
	@Override
	public void startScheduling() throws NoSchedulerLoadedException,
			OpenFailedException {
		if (!this.activeScheduler.isRunning()) {
			this.logger.debug("Start scheduling.");
			this.activeScheduler.startScheduling();
			this.logger.debug("Scheduling started.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#stopScheduling
	 * ()
	 */
	@Override
	public void stopScheduling() throws NoSchedulerLoadedException {
		if (this.activeScheduler.isRunning()) {
			this.logger.debug("Stop scheduling.");
			this.activeScheduler.stopScheduling();
			this.logger.debug("Scheduling stopped.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * refreshScheduling
	 * (de.uniol.inf.is.odysseus.scheduler.manager.IScheduleable)
	 */
	@Override
	public void refreshScheduling(IScheduleable scheduleInfos)
			throws NoSchedulerLoadedException {
		this.logger.debug("Refresh Scheduling. Set Sources");
		this.activeScheduler.setSources(scheduleInfos.getExecutionPlan()
				.getSources());
		this.logger.debug("Refresh Scheduling. Set Partial Plans");
		this.activeScheduler.setPartialPlans(scheduleInfos.getExecutionPlan()
				.getPartialPlans());
		this.logger.debug("Refresh Scheduling. Done");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * setSchedulerCount(int)
	 */
	@Override
	public void setSchedulerCount(int schedulerCount) {
		// do nothing use only one scheduler
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * getActiveScheduler()
	 */
	@Override
	public String getActiveScheduler() {
		return activeSchedulerString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * getActiveSchedulingStrategy()
	 */
	@Override
	public String getActiveSchedulingStrategy() {
		return activeSchedulingStrategy;
	}
}
