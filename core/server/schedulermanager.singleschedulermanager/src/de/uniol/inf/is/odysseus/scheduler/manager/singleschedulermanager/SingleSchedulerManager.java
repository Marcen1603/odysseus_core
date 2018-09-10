/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.AbstractSchedulerManager;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.util.FileUtils;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

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

	static Logger logger = LoggerFactory
			.getLogger(SingleSchedulerManager.class);

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

	private Map<IIterableSource<?>, Integer> sourceUsage = new HashMap<>();

	private boolean shouldRun;

	private SessionManagement sessionManagement;

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 */
	@SuppressWarnings("unused")
	private void activate() {
		logger.trace("Activate schedulererManager.");
		Set<String> schedulers = getScheduler();
		Set<String> strats = getSchedulingStrategy();
		String defaultScheduler = null;
		String defaultStrat = null;

		// create default scheduler
		if (schedulers != null && strats != null) {

			try {
				File f = FileUtils.openOrCreateFile(config.get("schedulingConfigFile"));
				FileInputStream in;
				in = new FileInputStream(f);
				props.load(in);
				in.close();

				if (props.getProperty("defaultScheduler") == null
						|| props.getProperty("defaultScheduler").length() == 0) {
					logger.info("No Scheduler-Config-File found.");
					// the first scheduler that is found, is not the best one typically
					// set defaults to fixed values that should always be part of Odysseus
					
					props.setProperty("defaultScheduler", "Single Thread Scheduler RR Multi Source");
					props.setProperty("defaultStrat", "Aurora Min Latency");
					FileOutputStream out;
					try {
						out = new FileOutputStream(config.get("schedulingConfigFile"));
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

				logger.info("Active scheduler. "
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
	 * de.uniol.inf.is.odysseus.core.server.scheduler.manager.IScheduleable)
	 */
	@Override
	public void setActiveScheduler(String schedulerToSet,
			String schedulingStrategyToSet, IExecutionPlan executionPlan) {
		List<IIterableSource<?>> leafSources = executionPlan != null ? executionPlan
				.getLeafSources(superUser()) : null;
		Collection<IPhysicalQuery> partialPlans = executionPlan != null ? executionPlan
				.getQueries(superUser()) : null;
		setActiveScheduler(schedulerToSet, schedulingStrategyToSet,
				leafSources, partialPlans);
	}

	private void setActiveScheduler(String schedulerToSet,
			String schedulingStrategyToSet,
			List<IIterableSource<?>> leafSources,
			Collection<IPhysicalQuery> partialPlans) {
		Set<String> schedulers = getScheduler();
		Set<String> strats = getSchedulingStrategy();


		if (activeScheduler != null && activeScheduler.isRunning()) {
			activeScheduler.stopScheduling();
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
			if (shouldRun) {
				activeScheduler.startScheduling();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// refresh the scheduling
		if (leafSources != null || partialPlans != null) {
			refreshScheduling(leafSources, partialPlans);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider#getInfos
	 * ()
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
	 * de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager
	 * #isRunning()
	 */
	@Override
	public boolean isRunning() throws NoSchedulerLoadedException {
		return activeScheduler != null && this.activeScheduler.isRunning();
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
	 * de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager
	 * #startScheduling ()
	 */
	@Override
	public void startScheduling() throws NoSchedulerLoadedException,
			OpenFailedException {
		shouldRun = true;
		if (this.activeScheduler != null){
			if (!this.activeScheduler.isRunning()) {
				logger.debug("Start scheduling.");
				this.activeScheduler.startScheduling();
				logger.debug("Scheduling started.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager
	 * #stopScheduling ()
	 */
	@Override
	public void stopScheduling() throws NoSchedulerLoadedException {
		shouldRun = false;
		if (this.activeScheduler.isRunning()) {
			logger.debug("Stop scheduling.");
			this.activeScheduler.stopScheduling();
			logger.debug("Scheduling stopped.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * refreshScheduling
	 * (de.uniol.inf.is.odysseus.core.server.scheduler.manager.IScheduleable)
	 */
	@Override
	public void refreshScheduling(IExecutionPlan execPlan)
			throws NoSchedulerLoadedException {
		// Update Source/Query assignment
		sourceUsage.clear();
		for (IPhysicalQuery p : execPlan.getQueries(superUser())) {
			increaseSourceUsage(p);
		}
		refreshScheduling(execPlan.getLeafSources(superUser()), execPlan.getQueries(superUser()));
	}

	private void refreshScheduling(List<IIterableSource<?>> leafSources,
			Collection<IPhysicalQuery> partialPlans) {
		// Determine query source assignment
		logger.debug("Refresh Scheduling. Set Sources");
		this.activeScheduler.setLeafSources(leafSources);
		logger.debug("Refresh Scheduling. Set Partial Plans");
		this.activeScheduler.setPartialPlans(partialPlans);
		logger.debug("Refresh Scheduling. Done");
	}

	@Override
	public void addQuery(IPhysicalQuery affectedQuery) {
		// "Moved" to startQuery
		//		logger.debug("AddQuery " + affectedQuery);
//		increaseSourceUsage(affectedQuery);
//		this.activeScheduler.addLeafSources(affectedQuery
//				.getIteratableLeafSources());
//		this.activeScheduler.addPartialPlan(affectedQuery);
	}

	private void increaseSourceUsage(IPhysicalQuery affectedQuery) {
		for (IIterableSource<?> s : affectedQuery.getIteratableLeafSources()) {
			Integer count = sourceUsage.get(s);
			if (count == null) {
				sourceUsage.put(s, 1);
			} else {
				sourceUsage.put(s, count + 1);
			}
		}
	}

	private List<IIterableSource<?>> decreaseSourceUsage(
			IPhysicalQuery affectedQuery) {
		List<IIterableSource<?>> ret = new LinkedList<>();
		synchronized (sourceUsage) {
			for (IIterableSource<?> s : affectedQuery
					.getIteratableLeafSources()) {
				Integer count = sourceUsage.get(s);
				if (count == null) {
					logger.error("Trying to remove not scheduled source" + s);
				} else {
					if (count > 1) {
						sourceUsage.put(s, count - 1);
					} else {
						sourceUsage.remove(s);
						ret.add(s);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public void removeQuery(IPhysicalQuery affectedQuery) {
		// "Move" to stopp query
		//		logger.debug("RemoveQuery " + affectedQuery);
//		this.activeScheduler.removePartialPlan(affectedQuery);
//		List<IIterableSource<?>> toRemove = decreaseSourceUsage(affectedQuery);
//		this.activeScheduler.removeLeafSources(toRemove);
	}

	@Override
	public void startedQuery(IPhysicalQuery affectedQuery) {
		logger.debug("StartedQuery " + affectedQuery);
		increaseSourceUsage(affectedQuery);
		this.activeScheduler.addLeafSources(affectedQuery
				.getIteratableLeafSources());
		this.activeScheduler.addPartialPlan(affectedQuery);
	}

	@Override
	public void stoppedQuery(IPhysicalQuery affectedQuery) {
		logger.debug("Stop Query " + affectedQuery);
		logger.debug("Remove partial plans");
		this.activeScheduler.removePartialPlan(affectedQuery);
		logger.debug("decrease Source usage");
		List<IIterableSource<?>> toRemove = decreaseSourceUsage(affectedQuery);
		logger.debug("Remove Leaf Sources");
		this.activeScheduler.removeLeafSources(toRemove);
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
	
	
	private ISession superUser() {
		return sessionManagement.loginSuperUser(null);
	}
	public void setSessionManagement(SessionManagement sessionManagement) {
		this.sessionManagement = sessionManagement;
	}
}
