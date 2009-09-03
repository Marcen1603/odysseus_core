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

public class SingleThreadScheduler extends AbstractScheduler implements
		UncaughtExceptionHandler {

	public SingleThreadScheduler(
			ISchedulingStrategyFactory schedulingStrategieFactory) {
		super(schedulingStrategieFactory);
	}

	final private ArrayList<IIterableSource<?>> sourcesToSchedule = new ArrayList<IIterableSource<?>>();

	final private Map<IPartialPlan, ISchedulingStrategy> parts = new HashMap<IPartialPlan, ISchedulingStrategy>();

	private final SourceExecutor sourceExecutor = new SourceExecutor();

	private ExecutorThread thread;

	private Thread sourceThread;

	private class ExecutorThread extends Thread {

		@Override
		public void run() {
			Collection<ISchedulingStrategy> values = parts.values();
			Iterator<ISchedulingStrategy> part;
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
		}
	}

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

	@Override
	public synchronized void stopScheduling() {
		if (!isRunning()) {
			throw new SchedulingException("scheduler isn't running");
		}
		sourceThread.interrupt();
		super.stopScheduling();
	}

	@Override
	public void setPartialPlans(List<IPartialPlan> partialPlans) {
		if (partialPlans != null) {
			synchronized (this.parts) {
				this.parts.clear();

				for (IPartialPlan partialPlan : partialPlans) {
					final ISchedulingStrategy strategy = schedulingStrategieFactory
							.createStrategy(partialPlan, partialPlan
									.getPriority());
					this.parts.put(partialPlan, strategy);
				}
			}
		}
	}

	@Override
	public void setSources(List<IIterableSource<?>> sources) {
		if (sources != null) {
			synchronized (this.sourcesToSchedule) {
				this.sourcesToSchedule.clear();
				this.sourcesToSchedule.addAll(sources);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<IPartialPlan> getPartialPlans() {
		return (ArrayList<IPartialPlan>) this.parts.keySet();
	}

	@Override
	public ArrayList<IIterableSource<?>> getSources() {
		return this.sourcesToSchedule;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (!this.thread.equals(t)) {
			this.thread.interrupt();
		}
		if (!this.sourceThread.equals(t)) {
			super.stopScheduling();
		}
		
		fireErrorEvent(new ErrorEvent(this, "", new Exception(e.getMessage())));
	}
}
