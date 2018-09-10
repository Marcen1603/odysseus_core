package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

abstract public class AbstractSimpleThreadSchedulerMultipleSourcesThreaded
		extends AbstractSimpleThreadScheduler {

	static final Logger LOG = LoggerFactory
			.getLogger(AbstractSimpleThreadSchedulerMultipleSourcesThreaded.class);

	public AbstractSimpleThreadSchedulerMultipleSourcesThreaded(
			ISchedulingFactory schedulingStrategieFactory,
			IPhysicalQueryScheduling[] planScheduling,
			OdysseusConfiguration config) {
		super(schedulingStrategieFactory, planScheduling, config);
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
	
				}

			}
		}
	}

	@Override
	public void sourcesChanged(MultipleSourceExecutor sourceExecutor) {
		if (sourceExecutor.getRunningSources() == 0) {
			sourceExecutor.interrupt();
			synchronized (sourceThreads) {
				sourceThreads.notifyAll();
			}
			removeSourceThread(sourceExecutor);
			logger.debug("Remove source executor " + sourceExecutor);
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
				Map<MultipleSourceExecutor, List<IIterableSource<?>>> toAdd = new HashMap<>();
				for (IIterableSource<?> source : newSources) {
					if (!isScheduled(source)) {
						MultipleSourceExecutor mse = getNextSourceExecutor();
						List<IIterableSource<?>> list = toAdd.get(mse);
						if (list == null){
							list = new LinkedList<>();
							toAdd.put(mse,list);
						}	
						list.add(source);
					}
				}
				for (Entry<MultipleSourceExecutor, List<IIterableSource<?>>> e:toAdd.entrySet()){
					e.getKey().addSources(e.getValue());
					if (this.isRunning() && !e.getKey().isAlive()) {
						e.getKey().start();
					}

					
				}
			}
		}
	}

	abstract protected MultipleSourceExecutor getNextSourceExecutor();

	protected MultipleSourceExecutor createNewExecutor() {
		MultipleSourceExecutor ret;
		ret = new MultipleSourceExecutor(this);
		logger.debug("Created new Multi-Source-Excutor "+ret);
		sourceThreads.add(ret);
		return ret;
	}

}
