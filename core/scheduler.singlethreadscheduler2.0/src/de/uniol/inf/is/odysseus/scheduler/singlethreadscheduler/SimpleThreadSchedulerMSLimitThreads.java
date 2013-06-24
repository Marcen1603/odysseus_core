package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;


public class SimpleThreadSchedulerMSLimitThreads extends
		AbstractSimpleThreadSchedulerMultipleSourcesThreaded {

	final private long numberOfThreads;
	private int lastAssignedSourceExecutor = -1;
	
	public SimpleThreadSchedulerMSLimitThreads(
			ISchedulingFactory schedulingStrategy,
			IPhysicalQueryScheduling[] scheduling, long numberOfThreads) {
		super(schedulingStrategy, scheduling);
		this.numberOfThreads = numberOfThreads;
	}
	
	@Override
	protected MultipleSourceExecutor getNextSourceExecutor() {
		lastAssignedSourceExecutor++;
		if (lastAssignedSourceExecutor == numberOfThreads){
			lastAssignedSourceExecutor=0;
		}
		// Create Executor only if there is a source to schedule
		if (sourceThreads.size() < numberOfThreads){
			createNewExecutor();
		}
		return (MultipleSourceExecutor) sourceThreads.get(lastAssignedSourceExecutor);
	}

}
