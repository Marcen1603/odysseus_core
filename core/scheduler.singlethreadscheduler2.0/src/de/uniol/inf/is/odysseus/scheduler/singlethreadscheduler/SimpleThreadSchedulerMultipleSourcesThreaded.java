package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

public class SimpleThreadSchedulerMultipleSourcesThreaded extends
		AbstractSimpleThreadScheduler {

	private int sourcesPerThread;

	public SimpleThreadSchedulerMultipleSourcesThreaded(
			ISchedulingFactory schedulingStrategieFactory,
			IPhysicalQueryScheduling[] planScheduling, int sourcesPerThread) {
		super(schedulingStrategieFactory, planScheduling);
		this.sourcesPerThread = sourcesPerThread;
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
			if (newSources != null) {
				// in a multi-source-executor, we have to remove all
				for (ISourceExecutor sourceExecutor : sourceThreads) {
					sourceExecutor.interrupt();
				}
				ArrayList<IIterableSource<?>> newSourcesClone = new ArrayList<>(
						newSources);
				sourceThreads.clear();
				// we add all into one executor
				if (newSourcesClone.size() > 0) {
					logger.debug("Create new multi source executor for: "
							+ newSourcesClone);
					MultipleSourceExecutor mse = new MultipleSourceExecutor(
							newSourcesClone, this);
					sourceThreads.add(mse);
					if (this.isRunning() && !mse.isAlive()) {
						mse.start();
					}
				}

			}
		}
	}

}
