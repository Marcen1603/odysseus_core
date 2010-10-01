package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.AbstractScheduler;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
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
public class SingleThreadScheduler extends AbstractScheduler implements
		UncaughtExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(SingleThreadScheduler.class);

	/**
	 * Creates a new SingleThreadScheduler.
	 * 
	 * @param schedulingStrategieFactory
	 *            Factory for creating new scheduling strategies for each
	 *            partial plan which should be scheduled.
	 */
	public SingleThreadScheduler(ISchedulingFactory schedulingStrategieFactory) {
		super(schedulingStrategieFactory);
	}

	/**
	 * All schedulings.
	 */
	final private List<IScheduling> parts = new CopyOnWriteArrayList<IScheduling>();
	final private Set<IScheduling> pausedParts = new HashSet<IScheduling>();

	/**
	 * Thread for execution the registered partial plans.
	 */
	private ExecutorThread execThread;

	/**
	 * Thread for execution the global sources.
	 */
	private List<SingleSourceExecutor> sourceThreads = new Vector<SingleSourceExecutor>();
	
	/**
	 * Marker that the plan has changed, needed because plan can change while
	 * scheduling is paused
	 */
	private boolean planChanged;

	/**
	 * Thread for execution the registered partial plans. Based on scheduling
	 * strategies.
	 * 
	 * @author Wolf Bauer, Marco Grawunder
	 * 
	 */
	private class ExecutorThread extends Thread implements
			ISchedulingEventListener {

		private boolean terminate;

		public void terminate() {
			terminate = true;
		};

		@Override
		public void run() {
			terminate = false;
			try {
				while (!isInterrupted() && !terminate) {
					synchronized (pausedParts) {
						if (parts.isEmpty()) {
							break;
						}
						Iterator<IScheduling> part = parts.iterator();
						while (part.hasNext() && !terminate && !isInterrupted()) {
							while (parts.size() == pausedParts.size() && !terminate && !isInterrupted()) {
								try {
									pausedParts.wait(10);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							// While scheduling is paused, the plan can be changed
							// so a new iterator is necessary
							if (planChanged){
								planChanged = false;
								break;
							}
							IScheduling nextPart = part.next();
							if (nextPart.schedule(timeSlicePerStrategy)) {
								// part is done
								parts.remove(nextPart);
								part = parts.iterator();
							}
						}
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		@Override
		public void nothingToSchedule(IScheduling sched) {
			synchronized (pausedParts) {
				pausedParts.add(sched);
			}
		}

		@Override
		public void scheddulingPossible(IScheduling sched) {
			synchronized (pausedParts) {
				pausedParts.remove(sched);	
				pausedParts.notifyAll();
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
			logger.debug("Added Source "+s);
			while (!isInterrupted() && s.isOpen() && !s.isDone()) {
				while (s.hasNext()) {
					s.transferNext();
				}
				Thread.yield();
			}
			logger.debug("Removed Source "+s);
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
		execThread = new ExecutorThread();

		execThread.setUncaughtExceptionHandler(this);
		// sourceThread = new Thread(this.sourceExecutor);

		for (SingleSourceExecutor source : sourceThreads) {
			source.start();
		}

		// sourceThread.setPriority(Thread.MAX_PRIORITY);
		execThread.setPriority(Thread.NORM_PRIORITY);

		// sourceThread.start();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		execThread.start();
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
		execThread.terminate();
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
//			synchronized (this.parts) {
				planChanged = true;
				logger.debug("setPartialPlans clear");
				this.parts.clear();
				this.pausedParts.clear();

				// Create for each partial plan an own scheduling strategy.
				// These strategies are used for scheduling partial plans.
				logger.debug("setPartialPlans create new Parts");
				for (IPartialPlan partialPlan : partialPlans) {
					final IScheduling scheduling = schedulingFactory.create(
							partialPlan, partialPlan.getCurrentPriority());
					this.parts.add(scheduling);
				}
//			}
			// restart ExecutorThread, if terminated before
			if (isRunning() && !this.parts.isEmpty()
					&& (execThread == null || !execThread.isAlive())) {
				execThread.terminate();
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
		for (IScheduling scheduling : parts) {
			scheduling.addSchedulingEventListener(execThread);
		}
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
			this.execThread.terminate();
		}
		if (!this.sourceThreads.contains(t)) {
			super.stopScheduling();
		}

		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR, new Exception(e)));
	}

}
