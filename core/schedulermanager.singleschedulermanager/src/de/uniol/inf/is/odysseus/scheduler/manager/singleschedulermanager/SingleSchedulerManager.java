/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.scheduler.manager.singleschedulermanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.scheduler.manager.AbstractSchedulerManager;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;

/**
 * Standard {@link ISchedulerManager} for odysseus. The manger uses OSGi
 * services for scheduling modules. Only one {@link IScheduler} is active at the
 * same time.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class SingleSchedulerManager extends AbstractSchedulerManager implements
		IInfoProvider, IPlanModificationListener {

	static Logger _logger = null;

	static synchronized Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(SingleSchedulerManager.class);
		}
		return _logger;
	}

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

	Properties props = new Properties();
	boolean configSchedulerDirty = false;
	boolean configSchedulingDirty = false;
	boolean activated = false;

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
		String defaultScheduler = null;
		String defaultStrat = null;

		// create default scheduler
		if (schedulers != null && strats != null) {

			try {
				File f = OdysseusDefaults.openOrCreateFile(OdysseusDefaults
						.get("schedulingConfigFile"));
				FileInputStream in;
				in = new FileInputStream(f);
				props.load(in);
				in.close();

				if (props.getProperty("defaultScheduler") == null
						|| props.getProperty("defaultScheduler").length() == 0) {
					logger.info("No Scheduler-Config-File found.");
					props.setProperty("defaultScheduler", schedulers.iterator()
							.hasNext() ? schedulers.iterator().next() : null);
					props.setProperty("defaultStrat", strats.iterator()
							.hasNext() ? strats.iterator().next() : null);
					FileOutputStream out;
					try {
						out = new FileOutputStream(
								OdysseusDefaults.get("schedulingConfigFile"));
						props.store(out,
								"--- Scheduling Property File edit only if you know what you are doing ---");
						out.close();
						logger.info("New Scheduler-Config-File created");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}

				defaultScheduler = props.getProperty("defaultScheduler");
				defaultStrat = props.getProperty("defaultStrat");

				setActiveScheduler(defaultScheduler, defaultStrat, null);

				this.logger.info("Active scheduler. "
						+ this.activeScheduler.getClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		activated = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * setActiveScheduler(java.lang.String, java.lang.String,
	 * de.uniol.inf.is.odysseus.scheduler.manager.IScheduleable)
	 */
	@Override
	public void setActiveScheduler(String schedulerToSet,
			String schedulingStrategyToSet, IExecutionPlan executionPlan) {
		setActiveScheduler(schedulerToSet, schedulingStrategyToSet,
				executionPlan.getLeafSources(), executionPlan.getPartialPlans());
	}

	private void setActiveScheduler(String schedulerToSet,
			String schedulingStrategyToSet,
			List<IIterableSource<?>> leafSources,
			List<IPartialPlan> partialPlans) {
		Set<String> schedulers = getScheduler();
		Set<String> strats = getSchedulingStrategy();

		boolean wasRunning = false;

		if (activeScheduler != null && activeScheduler.isRunning()) {
			activeScheduler.stopScheduling();
			wasRunning = true;
		}

		// Test if this scheduler is loaded
		if (!schedulers.contains(schedulerToSet)) {
			logger.debug(schedulerToSet + " not loaded (now)");
			configSchedulerDirty = true;
			schedulerToSet = schedulers.iterator().hasNext() ? schedulers
					.iterator().next() : null;
			logger.debug("using default " + schedulerToSet);
		}
		if (!strats.contains(schedulingStrategyToSet)) {
			logger.debug(schedulingStrategyToSet + " not loaded (now)");
			configSchedulingDirty = true;
			schedulingStrategyToSet = strats.iterator().hasNext() ? strats
					.iterator().next() : null;
			logger.debug("using default " + schedulingStrategyToSet);
		}

		try {
			if (this.activeScheduler != null) {
				fire(new SchedulerManagerEvent(this,
						SchedulerManagerEventType.SCHEDULER_REMOVED,
						this.activeScheduler));
			}
			logger.debug("Set active Scheduler " + schedulerToSet + " ("
					+ schedulingStrategyToSet + ")");
			// create a new scheduler an set the error listener
			this.activeScheduler = createScheduler(schedulerToSet,
					schedulingStrategyToSet);
			this.activeScheduler.addErrorEventListener(this);
			fire(new SchedulerManagerEvent(this,
					SchedulerManagerEventType.SCHEDULER_SET,
					this.activeScheduler));
			this.activeSchedulingStrategy = schedulingStrategyToSet;
			this.activeSchedulerString = schedulerToSet;
			if (wasRunning) {
				activeScheduler.startScheduling();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// refresh the scheduling
		if (leafSources != null || partialPlans != null) {
			try {
				refreshScheduling(leafSources, partialPlans);
			} catch (NoSchedulerLoadedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.IInfoProvider#getInfos()
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
	public void refreshScheduling(IExecutionPlan execPlan)
			throws NoSchedulerLoadedException {
		refreshScheduling(execPlan.getLeafSources(), execPlan.getPartialPlans());
	}

	public void refreshScheduling(List<IIterableSource<?>> leafSources,
			List<IPartialPlan> partialPlans) throws NoSchedulerLoadedException{
		this.logger.debug("Refresh Scheduling. Set Sources");
		this.activeScheduler.setLeafSources(leafSources);
		this.logger.debug("Refresh Scheduling. Set Partial Plans");
		this.activeScheduler.setPartialPlans(partialPlans);
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
	public String getActiveSchedulerID() {
		return activeSchedulerString;
	}

	@Override
	public IScheduler getActiveScheduler() {
		return activeScheduler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * getActiveSchedulingStrategy()
	 */
	@Override
	public String getActiveSchedulingStrategyID() {
		return activeSchedulingStrategy;
	}

	@Override
	public synchronized void schedulingsChanged() {
		logger.debug("SchedulingChanged " + configSchedulerDirty + " "
				+ configSchedulingDirty);
		if (configSchedulerDirty || configSchedulingDirty) {

			Set<String> schedulers = getScheduler();
			Set<String> strats = getSchedulingStrategy();
			// logger.debug(""+strats);
			String defaultScheduler = props.getProperty("defaultScheduler");
			String defaultStrat = props.getProperty("defaultStrat");
			// logger.debug(" "+defaultScheduler+" "+defaultStrat);

			if (configSchedulerDirty) {
				// Test if this scheduler is loaded
				if (!schedulers.contains(defaultScheduler)) {
					configSchedulerDirty = true;
					defaultScheduler = schedulers.iterator().hasNext() ? schedulers
							.iterator().next() : null;
				} else {
					configSchedulerDirty = false;
					logger.debug("using Scheduler " + defaultScheduler);
				}
			}

			if (configSchedulingDirty) {
				if (!strats.contains(defaultStrat)) {
					configSchedulingDirty = true;
					defaultStrat = strats.iterator().hasNext() ? strats
							.iterator().next() : null;
				} else {
					configSchedulingDirty = false;
					logger.debug("Using Scheduling Strategy " + defaultStrat);
				}
			}

			if (!configSchedulerDirty && !configSchedulingDirty) {
				logger.debug("Set Scheduler " + defaultScheduler + " "
						+ defaultStrat);

				setActiveScheduler(defaultScheduler, defaultStrat,
						activeScheduler.getLeafSources(),
						activeScheduler.getPartialPlans());
			}
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (this.activeScheduler instanceof IPlanModificationListener) {
			((IPlanModificationListener) this.activeScheduler)
					.planModificationEvent(eventArgs);
		}
	}

}
