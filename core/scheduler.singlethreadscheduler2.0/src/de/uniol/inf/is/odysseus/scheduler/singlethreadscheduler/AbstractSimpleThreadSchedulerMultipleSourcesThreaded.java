package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

abstract public class AbstractSimpleThreadSchedulerMultipleSourcesThreaded
		extends AbstractSimpleThreadScheduler {

	static final Logger LOG = LoggerFactory
			.getLogger(AbstractSimpleThreadSchedulerMultipleSourcesThreaded.class);

	public AbstractSimpleThreadSchedulerMultipleSourcesThreaded(
			ISchedulingFactory schedulingStrategieFactory,
			IPhysicalQueryScheduling[] planScheduling) {
		super(schedulingStrategieFactory, planScheduling);
	}

	private void removeUnscheduledSources() {
		// 1. Remove all Sources that no longer need to be scheduled
		// --> are no longer part of super.sources
		// Hint: These are not the new Sources!
		synchronized (sourceThreads) {

			for (ISourceExecutor sourceExe : sourceThreads) {
				MultipleSourceExecutor sourceExecutor = (MultipleSourceExecutor) sourceExe;

				// logger.debug("Removing sources from thread " +
				// sourceExecutor);
				// there should be only one running source per executor in
				// single mode
				// need a copy, since the list may change during iteration in
				// loop, which causes concurrent modification exceptions
				List<IIterableSource<?>> copyList = new ArrayList<IIterableSource<?>>(
						sourceExecutor.getSources());
				List<IIterableSource<?>> toRemove = new LinkedList<>();
				for (IIterableSource<?> runningSource : copyList) {
					if (!sources.contains(runningSource)) {
						toRemove.add(runningSource);
					}
				}
				if (toRemove.size() > 0) {
					logger.debug("Removing sources " + toRemove + " from "
							+ sourceExecutor);
					sourceExecutor.removeSources(toRemove);
					logger.debug("Removed sources " + toRemove + " from "
							+ sourceExecutor);
					// TODO: Move to another place--> Remove is async
					if (sourceExecutor.getRunningSources() == 0) {
						sourceExecutor.interrupt();
						removeSourceThread(sourceExecutor);
						logger.debug("Remove source executor " + sourceExecutor);
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

	abstract protected MultipleSourceExecutor getNextSourceExecutor();

	protected MultipleSourceExecutor createNewExecutor() {
		MultipleSourceExecutor ret;
		ret = new MultipleSourceExecutor(this);
		sourceThreads.add(ret);
		return ret;
	}

}
