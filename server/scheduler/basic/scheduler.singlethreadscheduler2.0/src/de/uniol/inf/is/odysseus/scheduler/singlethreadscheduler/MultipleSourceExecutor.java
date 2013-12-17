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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

public class MultipleSourceExecutor extends Thread implements ISourceExecutor {

	Logger logger = LoggerFactory.getLogger(MultipleSourceExecutor.class);
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
	// private boolean sourcesChangeRequested = false;

	private static long counter = 0;

	public MultipleSourceExecutor(
			AbstractSimpleThreadScheduler singleThreadScheduler) {
		this.setName("MultiSourceExecutor #" + (counter++) + " ");
		this.caller = singleThreadScheduler;

	}

	@Override
	public void run() {
		try {
			interrupt = false;
			while (!interrupt && !isInterrupted() && caller.isRunning()) {
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
				}// while sources.size() > 0
			} // !interrupt && !isInterrupted() && caller.isRunning()
		} catch (Exception e) {
			// TODO: Error Handler
			e.printStackTrace();
		}

		logger.debug("Removing Source Executor " + this.getName() + " with sources: " + sources);
		// caller.removeSourceThread(this);

	}

	// private void delay(int t) {
	// try {
	// synchronized (sources) {
	// sources.wait(t);
	// }
	// } catch (InterruptedException e) {
	// } catch (IllegalMonitorStateException e) {
	// e.printStackTrace();
	// }
	// }

	private void waitForProcessableSources() {
		synchronized (sources) {
			boolean processableSources = false;
			while (!processableSources && sources.size() > 0) {
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
							logger.trace(this.getName()+" - Waiting for open sources to process "
									+ sources);
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

									while (s.getDelay()
											- (System.currentTimeMillis() - lastRun) > 0) {
										long time = s.getDelay()
												- (System.currentTimeMillis() - lastRun);
										if (logger.isTraceEnabled()) {
											logger.trace(this.hashCode()
													+ " Sleeping ..." + time
													+ " " + s + " "
													+ toRemove.size() + " "
													+ toRemove + " "
													+ sources.size() + " "
													+ sources);
										}
										sources.wait(time);
										if (logger.isTraceEnabled()) {
											logger.trace(this.hashCode()
													+ " Sleeping done");
										}
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							transfer(s);
						} else { // Handle all but the first source
							long ct2 = System.currentTimeMillis();
							if (lastRun == null
									|| ct2 - lastRun >= s.getDelay()) {
								transfer(s);
							}
						}
					} // if (s.hasNext())
				} // if (s.isOpen() && !s.isDone())
			} // for
		}
		return processableSources;
	}

	// private void waitForOpen() {
	// boolean open = false;
	// currentState = State.WAIT_FOR_OPEN;
	// // Wait for at least one open source
	// synchronized (sources) {
	//
	// while (!open) {
	// updateSources();
	//
	// for (IIterableSource<?> s : sources) {
	// if (s.isOpen()) {
	// logger.trace("Opened " + this.hashCode()
	// + "... Start Processing of Source " + s);
	// open = true;
	// break;
	// }
	// }
	//
	// try {
	// if (!open) {
	// sources.wait(1000);
	// }
	// } catch (InterruptedException ignored) {
	// }
	//
	// }
	// }
	// logger.trace("At least one source is open");
	// }

	private void updateSources() {
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
			logger.trace("Transfer for " + s + " d=" + s.getDelay() + " real="
					+ (ct2 - lastRun) + " n=" + ct2 + " last=" + lastRun);
		}
		s.transferNext();
		lastRuns.put(s, ct2);
	}

	@Override
	public void interrupt() {
		super.interrupt();
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
		// logger.debug("Added " + remove + " to remove list");
		synchronized (sources) {
			sources.notifyAll();
		}
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
		}
		logger.debug("Added Sources " + addList);
		logger.trace("" + this.currentState);
		addList.clear();
		// this.sourcesChangeRequested = false;
	}

	private void delayedRemoveSources(List<IIterableSource<?>> removeList) {
		// logger.debug("Trying to remove " + toRemove.size() + " sources");
		synchronized (sources) {
			sources.removeAll(removeList);
		}
		logger.debug("Removed Sources " + removeList);
		removeList.clear();
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
