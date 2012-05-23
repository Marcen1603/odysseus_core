package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;

class SchedulingExecutor extends Thread {

	static Logger logger = LoggerFactory.getLogger(SchedulingExecutor.class);

	final private IPartialPlanScheduling planScheduling;
	private long timeSlicePerStrategy;
	private SimpleThreadScheduler caller;
	private int trainsize;

	private boolean pause;

	private boolean interrupt;

	public SchedulingExecutor(IPartialPlanScheduling planScheduling,
			long timeSlicePerStrategy,
			SimpleThreadScheduler caller, int trainsize) {
		this.planScheduling = planScheduling;
		if (planScheduling == null) {
			throw new IllegalArgumentException("PlanScheduling cannot be null!");
		}
		this.timeSlicePerStrategy = timeSlicePerStrategy;
		this.caller = caller;
		this.trainsize = trainsize;
	}

	protected SimpleThreadScheduler getCaller() {
		return caller;
	}

	protected synchronized void pause() {
		this.pause = true;
	}

	protected synchronized void endPause() {
		this.pause = false;
		notifyAll();
	}

	@Override
	public void run() {
		logger.debug(" Started ");
		interrupt = false;
		try {
			while (!isInterrupted()) {
				if (!pause && planScheduling.planCount() > 0) {
					IScheduling plan = planScheduling.nextPlan();
					while (plan != null && !isInterrupted() && !pause) {
						// logger.debug("Process Plan "+plan);
						boolean planDone = false;
						if (trainsize > 1) {
							planDone = plan.schedule(timeSlicePerStrategy,
									trainsize);
						} else {
							planDone = plan.schedule(timeSlicePerStrategy);
						}
						if (planDone) {
							planScheduling.removePlan(plan);
						}

						plan = planScheduling.nextPlan();
					}
				} else {
					synchronized (this) {
						// logger.debug("Waiting for plans to execute ");
						wait(1000);
					}
				}
			}
		} catch (Throwable t) {
			if (!(t instanceof InterruptedException)) {
				t.printStackTrace();
				caller.exception(new SchedulingException(t));
			}
		}
		logger.debug(" Terminated ");
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		this.interrupt = true;
	}
	
	@Override
	public boolean isInterrupted() {
		return super.isInterrupted() || interrupt;
	}
}
