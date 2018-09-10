package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

public class MultipleSourceExecutor extends Thread implements ISourceExecutor {

	static final Logger logger = LoggerFactory.getLogger(MultipleSourceExecutor.class);
	static final InfoService infoService = InfoServiceFactory.getInfoService(MultipleSourceExecutor.class);
	private boolean interrupt = false;
	final private List<IIterableSource<?>> sources = new ArrayList<>();
	final private List<IIterableSource<?>> toAdd = new LinkedList<>();
	final private List<IIterableSource<?>> toRemove = new LinkedList<>();

	enum State {
		WAIT_FOR_OPEN, PROCESSING, UPDATE_SOURCES
	};

	private State currentState = State.WAIT_FOR_OPEN;

	final private Comparator<IIterableSource<?>> sourcesComparator = new Comparator<IIterableSource<?>>() {
		@Override
		public int compare(IIterableSource<?> left, IIterableSource<?> right) {
			long l = left.isOpen() ? left.getDelay() : 0;
			long r = right.isOpen() ? right.getDelay() : 0;
			return Long.compare(l, r);
		}
	};
	final private AbstractSimpleThreadScheduler caller;
	final private Map<IIterableSource<?>, Long> lastRuns = new HashMap<>();

	private static long counter = 0;

	public MultipleSourceExecutor(AbstractSimpleThreadScheduler singleThreadScheduler) {
		this.setName("MultiSourceExecutor #" + (counter++) + " ");
		this.caller = singleThreadScheduler;
	}

	@Override
	public void run() {
		try {
			interrupt = false;
			while (!interrupt && caller.isRunning()) {
				// waitForOpen();
				updateSources();
				while (sources.size() > 0) {
					updateSources();
					// Need to delay some time to catch an open
					// delay(10);
					boolean processableSources = processSources();
					updateSources();
					if (!processableSources) {
						logger.trace("No processable sources");
						waitForProcessableSources();
					}
				} // while sources.size() > 0
			} // !interrupt && !isInterrupted() && caller.isRunning()
		} catch (Exception e) {
			// TODO: Error Handler
			e.printStackTrace();
			infoService.error("Error in processing sources " + sources, e);
		}
		logger.debug("Terminating Source Executor " + this.getName() + " with sources: " + sources);
		// caller.removeSourceThread(this);

	}

