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
package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.AbstractScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

/**
 * SimpleThreadScheduler is a scheduler which uses threads for execution the physical execution plan.
 * 
 * For each source there is a thread that retrieves values A configurable number of threads is used the schedule the registered partial plans
 * 
 * The Selection which plans to schedule is based on a selection strategy
 * 
 * @author Wolf Bauer, Marco Grawunder, Thomas Vogelgesang
 * 
 */
abstract public class AbstractSimpleThreadScheduler extends AbstractScheduler implements UncaughtExceptionHandler, IPlanModificationListener {

	private volatile int trainSize = (int) OdysseusConfiguration.instance.getLong("scheduler_trainSize", 1);

	Logger logger = LoggerFactory.getLogger(AbstractSimpleThreadScheduler.class);
	final IPhysicalQueryScheduling[] planScheduling;

	/**
	 * Thread for execution the registered partial plans.
	 */
	protected SchedulingExecutor[] schedulingExecutor;
	protected int nextExecutorToAssign = 0;

	/**
	 * Thread for execution the global sources.
	 */
	protected List<ISourceExecutor> sourceThreads = new CopyOnWriteArrayList<ISourceExecutor>();


	final private Map<IPhysicalQuery, Integer> planScheduledBy = new HashMap<>();
	final private Map<IPhysicalQuery, IScheduling> partialPlanSchedulingMap = new HashMap<>();


	/**
	 * Creates a new SingleThreadScheduler.
	 * 
	 * @param schedulingStrategieFactory
	 *            Factory for creating new scheduling strategies for each partial plan which should be scheduled.
	 * @throws IOException
	 */
	public AbstractSimpleThreadScheduler(ISchedulingFactory schedulingStrategieFactory, IPhysicalQueryScheduling[] planScheduling, OdysseusConfiguration config) {
		super(schedulingStrategieFactory, config);
		this.planScheduling = planScheduling;
	}

	private void initPlanScheduling() {
		if (schedulingExecutor != null) {
			for (SchedulingExecutor exec : schedulingExecutor) {
				exec.setUncaughtExceptionHandler(null);
			}
		}

		schedulingExecutor = new SchedulingExecutor[planScheduling.length];
		for (int i = 0; i < planScheduling.length; i++) {
			schedulingExecutor[i] = new SchedulingExecutor(planScheduling[i], timeSlicePerStrategy, this, trainSize);
			schedulingExecutor[i].setUncaughtExceptionHandler(this);
			schedulingExecutor[i].setPriority(Thread.NORM_PRIORITY);
			schedulingExecutor[i].setName("Scheduling Executor #" + i);
		}
	}

