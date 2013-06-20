package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

public class SimpleThreadSchedulerMultipleSourcesThreaded extends
		AbstractSimpleThreadScheduler {

	private long sourcesPerThread;

	public SimpleThreadSchedulerMultipleSourcesThreaded(
			ISchedulingFactory schedulingStrategieFactory,
			IPhysicalQueryScheduling[] planScheduling, long sourcesPerThread) {
		super(schedulingStrategieFactory, planScheduling);
		this.sourcesPerThread = sourcesPerThread;
	}

	private void removeUnscheduledSources() {
		// 1. Remove all Sources that no longer need to be scheduled
		// --> are no longer part of super.sources
		// Hint: These are not the new Sources!
		for (ISourceExecutor sourceExe : sourceThreads) {
			MultipleSourceExecutor sourceExecutor = (MultipleSourceExecutor) sourceExe;

			//logger.debug("Removing sources from thread " + sourceExecutor);
			// there should be only one running source per executor in
			// single mode
			for (IIterableSource<?> runningSource : sourceExecutor.getSources()) {
				if (!sources.contains(runningSource)) {
					sourceExecutor.removeSource(runningSource);
					logger.debug("Remove source " + runningSource + " from "
							+ sourceExecutor);
					if (sourceExecutor.getRunningSources() == 0){
						sourceExecutor.interrupt();
						removeSourceThread(sourceExecutor);
						logger.debug("Remove source executor "+sourceExecutor);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler#setSources(
	 * java.util.List)
	 */
	@Override
	protected synchronized void process_setLeafSources(
			List<IIterableSource<?>> newSources) {
		synchronized (sourceThreads) {
			removeUnscheduledSources();
			if (newSources != null) {
				for (IIterableSource<?> source : newSources) {
					if (!isScheduled(source)) {
						MultipleSourceExecutor mse = getNextSourceExecutor();
						mse.addSource(source);
						if (this.isRunning() && !mse.isAlive()) {
							mse.start();
						}
					}

				}
			}
		}
	}

	private MultipleSourceExecutor getNextSourceExecutor() {
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

	private MultipleSourceExecutor createNewExecutor() {
		MultipleSourceExecutor ret;
		ret = new MultipleSourceExecutor(this);
		sourceThreads.add(ret);
		return ret;
	}

}
