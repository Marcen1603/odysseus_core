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
package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusDefaults;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.core.server.scheduler.AbstractScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

/**
 * SingleThreadScheduler is a scheduler which uses two threads for execution the
 * physical execution plan.
 * 
 * One Thread for global sources and one for the registered partial plans.
 * 
 * The Selection which plans to schedule is based on a selection strategy
 * 
 * @author Wolf Bauer, Marco Grawunder, Thomas Vogelgesang
 * 
 */
public class SingleThreadSchedulerWithStrategy extends AbstractScheduler
		implements UncaughtExceptionHandler, IPlanModificationListener {

	private volatile int trainSize = (int) OdysseusDefaults.getLong(
			"scheduler_trainSize", 1);

	Logger logger = LoggerFactory
			.getLogger(SingleThreadSchedulerWithStrategy.class);
	final IPartialPlanScheduling planScheduling;

	/**
	 * Creates a new SingleThreadScheduler.
	 * 
	 * @param schedulingStrategieFactory
	 *            Factory for creating new scheduling strategies for each
	 *            partial plan which should be scheduled.
	 * @throws IOException
	 */
	public SingleThreadSchedulerWithStrategy(
			ISchedulingFactory schedulingStrategieFactory,
			IPartialPlanScheduling planScheduling) {
		super(schedulingStrategieFactory);
		this.planScheduling = planScheduling;
		schedulingExecutor = new SchedulingExecutor(planScheduling, timeSlicePerStrategy, this,
				trainSize);
		schedulingExecutor.setUncaughtExceptionHandler(this);
		schedulingExecutor.setPriority(Thread.NORM_PRIORITY);
	}

	/**
	 * Thread for execution the registered partial plans.
	 */
	protected SchedulingExecutor schedulingExecutor;

	/**
	 * Thread for execution the global sources.
	 */
	private List<SingleSourceExecutor> sourceThreads = new CopyOnWriteArrayList<SingleSourceExecutor>();

	@Override
	public synchronized void startScheduling() {
		if (isRunning()) {
			throw new SchedulingException("scheduler is already running");
		}

		super.startScheduling();
		// Start Source Threads
		// TODO: Wenn die Threads vorher beendet wurden, kann man die nicht einfach neu starten
		for (SingleSourceExecutor source : sourceThreads) {
			source.start();
		}
		// Start Executor Thread to execute plans
		schedulingExecutor.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.AbstractScheduler#stopScheduling()
	 */
	@Override
	public synchronized void stopScheduling() {
		if (!isRunning()) {
			throw new SchedulingException("scheduler isn't running");
		}
		// stop the scheduler threads
		for (Thread sourceThread : sourceThreads) {
			sourceThread.interrupt();
		}
		schedulingExecutor.interrupt();
		super.stopScheduling();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#setPartialPlans(java.util
	 * .List)
	 */
	@Override
	protected synchronized void process_setPartialPlans(
			List<IPartialPlan> partialPlans) {
		logger.debug("Setting new Plans to schedule :" + partialPlans);
		
		schedulingExecutor.pause();
		
		this.planScheduling.clear();

		if (partialPlans != null) {
			// Create for each partial plan an own scheduling strategy.
			// These strategies are used for scheduling partial plans.
			for (IPartialPlan partialPlan : partialPlans) {
				logger.debug("setPartialPlans create new Parts with Scheduling "
						+ schedulingFactory.getName());
				final IScheduling scheduling = schedulingFactory.create(
						partialPlan, partialPlan.getCurrentPriority());
				planScheduling.addPlan(scheduling);
			}
		}

		schedulingExecutor.endPause();

		logger.debug("setPartialPlans done");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#setSources(java.util.List)
	 */
	@Override
	protected synchronized void process_setLeafSources(
			List<IIterableSource<?>> sourcesToSchedule) {
		if (sourcesToSchedule != null) {
			for (SingleSourceExecutor source : sourceThreads) {
				source.interrupt();
			}

			for (IIterableSource<?> source : sourcesToSchedule) {
				final IIterableSource<?> s = source;
				SingleSourceExecutor singleSourceExecutor = new SingleSourceExecutor(
						s, this);
				sourceThreads.add(singleSourceExecutor);
				if (this.isRunning() && !singleSourceExecutor.isAlive()) {
					singleSourceExecutor.start();
				}
			}

		}
	}

	void removeSourceThread(SingleSourceExecutor singleSourceExecutor) {
		synchronized (sourceThreads) {
			sourceThreads.remove(singleSourceExecutor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang
	 * .Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (!this.schedulingExecutor.equals(t)) {
			this.schedulingExecutor.interrupt();
		}
		if (!this.sourceThreads.contains(t)) {
			super.stopScheduling();
		}

		logger.error(e.getMessage());

		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR,
				new Exception(e)));
	}

	public void exception(SchedulingException schedulingException) {
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR,
				new Exception(schedulingException)));

	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (this.planScheduling instanceof IPlanModificationListener) {
			((IPlanModificationListener) this.planScheduling).planModificationEvent(eventArgs);
		}
	}

}