	@SuppressWarnings("unused")
	private void delay(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void waitForProcessableSources() {
		synchronized (sources) {
			boolean processableSources = false;
			while (!processableSources && sources.size() > 0 && !interrupt && caller.isRunning()) {
				updateSources();
				for (IIterableSource<?> others : sources) {
					if (others.isOpen()) {
						logger.trace("Found open source " + others);
						processableSources = true;
						break;
					}
				}
				try {
					if (!processableSources) {
						if (logger.isTraceEnabled()) {
							logger.trace(this.getName() + " - Waiting for open sources to process " + sources);
						}
						sources.wait(1000);
					}
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private boolean processSources() {
		currentState = State.PROCESSING;
		boolean waitedForFirstSource = false;
		if (logger.isTraceEnabled()) {
			logger.trace("Process Sources " + sources);
		}
		boolean processableSources = false;
		synchronized (sources) {
			for (IIterableSource<?> s : sources) {
				if (s.isOpen() && !s.isDone()) {
					processableSources = true;
					if (s.hasNext()) {
						Long lastRun = -1l;
						lastRun = lastRuns.get(s);
						// Only delay source with shortest waiting time
						// all other sources should be testet each time
						// ... its some kind of busy wait ... :-/
						if (!waitedForFirstSource) {
							waitedForFirstSource = true;
							// The first round nobody has to wait
							if (lastRun != null) {
								try {

									while (s.getDelay() - (System.currentTimeMillis() - lastRun) > 0) {
										long time = s.getDelay() - (System.currentTimeMillis() - lastRun);
										if (logger.isTraceEnabled()) {
											logger.trace(this.hashCode() + " Sleeping ..." + time + " " + s + " "
													+ toRemove.size() + " " + toRemove + " " + sources.size() + " "
													+ sources);
										}
										sources.wait(time);
										if (logger.isTraceEnabled()) {
											logger.trace(this.hashCode() + " Sleeping done");
										}
									}
								} catch (InterruptedException e) {
									if (logger.isTraceEnabled()) {
										logger.trace("Scheduler interrupted", e);
									}
								}
							}
							transfer(s);
						} else { // Handle all but the first source
							long ct2 = System.currentTimeMillis();
							if (lastRun == null || ct2 - lastRun >= s.getDelay()) {
								transfer(s);
							}
						}
					} // if (s.hasNext())
				} // if (s.isOpen() && !s.isDone())
			} // for
		}
		return processableSources;
	}

	private void updateSources() {
		// logger.debug("Updating sources toAdd "+toAdd+" toRemove "+toRemove);
		boolean changed = false;
		currentState = State.UPDATE_SOURCES;
		List<IIterableSource<?>> list = null;
		synchronized (toAdd) {
			if (toAdd.size() > 0) {
				list = new ArrayList<>(toAdd);
				toAdd.clear();
				changed = true;
			}
		}
		if (list != null) {
			delayedAddSources(list);
			list = null;
		}
		synchronized (toRemove) {
			if (toRemove.size() > 0) {
				list = new ArrayList<>(toRemove);
				toRemove.clear();
				changed = true;
			}
		}
		if (list != null) {
			delayedRemoveSources(list);
		}
		synchronized (sources) {
			if (changed) {
				Collections.sort(sources, sourcesComparator);
			}
		}
	}

	private void transfer(IIterableSource<?> s) {
		long ct2 = System.currentTimeMillis();
		if (logger.isTraceEnabled()) {
			Long lastRun = lastRuns.get(s);
			if (lastRun == null) {
				lastRun = -1L;
			}
			logger.trace("Transfer for " + s + " d=" + s.getDelay() + " real=" + (ct2 - lastRun) + " n=" + ct2
					+ " last=" + lastRun);
		}
		s.transferNext();
		lastRuns.put(s, ct2);
	}

	@Override
	public void interrupt() {
		this.interrupt = true;
	}

	@Override
	public boolean hasSource(IIterableSource<?> other) {
		return this.sources.contains(other);
	}

	@Override
	public Collection<IIterableSource<?>> getSources() {
		return Collections.unmodifiableCollection(sources);
	}

	public void removeSource(IIterableSource<?> source) {
		synchronized (toRemove) {
			toRemove.add(source);
		}
		// this.sourcesChangeRequested = true;
		synchronized (sources) {
			sources.notifyAll();
		}
	}

	public void removeSources(List<IIterableSource<?>> remove) {
		// logger.debug("Trying to remove " + remove + " to remove list");
		synchronized (toRemove) {
			toRemove.addAll(remove);
		}
		// this.sourcesChangeRequested = true;
//		if (logger.isDebugEnabled()) {
//			if (remove.size() > 0) {
//				logger.debug("Added " + remove + " to remove list");
//			}
//		}
//		synchronized (sources) {
//			sources.notifyAll();
//		}
	}

	public synchronized void addSource(IIterableSource<?> source) {
		// this.sourcesChangeRequested = true;
		synchronized (toAdd) {
			toAdd.add(source);
		}
		synchronized (sources) {
			sources.notifyAll();
		}
	}

	public void addSources(List<IIterableSource<?>> add) {
		synchronized (toAdd) {
			toAdd.addAll(add);
		}
		// this.sourcesChangeRequested = true;
		synchronized (sources) {
			sources.notifyAll();
		}
	}

	private void delayedAddSources(List<IIterableSource<?>> addList) {
		// logger.debug("Trying to add " + toAdd.size() + " sources");
		synchronized (sources) {
			sources.addAll(addList);
			addList.clear();
		}
		logger.debug("Added Sources " + addList);
		logger.trace("" + this.currentState);
		// this.sourcesChangeRequested = false;
		caller.sourcesChanged(this);
	}

	private void delayedRemoveSources(List<IIterableSource<?>> removeList) {
		// logger.debug("Trying to remove " + toRemove.size() + " sources");
		synchronized (sources) {
			sources.removeAll(removeList);
			removeList.clear();
		}
		logger.debug("Removed Sources " + removeList);
		caller.sourcesChanged(this);
		// this.sourcesChangeRequested = false;
	}

	public int getRunningSources() {
		return sources.size();
	}

	@Override
	public String toString() {
		return this.getName();
		// synchronized (sources) {
		// return this.getClass().getSimpleName() + " " + sources;
		// }
	}

}
