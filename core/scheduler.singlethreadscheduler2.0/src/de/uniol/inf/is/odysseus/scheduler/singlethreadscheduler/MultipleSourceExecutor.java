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
	final private Map<IIterableSource<?>, Long> lastRun = new HashMap<>();

	private static long counter = 0;

	public MultipleSourceExecutor(
			AbstractSimpleThreadScheduler singleThreadScheduler) {
		this.setName("MultiSourceExecutor #" + (counter++) + " ");
		this.caller = singleThreadScheduler;

	}

	@Override
	public void run() {
		logger.debug("Starting source scheduling for " + sources + " - " + this
				+ "(" + this.hashCode() + ")");
		interrupt = false;
		for (IIterableSource<?> s : sources) {
			// logger.debug("Adding Source. " + s + ".Waiting for open ...");
			s.subscribe(this, POEventType.OpenDone);
			synchronized (this) {
				while (!interrupt && !s.isOpen() && !isInterrupted()
						&& caller.isRunning()) {
					try {
						this.wait(1000);
					} catch (InterruptedException ignored) {
					}
				}
			}
			if (!interrupt) {
				logger.debug("Opened " + this.hashCode()
						+ "... Start Processing of Source " + s);
			}
		}
		logger.debug("All sources are open");
		boolean alldone = false;
		boolean firstRun = false;
		while (!interrupt && !isInterrupted() && !alldone) {
			synchronized (sources) { // No interruptions while one run
				for (IIterableSource<?> s : sources) {
					if (s.isOpen() && !s.isDone()) {
						if (s.hasNext()) {
							Long ct1 = -1l;
							if (s.getDelay() > 0) {
								ct1 = lastRun.get(s);
								if (ct1 == null) {
									ct1 = System.currentTimeMillis();
									firstRun = true;
								}
							}
							s.transferNext();
							if (s.getDelay() > 0) {
								// The first round nobody has to wait
								if (!firstRun) {
									long ct2 = System.currentTimeMillis();
									long diff = ct2 - ct1;
									try {
										if (s.getDelay() - diff > 0) {
											Thread.sleep(s.getDelay() - diff);
										}
									} catch (InterruptedException e) {
										// Exception can be ignored
									}
								}
								lastRun.put(s, ct1);
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
								this.wait(1000);
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
		notifyAll();
	}

	public void removeSource(IIterableSource<?> source) {
		synchronized (sources) {
			sources.remove(source);
		}
	}

	public synchronized void addSource(IIterableSource<?> source) {
		synchronized (sources) {
			sources.add(source);
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
