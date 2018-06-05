package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

public class SimpleThreadSchedulerMSLimitThreads extends
		AbstractSimpleThreadSchedulerMultipleSourcesThreaded {

	final private long maxNumberOfThreads;
	private int lastAssignedSourceExecutor = -1;

	public SimpleThreadSchedulerMSLimitThreads(
			ISchedulingFactory schedulingStrategy,
			IPhysicalQueryScheduling[] scheduling, long numberOfThreads,
			OdysseusConfiguration config) {
		super(schedulingStrategy, scheduling, config);
		this.maxNumberOfThreads = numberOfThreads;
	}

	@Override
	protected MultipleSourceExecutor getNextSourceExecutor() {
		lastAssignedSourceExecutor++;
		if (lastAssignedSourceExecutor == maxNumberOfThreads) {
			lastAssignedSourceExecutor = 0;
		}
		// Create Executor only if there is a source to schedule
		if (sourceThreads.size() < maxNumberOfThreads) {
			createNewExecutor();
		}
		if (sourceThreads.size() > lastAssignedSourceExecutor) {
			return (MultipleSourceExecutor) sourceThreads
					.get(lastAssignedSourceExecutor);
		} else {
			lastAssignedSourceExecutor = 0;
			return (MultipleSourceExecutor) sourceThreads
					.get(0);
		}
	}

}
