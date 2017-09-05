package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

public class SimpleThreadSchedulerEachSourceThreaded extends
		AbstractSimpleThreadScheduler {

	public SimpleThreadSchedulerEachSourceThreaded(
			ISchedulingFactory schedulingStrategieFactory,
			IPhysicalQueryScheduling[] planScheduling,
			OdysseusConfiguration config) {
		super(schedulingStrategieFactory, planScheduling, config);
	}
	
	private void removeUnscheduledSources() {
		// 1. Remove all Sources that no longer need to be scheduled
		// --> are no longer part of super.sources
		// Hint: These are not the new Sources!
		for (ISourceExecutor sourceExecutor : sourceThreads) {
			logger.debug("Interrupting running source thread "
					+ sourceExecutor);
			// there should be only one running source per executor in
			// single mode
			List<IIterableSource<?>> copyList = new ArrayList<IIterableSource<?>>(
					sourceExecutor.getSources());
			for (IIterableSource<?> runningSource :copyList) {
				if (!sources.contains(runningSource)) {
					sourceExecutor.interrupt();
					logger.debug("Remove source thread "
							+ sourceExecutor);
					sourceThreads.remove(sourceExecutor);
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
				// Add new Sources
				for (IIterableSource<?> source : newSources) {
					final IIterableSource<?> s = source;
					if (!isScheduled(s)) {
						SingleSourceExecutor singleSourceExecutor = new SingleSourceExecutor(
								s, this);
						sourceThreads.add(singleSourceExecutor);
						if (this.isRunning() && !singleSourceExecutor.isAlive()) {
							singleSourceExecutor.start();
						}
					}
				}
			}
		}
	}

}
