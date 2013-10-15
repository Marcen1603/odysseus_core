package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;

public class MultipleSourceExecutor extends Thread implements IEventListener,
		ISourceExecutor {

	Logger logger = LoggerFactory.getLogger(MultipleSourceExecutor.class);
	boolean alldone = false;
	private boolean interrupt = false;
	final private List<IIterableSource<?>> sources = new ArrayList<>();
	final private Comparator<IIterableSource<?>> sourcesComparator = new Comparator<IIterableSource<?>>() {
		@Override
		public int compare(IIterableSource<?> left, IIterableSource<?> right) {
			long l = left.isOpen()?left.getDelay():0;
			long r = right.isOpen()?right.getDelay():0;
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
		interrupt = false;
		boolean open = false;
		// Wait for at least one open source
		logger.debug("Waiting for at least one open source");
		synchronized (sources) {

			for (IIterableSource<?> s : sources) {
				s.subscribe(this, POEventType.OpenDone);
			}
			while (!open) {
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
		while (!interrupt && !isInterrupted() && caller.isRunning()) {
			synchronized (sources) { // No interruptions while one run
				if (this.sourcesChangeRequested) {
					Collections.sort(sources, sourcesComparator);
					try {
						sources.wait(10);
					} catch (InterruptedException e) {
					} catch (IllegalMonitorStateException e) {
						e.printStackTrace();
					}
				}

				boolean waitedForFirstSource = false;

				for (IIterableSource<?> s : sources) {
					if (s.isOpen() && !s.isDone()) {

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
								if (lastRun == null
										|| ct2 - lastRun >= s.getDelay()) {
									transfer(s);
								}
							}
						}
					} else {
						alldone = true;
						while (alldone) {
							for (IIterableSource<?> others : sources) {
								if (!others.isDone()) {
									alldone = false;
									break;
								}
							}
							try {
								sources.wait(1000);
							} catch (InterruptedException e) {
							}
						}
					}
				}
			}
		}

		// logger.debug("Removing " + this.hashCode() + " Sources " + sources);
		// caller.removeSourceThread(this);

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
		this.sourcesChangeRequested = true;
		synchronized (sources) {
			logger.debug("Removing Source " + source);
			sources.remove(source);
			this.sourcesChangeRequested = false;
			sources.notifyAll();
		}
	}

	public synchronized void addSource(IIterableSource<?> source) {
		this.sourcesChangeRequested = true;
		synchronized (sources) {
			sources.add(source);
			logger.debug("Added Source " + source + " " + sources);
			alldone = false;
			this.sourcesChangeRequested = false;
			sources.notifyAll();
		}
	}

	public int getRunningSources() {
		return sources.size();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " " + sources;
	}
}