	@Override
	public synchronized void startScheduling() {
		if (isRunning()) {
			throw new SchedulingException("scheduler is already running");
		}
		initPlanScheduling();

		super.startScheduling();
		// Start Source Threads
		for (ISourceExecutor source : sourceThreads) {
			source.start();
		}
		// Start Executor Thread to execute plans
		for (int i = 0; i < schedulingExecutor.length; i++) {
			schedulingExecutor[i].start();
		}
		logger.debug("Starting Scheduler with " + schedulingExecutor.length + " thread(s).");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.scheduler.AbstractScheduler# stopScheduling()
	 */
	@Override
	public synchronized void stopScheduling() {
		if (!isRunning()) {
			throw new SchedulingException("scheduler isn't running");
		}
		// stop the scheduler threads
		logger.debug("Stopping source treads");
		for (ISourceExecutor sourceThread : sourceThreads) {
			sourceThread.interrupt();
			removeSourceThread(sourceThread);
		}
		logger.debug("Stopping scheduler executors");
		for (int i = 0; i < schedulingExecutor.length; i++) {
			schedulingExecutor[i].interrupt();
		}
		
		super.stopScheduling();
		// to allow restart of processing init threads again
		setPartialPlans(partialPlans);
		setLeafSources(sources);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#setPartialPlans (java.util .List)
	 */
	@Override
	protected synchronized void process_setPartialPlans(Collection<IPhysicalQuery> partialPlans) {
		planScheduledBy.clear();
		partialPlanSchedulingMap.clear();
		logger.debug("Setting new Plans to schedule :" + partialPlans);

		for (int i = 0; i < schedulingExecutor.length; i++) {
			schedulingExecutor[i].pause();
			this.planScheduling[i].clear();
		}

		if (partialPlans != null) {
			// Create for each partial plan an own scheduling strategy.
			// These strategies are used for scheduling partial plans.
			// Round Robin assigment to scheduler
			for (IPhysicalQuery partialPlan : partialPlans) {
				addPartialPlan(partialPlan);
			}
		}

		for (int i = 0; i < schedulingExecutor.length; i++) {
			schedulingExecutor[i].endPause();
		}

		logger.debug("setPartialPlans done");
	}
	
	@Override
	public void addPartialPlan(IPhysicalQuery partialPlan) {
		if (!partialPlans.contains(partialPlan)) {
			partialPlans.add(partialPlan);
			logger.debug("setPartialPlans create new Parts with Scheduling " + schedulingFactory.getName() + " assigned to thread " + nextExecutorToAssign);
			final IScheduling scheduling = schedulingFactory.create(partialPlan, partialPlan.getCurrentPriority());
			planScheduledBy.put(partialPlan, nextExecutorToAssign);
			partialPlanSchedulingMap.put(partialPlan, scheduling);
			planScheduling[nextExecutorToAssign++].addPlan(scheduling);
			if (nextExecutorToAssign == schedulingExecutor.length) {
				nextExecutorToAssign = 0;
			}
		}
	}

	@Override
	public void removePartialPlan(IPhysicalQuery affectedQuery) {
		if (partialPlans.contains(affectedQuery)) {
			partialPlans.remove(affectedQuery);
			removeFromScheduler(affectedQuery);
		}
	}

	private void removeFromScheduler(IPhysicalQuery affectedQuery) {
		Integer scheduler = planScheduledBy.remove(affectedQuery);
		if (scheduler != null) {
			planScheduling[scheduler].removePlan(partialPlanSchedulingMap.remove(affectedQuery));
		} else {
			logger.warn("Potential scheduling removal problem? Trying to remove " + affectedQuery);
		}
	}
	
	@Override
	public void clear() {
		 Iterator<IPhysicalQuery> iter = partialPlans.iterator();
		 while(iter.hasNext()){
				removeFromScheduler(iter.next());
				iter.remove();
		 }
		 partialPlans.clear();
	}
	
	protected boolean isScheduled(IIterableSource<?> s) {
		for (ISourceExecutor source : sourceThreads) {
			if (source.hasSource(s)) {
				return true;
			}
		}
		return false;
	}

//	private List<IIterableSource<?>> removeScheduled(List<IIterableSource<?>> sources) {
//		List<IIterableSource<?>> notscheduled = new ArrayList<>();
//		for (IIterableSource<?> source : sources) {
//			if (!isScheduled(source)) {
//				notscheduled.add(source);
//			}
//		}
//		return notscheduled;
//	}

	void removeSourceThread(ISourceExecutor sourceExecutor) {
		synchronized (sourceThreads) {
			sourceThreads.remove(sourceExecutor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang .Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (!this.schedulingExecutor.equals(t)) {
			for (int i = 0; i < schedulingExecutor.length; i++) {
				this.schedulingExecutor[i].interrupt();
			}
		}
		if (!this.sourceThreads.contains(t)) {
			super.stopScheduling();
		}

		logger.error(e.getMessage());

		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR, new Exception(e)));
	}

	public void exception(SchedulingException schedulingException) {
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR, new Exception(schedulingException)));

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		for (IPhysicalQueryScheduling sched : this.planScheduling) {
			if (sched instanceof IPlanModificationListener) {
				((IPlanModificationListener) sched).planModificationEvent(eventArgs);
			}
		}
	}

	public IPhysicalQueryScheduling[] getPlanScheduling() {
		return planScheduling;
	}

	public void sourcesChanged(MultipleSourceExecutor multipleSourceExecutor) {
		
	}

}
