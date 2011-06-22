package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Single Thread Scheduler that supports tuple batching
 * @author tommy
 *
 */
public class SingleThreadTrainSchedulerWithStrategy extends SingleThreadSchedulerWithStrategy {
	
	private volatile int trainSize = (int)OdysseusDefaults.getLong("scheduler_trainSize", 1);

	public SingleThreadTrainSchedulerWithStrategy(
			ISchedulingFactory schedulingStrategieFactory,
			IPartialPlanScheduling planScheduling) {
		super(schedulingStrategieFactory, planScheduling);
	}

	@Override
	protected void initExecThread() {
		logger.debug("initExecThread");
		execThread = new TrainExecutorThread(planScheduling, timeSlicePerStrategy,
				this, this.trainSize);
		execThread.setUncaughtExceptionHandler(this);
		execThread.setPriority(Thread.NORM_PRIORITY);
		execThread.start();
		logger.debug("initExecThread done");
	}

	public void setTrainSize(int trainSize) {
		this.trainSize = trainSize;
	}

	public int getTrainSize() {
		return trainSize;
	}

}
