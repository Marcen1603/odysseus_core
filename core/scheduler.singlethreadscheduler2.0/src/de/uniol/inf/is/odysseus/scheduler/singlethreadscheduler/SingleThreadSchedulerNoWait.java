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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
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
	private List<SingleSourceExecutor> sourceThreads = new CopyOnWriteArrayList<SingleSourceExecutor>();

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
			this.setName("ExecutorThread");
			try {
				while (!isInterrupted())  {
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
				// TODO: Message to Caller
				t.printStackTrace();
				throw new SchedulingException(t);
			}
		}
	}

	private class SingleSourceExecutor extends Thread {

		private IIterableSource<?> s;

		public SingleSourceExecutor(IIterableSource<?> s) {
			this.s = s;
		}

		@Override
		public void run() {
			sourceThreads.add(this);
			logger.debug("Added Source "+s);
			while (!isInterrupted() && s.isOpen() && !s.isDone()) {
				try{
					while (s.hasNext()) {
						s.transferNext();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Thread.yield();
			}
			logger.debug("Removed Source "+s+" interrupted "+isInterrupted()+" open "+s.isOpen()+" done "+s.isDone());
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
	public synchronized void setPartialPlans(List<IPartialPlan> partialPlans) {
		logger.debug("setPartialPlans");
		if (partialPlans != null) {
			logger.debug("setPartialPlans clear");
			this.parts.clear();

			// Create for each partial plan an own scheduling strategy.
			// These strategies are used for scheduling partial plans.
			logger.debug("setPartialPlans create new Parts");
			try {
			for (IPartialPlan partialPlan : partialPlans) {
				final IScheduling scheduling = schedulingFactory.create(
						partialPlan, partialPlan.getCurrentPriority());
				synchronized(parts){
					this.parts.add(scheduling);
					parts.notifyAll();
				}
			}
			} catch( Throwable t ) {
				t.printStackTrace();
				throw new RuntimeException(t);
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
		
		logger.error(e.getMessage());
		
		// send an ErrorEvent to all listenern
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR, new Exception(e)));
	}

}
