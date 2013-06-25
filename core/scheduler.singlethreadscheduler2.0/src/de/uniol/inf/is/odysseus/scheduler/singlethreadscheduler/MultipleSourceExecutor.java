package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

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
	// Sort source regarding delay
	final private PriorityQueue<IIterableSource<?>> sources = new PriorityQueue<IIterableSource<?>>(
			11, new Comparator<IIterableSource<?>>() {
				@Override
				public int compare(IIterableSource<?> left,
						IIterableSource<?> right) {
					return Long.compare(left.getDelay(), right.getDelay());
				}
			});
	final private AbstractSimpleThreadScheduler caller;
	final private Map<IIterableSource<?>, Long> lastRuns = new HashMap<>();

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
		boolean firstRun = false;
		while (!interrupt && !isInterrupted()) {
			synchronized (sources) { // No interruptions while one run
				boolean waitedForFirstSource = false;
				for (IIterableSource<?> s : sources) {
					if (s.isOpen() && !s.isDone()) {

						if (s.hasNext()) {
							Long lastRun = -1l;
							if (s.getDelay() > 0) {
								lastRun = lastRuns.get(s);
								if (lastRun == null) {
									lastRun = System.currentTimeMillis();
									firstRun = true;
								} else {
									firstRun = false;
								}
							}
							// Only delay source with shortest waiting time
							// all other sources should be testet each time
							// ... its some kind of busy wait ... :-/
							if (!waitedForFirstSource) {
								waitedForFirstSource = true;
								long ct2 = System.currentTimeMillis();
								transfer(s);
								if (s.getDelay() > 0) {
									// The first round nobody has to wait
									if (!firstRun) {
										long diff = ct2 - lastRun;
										try {
											if (s.getDelay() - diff > 0) {
												Thread.sleep(s.getDelay()
														- diff);
											}
										} catch (InterruptedException e) {
											// Exception can be ignored
										}
									}
								}
							} else { // Handle next sources
								if (firstRun) {
									transfer(s);
								} else {
									long ct2 = System.currentTimeMillis();
									long diff = ct2 - lastRun;
									if (diff >= s.getDelay()) {
										transfer(s);
									}
								}
							}
						}
					} else {
						alldone = true;
						for (IIterableSource<?> others : sources) {
							if (!others.isDone()) {
								alldone = false;
							}
						}
						while (alldone) {
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
		//logger.debug("Transfer for " + s + " " + ct2);
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

	@Override
	public synchronized void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		synchronized (sources) {
			notifyAll();
		}
	}

	public void removeSource(IIterableSource<?> source) {
		synchronized (sources) {
			logger.debug("Removing Source " + source);
			sources.remove(source);
		}
	}

	public synchronized void addSource(IIterableSource<?> source) {
		synchronized (sources) {
			logger.debug("Adding Source " + source);
			sources.add(source);
			alldone = false;
			notifyAll();
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
