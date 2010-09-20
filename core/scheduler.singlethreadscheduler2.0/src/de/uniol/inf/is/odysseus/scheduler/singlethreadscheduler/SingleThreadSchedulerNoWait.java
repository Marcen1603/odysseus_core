package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.AbstractScheduler;
import de.uniol.inf.is.odysseus.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * SingleThreadScheduler is a scheduler which uses two threads for execution the
 * physical execution plan.
 * 
 * One Thread for global sources and one for the registered partial plans.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class SingleThreadSchedulerNoWait extends AbstractScheduler implements
		UncaughtExceptionHandler {

	Logger logger = LoggerFactory.getLogger(SingleThreadSchedulerNoWait.class);

	/**
	 * Creates a new SingleThreadScheduler.
	 * 
	 * @param schedulingStrategieFactory
	 *            Factory for creating new scheduling strategies for each
	 *            partial plan which should be scheduled.
	 */
	public SingleThreadSchedulerNoWait(
			ISchedulingFactory schedulingStrategieFactory) {
		super(schedulingStrategieFactory);
	}

	/**
	 * All schedulings.
	 */
	final private List<IScheduling> parts = new CopyOnWriteArrayList<IScheduling>();

	/**
	 * Thread for execution the registered partial plans.
	 */
	private ExecutorThread execThread;

	/**
	 * Thread for execution the global sources.
	 */
	private List<SingleSourceExecutor> sourceThreads = new Vector<SingleSourceExecutor>();

	/**
	 * Thread for execution the registered partial plans. Based on scheduling
	 * strategies.
	 * 
	 * @author Wolf Bauer, Marco Grawunder
	 * 
	 */
	private class ExecutorThread extends Thread {

		@Override
		public void run() {
			try {
				while (!isInterrupted()) {
					Iterator<IScheduling> part = parts.iterator();
					while (part.hasNext() && !isInterrupted()) {
						IScheduling nextPart = part.next();
						if (nextPart.isSchedulable()
								&& nextPart.schedule(timeSlicePerStrategy)) {
							// part is done
							parts.remove(nextPart);
							part = parts.iterator();
						}
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private class SingleSourceExecutor extends Thread {

		private IIterableSource<?> s;

		public SingleSourceExecutor(IIterableSource<?> s) {
			this.s = s;
		}

		public void run() {
			sourceThreads.add(this);
			while (!isInterrupted() && !s.isDone()) {
				while (s.hasNext()) {
					s.transferNext();
				}
				Thread.yield();
			}
			sourceThreads.remove(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.AbstractScheduler#startScheduling()
	 */
	@Override
	public synchronized void startScheduling() {
		if (isRunning()) {
			throw new SchedulingException("scheduler is already running");
		}

		super.startScheduling();
		for (SingleSourceExecutor source : sourceThreads) {
			source.start();
		}
		initExecThread();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.AbstractScheduler#stopScheduling()
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
		execThread.interrupt();
		super.stopScheduling();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.IScheduler#setPartialPlans(java.util
	 * .List)
	 */
	@Override
	public void setPartialPlans(List<IPartialPlan> partialPlans) {
		logger.debug("setPartialPlans");
		if (partialPlans != null) {
			logger.debug("setPartialPlans clear");
			this.parts.clear();

			// Create for each partial plan an own scheduling strategy.
			// These strategies are used for scheduling partial plans.
			logger.debug("setPartialPlans create new Parts");
			for (IPartialPlan partialPlan : partialPlans) {
				final IScheduling scheduling = schedulingFactory.create(
						partialPlan, partialPlan.getCurrentPriority());
				this.parts.add(scheduling);
			}
			// }
			// restart ExecutorThread, if terminated before
			if (isRunning() && !this.parts.isEmpty()
					&& (execThread == null || !execThread.isAlive())) {
				execThread.interrupt();
				initExecThread();
			}
		}
		logger.debug("setPartialPlans done");
	}

	private void initExecThread() {
		logger.debug("initExecThread");
		execThread = new ExecutorThread();
		execThread.setUncaughtExceptionHandler(this);
		execThread.setPriority(Thread.NORM_PRIORITY);
		execThread.start();
		logger.debug("initExecThread done");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.IScheduler#setSources(java.util.List)
	 */
	@Override
	public synchronized void setLeafSources(
			List<IIterableSource<?>> sourcesToSchedule) {
		if (sourcesToSchedule != null) {
			for (SingleSourceExecutor source : sourceThreads) {
				source.interrupt();
			}

			for (IIterableSource<?> source : sourcesToSchedule) {
				final IIterableSource<?> s = source;
				SingleSourceExecutor singleSourceExecutor = new SingleSourceExecutor(
						s);
				sourceThreads.add(singleSourceExecutor);
				if (this.isRunning() && !singleSourceExecutor.isAlive()) {
					singleSourceExecutor.start();
				}
			}

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
		if (!this.execThread.equals(t)) {
			this.execThread.interrupt();
		}
		if (!this.sourceThreads.contains(t)) {
			super.stopScheduling();
		}

		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR, new Exception(e)));
	}

}
