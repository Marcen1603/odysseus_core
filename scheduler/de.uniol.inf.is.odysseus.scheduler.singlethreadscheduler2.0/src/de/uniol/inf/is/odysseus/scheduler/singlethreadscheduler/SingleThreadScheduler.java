package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
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
	final private List<IScheduling> parts = new LinkedList<IScheduling>();
	final private List<IScheduling> pausedParts = new LinkedList<IScheduling>();

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
					synchronized (parts) {
						if (parts.isEmpty()) {
							break;
						}
						Iterator<IScheduling> part = parts.iterator();
						while (part.hasNext()) {
							while (parts.size() == pausedParts.size()) {
								try {
									parts.wait(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (part.next().schedule(timeSlicePerStrategy)) {
								// part is done
								part.remove();
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

			//			System.out.println("TEST: Die Strategy " + sched
//					+ " hat keine Daten");
				pausedParts.add(sched);
			}
		}

		@Override
		public void scheddulingPossible(IScheduling sched) {
			synchronized (pausedParts) {
//			System.out.println("TEST: Die Strategy " + sched
//					+ " hat wieder Daten");
				pausedParts.remove(sched);
				
			}
			synchronized(parts){
				parts.notifyAll();
			}
			
		}
	}

	private class SingleSourceExecutor extends Thread {

		private IIterableSource<?> s;

		boolean terminate = false;

		public SingleSourceExecutor(IIterableSource<?> s) {
			this.s = s;
		}

		public void run() {
			sourceThreads.add(this);
			terminate = false;
			while (!isInterrupted() && !s.isDone() && s.isActive()
					&& !terminate) {
				while (s.hasNext()) {
					s.transferNext();
				}
				Thread.yield();
			}
			sourceThreads.remove(this);
		}

		public void terminate() {
			terminate = true;
		};
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
		// stop the scheduler thread
		for (Thread sourceThread : sourceThreads) {
			sourceThread.interrupt();
		}
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
		if (partialPlans != null) {
			synchronized (this.parts) {
				this.parts.clear();

				// Create for each partial plan an own scheduling strategy.
				// These strategies are used for scheduling partial plans.
				for (IPartialPlan partialPlan : partialPlans) {
					final IScheduling scheduling = schedulingFactory.create(
							partialPlan, partialPlan.getPriority());
					this.parts.add(scheduling);
				}
			}
			// restart ExecutorThread, if terminated before
			if (isRunning() && !this.parts.isEmpty()
					&& (execThread == null || !execThread.isAlive())) {
				initExecThread();
			}
		}
	}

	private void initExecThread() {
		execThread = new ExecutorThread();
		execThread.setUncaughtExceptionHandler(this);
		execThread.setPriority(Thread.NORM_PRIORITY);
		for (IScheduling scheduling : parts) {
			scheduling.addSchedulingEventListener(execThread);
		}
		execThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.IScheduler#setSources(java.util.List)
	 */
	@Override
	public synchronized void setSources(
			List<IIterableSource<?>> sourcesToSchedule) {
		if (sourcesToSchedule != null) {
			for (SingleSourceExecutor source : sourceThreads) {
				source.terminate();
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

	// /*
	// * (non-Javadoc)
	// *
	// * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#getPartialPlans()
	// */
	// @Override
	// public List<IPartialPlan> getPartialPlans() {
	// // TODO: Recalc only if change occured
	// List<IPartialPlan> ret = new ArrayList<IPartialPlan>(parts.size());
	// for (ISchedulingStrategy s: parts){
	// ret.add(s.getPlan());
	// }
	// return ret;
	// }

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
		fireErrorEvent(new ErrorEvent(this, "", new Exception(e.getMessage())));
	}

}
