package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

public class MultipleSourceExecutor extends Thread implements IEventListener,
		ISourceExecutor {

	Logger logger = LoggerFactory.getLogger(MultipleSourceExecutor.class);
	private boolean interrupt = false;
	final private List<IIterableSource<?>> sources = new ArrayList<>();
	final private List<IIterableSource<?>> toAdd = new CopyOnWriteArrayList<>();
	final private List<IIterableSource<?>> toRemove = new CopyOnWriteArrayList<>();

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
	private boolean sourcesChangeRequested = false;

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
				waitForOpen();
				while (sources.size() > 0) {
					synchronized (sources) {
						updateSources();
						// Need to delay some time to catch an open
						delay(10);
						boolean processableSources = processSources();
						if (!processableSources) {
							waitForProcessableSources();
						}
					} // Synchronized
				}// while sources.size() > 0
			} // !interrupt && !isInterrupted() && caller.isRunning()
		} catch (Exception e) {
			// TODO: Error Handler
			e.printStackTrace();
		}

		// logger.debug("Removing " + this.hashCode() + " Sources " + sources);
		// caller.removeSourceThread(this);

	}

	private void delay(int t) {
		try {
			sources.wait(t);
		} catch (InterruptedException e) {
		} catch (IllegalMonitorStateException e) {
			e.printStackTrace();
		}
	}

	private void waitForProcessableSources() {

		boolean processableSources = false;
		while (!processableSources && sources.size() > 0) {
			for (IIterableSource<?> others : sources) {
				if (others.isOpen()) {
					processableSources = true;
					break;
				}
			}
			try {
				if (!processableSources) {
					logger.debug("Waiting for open sources to process "
							+ sources);
					sources.wait(1000);
					updateSources();
				}
			} catch (InterruptedException e) {
			}
		}
	}

	private boolean processSources() {

		boolean waitedForFirstSource = false;

		boolean processableSources = false;
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
						long ct2 = System.currentTimeMillis();
						// The first round nobody has to wait
						if (lastRun != null) {
							long diff = ct2 - lastRun;
							try {
								logger.trace("Sleeping ..." + s);
								if (s.getDelay() - diff > 0) {
									Thread.sleep(s.getDelay() - diff);
								}
							} catch (InterruptedException e) {
								// Exception can be ignored
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
		return processableSources;
	}

	private void waitForOpen() {
		boolean open = false;
		// Wait for at least one open source
		synchronized (sources) {

			while (!open) {

				updateSources();

				for (IIterableSource<?> s : sources) {
					if (s.isOpen()) {
						logger.debug("Opened " + this.hashCode()
								+ "... Start Processing of Source " + s);
						open = true;
						break;
					}
				}

				try {
					if (!open) {
						sources.wait(1000);
					}
				} catch (InterruptedException ignored) {
				}

			}
		}
		logger.debug("At least one source is open");
	}

	private void updateSources() {
		synchronized (sources) {
			if (this.sourcesChangeRequested) {
				delayedAddSources();
				delayedRemoveSources();
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
		return Collections.synchronizedCollection(sources);
	}

	@Override
	public synchronized void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		synchronized (sources) {
			sources.notifyAll();
		}
	}

	public void removeSource(IIterableSource<?> source) {
		synchronized (sources) {
			this.sourcesChangeRequested = true;
			toRemove.add(source);
			source.unsubscribe(this, POEventType.OpenDone);
			sources.notifyAll();
		}
	}

	public synchronized void addSource(IIterableSource<?> source) {
		synchronized (sources) {
			this.sourcesChangeRequested = true;
			toAdd.add(source);
			source.subscribe(this, POEventType.OpenDone);
			sources.notifyAll();
		}
	}

	private void delayedAddSources() {
		synchronized (sources) {
			Iterator<IIterableSource<?>> sourceIter = toAdd.iterator();
			while (sourceIter.hasNext()) {
				IIterableSource<?> source = sourceIter.next();
				sources.add(source);
				toAdd.remove(source);
				logger.debug("Added Source " + source + " " + sources);
			}
			sources.notifyAll();
			this.sourcesChangeRequested = false;
		}
	}

	private void delayedRemoveSources() {
		synchronized (sources) {
			Iterator<IIterableSource<?>> sourceIter = toRemove.iterator();
			while (sourceIter.hasNext()) {
				IIterableSource<?> source = sourceIter.next();
				sources.remove(source);
				toRemove.remove(source);
				logger.debug("Removed Source " + source + " " + sources);
			}
			sources.notifyAll();
			this.sourcesChangeRequested = false;
		}
	}

	public int getRunningSources() {
		return sources.size();
	}

	@Override
	public String toString() {
		synchronized (sources) {
			return this.getClass().getSimpleName() + " " + sources;
		}
	}
}
