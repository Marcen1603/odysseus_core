package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.scheduler.exception.SchedulingException;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.ITrainScheduling;

/**
 * Executor Thread that makes use of tuple batching if the buffer scheduling 
 * strategy is an instance of ITrainScheduling
 * 
 * @author Thomas Vogelgesang
 *
 */
public class TrainExecutorThread extends ExecutorThread {
	
	private int trainSize;

	public TrainExecutorThread(IPartialPlanScheduling planScheduling,
			long timeSlicePerStrategy, SingleThreadSchedulerWithStrategy caller, int trainSize) {
		super(planScheduling, timeSlicePerStrategy, caller);
		this.trainSize = trainSize;
	}
	
	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				boolean writingDone = false;
				boolean planDone = false;
				IScheduling plan = getPlanScheduling().nextPlan();
				while (plan != null && !isInterrupted()) {
					if (getCaller().isOutputDebug()
							&& ((getCaller().getLimitDebug() > 0 && getCaller().getLinesWritten() < getCaller().getLimitDebug()) || getCaller().getLimitDebug() < 0)) {
						int lineswritten = getCaller().print(plan);
						getCaller().incLinesWritten(lineswritten);
						if (!writingDone && getCaller().getLinesWritten() % 100000 == 0){
							getCaller().logger.debug("Written "+getCaller().getLinesWritten());
						}
						if (!writingDone && getCaller().getLinesWritten()>= getCaller().getLimitDebug()) {
							getCaller().logger.debug("Max No of lines written");
							getCaller().savePrint();
							getCaller().logger.debug("Dumped to Disk");
							writingDone = true;
							getCaller().stopScheduling();
						}
					}

					
					if (plan instanceof ITrainScheduling) {
						planDone = ((ITrainScheduling)plan).schedule(getTimeSlicePerStrategy(), trainSize);
					} else {
						planDone = plan.schedule(getTimeSlicePerStrategy());
					}
					if (planDone) {
						// plan is done
						getPlanScheduling().removePlan(plan);
					}
					plan = getPlanScheduling().nextPlan();
				}
			}
		} catch (Throwable t) {
			// TODO: Message to Caller
			t.printStackTrace();
			throw new SchedulingException(t);
		}
	}

}
