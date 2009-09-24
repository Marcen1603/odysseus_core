package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.AbstractScheduler;
import de.uniol.inf.is.odysseus.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingStrategyFactory;

/**
 * SingleThreadScheduler is a scheduler which uses two threads for execution the
 * physical execution plan.
 * 
 * One Thread for global sources and one for the registered partial plans.
 * 
 * @author Wolf Bauer
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
	public SingleThreadScheduler(
			ISchedulingStrategyFactory schedulingStrategieFactory) {
		super(schedulingStrategieFactory);
	}

	/**
	 * All global sources which should be executed.
	 */
	final private ArrayList<IIterableSource<?>> sourcesToSchedule = new ArrayList<IIterableSource<?>>();

	/**
	 * All registered partial plans with their scheduling strategy.
	 */
	final private Map<IPartialPlan, ISchedulingStrategy> parts = new HashMap<IPartialPlan, ISchedulingStrategy>();

	/**
	 * Runnable for execution the global sources.
	 */
	private final SourceExecutor sourceExecutor = new SourceExecutor();

	/**
	 * Thread for execution the registered partial plans.
	 */
	private ExecutorThread thread;

	/**
	 * Thread for execution the global sources.
	 */
	private Thread sourceThread;

	/**
	 * Thread for execution the registered partial plans. Based on scheduling
	 * strategies.
	 * 
	 * @author Wolf Bauer
	 * 
	 */
	private class ExecutorThread extends Thread {

		@Override
		public void run() {
			Collection<ISchedulingStrategy> values = parts.values();
			Iterator<ISchedulingStrategy> part;
			try{
			while (!isInterrupted() && !values.isEmpty()) {
				part = values.iterator();
				while (part.hasNext()) {
					if (part.next().schedule(timeSlicePerStrategy)) {// part
						// is
						// done
						part.remove();
					}
				}
			}
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}

	/**
	 * Runnable for execution the global sources.
	 * 
	 * @author Wolf Bauer
	 * 
	 */
	private class SourceExecutor implements Runnable {
		@Override
		public void run() {
			boolean sourcesDone = false;
			while (isRunning() && !sourcesDone) {
				synchronized (sourcesToSchedule) {
					Iterator<IIterableSource<?>> source = sourcesToSchedule
							.iterator();
					if (sourcesToSchedule.isEmpty()) {
						sourcesDone = true;
					}
					while (source.hasNext()) {
						IIterableSource<?> next = source.next();
						if (next.isDone() || !next.isActive()) {
							source.remove();
							if (sourcesToSchedule.isEmpty()) {
								sourcesDone = true;
							}
						} else {
							if (next.hasNext()) {
								next.transferNext();
							}
						}
					}
				}
			}
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
		thread = new ExecutorThread();

		thread.setUncaughtExceptionHandler(this);
		sourceThread = new Thread(this.sourceExecutor);
		sourceThread.setPriority(Thread.MAX_PRIORITY);
		thread.setPriority(Thread.NORM_PRIORITY);

		sourceThread.start();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.start();
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
		sourceThread.interrupt();
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
					final ISchedulingStrategy strategy = schedulingStrategieFactory
							.createStrategy(partialPlan, partialPlan
									.getPriority());
					this.parts.put(partialPlan, strategy);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.scheduler.IScheduler#setSources(java.util.List)
	 */
	@Override
	public void setSources(List<IIterableSource<?>> sources) {
		if (sources != null) {
			synchronized (this.sourcesToSchedule) {
				this.sourcesToSchedule.clear();
				this.sourcesToSchedule.addAll(sources);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#getPartialPlans()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<IPartialPlan> getPartialPlans() {
		return (ArrayList<IPartialPlan>) this.parts.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.scheduler.IScheduler#getSources()
	 */
	@Override
	public ArrayList<IIterableSource<?>> getSources() {
		return this.sourcesToSchedule;
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
		if (!this.thread.equals(t)) {
			this.thread.interrupt();
		}
		if (!this.sourceThread.equals(t)) {
			super.stopScheduling();
		}

		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, "", new Exception(e.getMessage())));
	}
}
