package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.AbstractScheduler;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

// MG: HINWEIS: Ich weiﬂ noch nicht warum, aber es scheint deutlich (!) weniger effizient zu sein

/**
 * SingleThreadScheduler is a scheduler which uses two threads for execution the
 * physical execution plan.
 * 
 * One Thread for global sources and one for the registered partial plans.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public class SingleThreadSchedulerWithStrategy extends AbstractScheduler implements
		UncaughtExceptionHandler {

	Logger logger = LoggerFactory.getLogger(SingleThreadSchedulerWithStrategy.class);
	RoundRobinPlanScheduling planScheduling = new RoundRobinPlanScheduling();

	/**
	 * Creates a new SingleThreadScheduler.
	 * 
	 * @param schedulingStrategieFactory
	 *            Factory for creating new scheduling strategies for each
	 *            partial plan which should be scheduled.
	 */
	public SingleThreadSchedulerWithStrategy(ISchedulingFactory schedulingStrategieFactory) {
		super(schedulingStrategieFactory);
	}

	/**
	 * Thread for execution the registered partial plans.
	 */
	private ExecutorThread execThread;

	/**
	 * Thread for execution the global sources.
	 */
	private List<SingleSourceExecutor> sourceThreads = new Vector<SingleSourceExecutor>();

	@Override
	public synchronized void startScheduling() {
		if (isRunning()) {
			throw new SchedulingException("scheduler is already running");
		}

		super.startScheduling();
		// Start Source Threads
		for (SingleSourceExecutor source : sourceThreads) {
			source.start();
		}
		// Start Executor Thread to execute plans
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
		logger.debug("Setting new Plans to schedule :"+partialPlans);
		this.planScheduling.clear();

		if (partialPlans != null) {
			// Create for each partial plan an own scheduling strategy.
			// These strategies are used for scheduling partial plans.
			for (IPartialPlan partialPlan : partialPlans) {
				logger.debug("setPartialPlans create new Parts with Scheduling "+schedulingFactory.getName());
				final IScheduling scheduling = schedulingFactory.create(
						partialPlan, partialPlan.getPriority());
				planScheduling.addPlan(scheduling);
			}
		}
		
		// restart ExecutorThread, if terminated before
		if (isRunning() && (this.planScheduling.planCount() > 0)
				&& (execThread == null || !execThread.isAlive())) {
			execThread.interrupt();
			initExecThread();
		}
		logger.debug("setPartialPlans done");
	}

	private void initExecThread() {
		logger.debug("initExecThread");
		execThread = new ExecutorThread(planScheduling, timeSlicePerStrategy);
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
	public synchronized void setSources(
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
		if (!this.execThread.equals(t)) {
			this.execThread.interrupt();
		}
		if (!this.sourceThreads.contains(t)) {
			super.stopScheduling();
		}

		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, "", new Exception(e.getMessage())));
	}

}

/**
 * Thread for execution of the registered partial plans. Based on scheduling
 * strategies.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */

class ExecutorThread extends Thread {

	private RoundRobinPlanScheduling planScheduling;
	private long timeSlicePerStrategy;

	public ExecutorThread(RoundRobinPlanScheduling planScheduling,
			long timeSlicePerStrategy) {
		this.planScheduling = planScheduling.clone();
		this.timeSlicePerStrategy = timeSlicePerStrategy;
	}

	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				IScheduling plan = planScheduling.nextPlan();
				while (plan != null && !isInterrupted()) {
					if (plan.schedule(timeSlicePerStrategy)) {
						// plan is done
						planScheduling.removeCurrent();
					}
					plan = planScheduling.nextPlan();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}

class SingleSourceExecutor extends Thread {

	private IIterableSource<?> s;

	private SingleThreadSchedulerWithStrategy caller;

	public SingleSourceExecutor(IIterableSource<?> s,
			SingleThreadSchedulerWithStrategy singleThreadScheduler) {
		this.s = s;
		this.caller = singleThreadScheduler;
	}

	public void run() {
		while (!isInterrupted() && !s.isDone() && s.isActive()) {
			while (s.hasNext()) {
				s.transferNext();
			}
			Thread.yield();
		}
		caller.removeSourceThread(this);
	}

}

class RoundRobinPlanScheduling implements ISchedulingEventListener, IClone{

	Logger logger = LoggerFactory.getLogger(RoundRobinPlanScheduling.class);

	final private List<IScheduling> planList;
	final private Set<IScheduling> pausedPlans;
	private Iterator<IScheduling> planIterator = null;

	public RoundRobinPlanScheduling() {
		planList = new ArrayList<IScheduling>();
		pausedPlans = new HashSet<IScheduling>();
	}
	
	public RoundRobinPlanScheduling(RoundRobinPlanScheduling other){
		this.planList = new ArrayList(other.planList);
		this.pausedPlans = new HashSet<IScheduling>(other.pausedPlans);
	}
	
	public void addPlan(IScheduling plan) {
		planList.add(plan);
		plan.addSchedulingEventListener(this);
		planIterator = null;
	}

	public void clear() {
		planIterator = null;
		pausedPlans.clear();
		planList.clear();
	}

	public void removeCurrent() {
		if (planIterator != null) {
			planIterator.remove();
		}
	}

	public int planCount() {
		return planList.size();
	}

	public IScheduling nextPlan() {
		if (planIterator == null || !planIterator.hasNext()) {
			planIterator = planList.iterator();
		}
		if (planIterator.hasNext()) {
			synchronized (pausedPlans) {
				while (planCount() == pausedPlans.size()) {
					try {
						logger.debug(this+" paused");
						pausedPlans.wait(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return planIterator.next();
		} else {
			return null;
		}
	}

	@Override
	public void nothingToSchedule(IScheduling sched) {
		pausedPlans.add(sched);
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		pausedPlans.remove(sched);
		synchronized (pausedPlans) {
			pausedPlans.notifyAll();
		}
	}

	@Override
	public RoundRobinPlanScheduling clone(){
		return new RoundRobinPlanScheduling(this);
	}
	
}