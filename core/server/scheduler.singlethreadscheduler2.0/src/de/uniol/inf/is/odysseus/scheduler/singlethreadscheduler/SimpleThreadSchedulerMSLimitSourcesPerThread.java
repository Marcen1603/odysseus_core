package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

public class SimpleThreadSchedulerMSLimitSourcesPerThread extends
		AbstractSimpleThreadSchedulerMultipleSourcesThreaded {

	private long sourcesPerThread;
	
	public SimpleThreadSchedulerMSLimitSourcesPerThread(
			ISchedulingFactory schedulingStrategieFactory,
			IPhysicalQueryScheduling[] planScheduling, long sourcesPerThread,
			OdysseusConfiguration config) {
		super(schedulingStrategieFactory, planScheduling, config);
		this.sourcesPerThread = sourcesPerThread;
	}
	
	
	@Override
	protected MultipleSourceExecutor getNextSourceExecutor() {
		MultipleSourceExecutor ret = null;
		// Only source thread
		if (sourcesPerThread == -1) {
			if (sourceThreads.size() == 0) {
				ret = createNewExecutor();
			}else{
				ret = (MultipleSourceExecutor) sourceThreads.get(0);
			}
		} else {
			// Find first available executor
			for (ISourceExecutor s : sourceThreads) {
				MultipleSourceExecutor sourceExecutor = (MultipleSourceExecutor) s;
				if (sourceExecutor.getRunningSources() < sourcesPerThread) {
					ret = sourceExecutor;
					break;
				}
			}
			// No free Executor found. Create a new one
			if (ret == null){
				ret = createNewExecutor();
			}

		}
		return ret;
	}
	
}
