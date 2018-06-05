package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;

public class ThreadCalculateLatency extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadCalculateLatency.class);

	private static final int minQueueLength = 5;
	private PriorityQueue<LatencyEvent> queue;
	private volatile boolean running = true;
	Comparator<LatencyEvent> comparator = new EventComparator();
	private boolean waiting = false;

	public ThreadCalculateLatency() {
		this.setDaemon(true);
		this.setName("Calculating_Latencys");
		this.queue = new PriorityQueue<LatencyEvent>(comparator);
	}

	public void shutdown() {
		this.running = false;
	}

	/**
	 * Looks at queue for new events
	 */
	@Override
	public void run() {
		while (running) {
			if (!queue.isEmpty() && queue.size() >= minQueueLength) {
				processEvent();
			} else {
				waitForEvent();
			}
		}
	}

	private void waitForEvent() {
		synchronized (this) {
			waiting = true;
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void processEvent() {
		LatencyEvent e = removeEvent();
		long time = e.getNanoTimestamp();
		IEvent<?, ?> event = e.getEvent();
		Measurement m = e.getMeasurement();
		printEvent(event, time);
		m.processEvent(event, time);
	}

	private void printEvent(IEvent<?, ?> e, long time) {
		ThreadCalculateLatency.LOG.debug(time + " " + e.toString());
	}

	public synchronized void addEvent(Measurement m, IEvent<?, ?> e, long nanoTimestamp) {
		queue.add(new LatencyEvent(m, e, nanoTimestamp));	
		if (waiting) {
			this.notifyAll();
		}
	}

	public synchronized LatencyEvent removeEvent() {
		return queue.poll();
	}

	public synchronized boolean isWaiting() {
		return waiting;
	}
}

class EventComparator implements Comparator<LatencyEvent> {

	@Override
	public int compare(LatencyEvent o1, LatencyEvent o2) {
		if (o1.getNanoTimestamp() < o2.getNanoTimestamp()) {
			return -1;
		}
		if (o1.getNanoTimestamp() > o2.getNanoTimestamp()) {
			return 1;
		}
		return 0;
	}
}

class LatencyEvent {
	private IEvent<?, ?> event;
	private long nanoTimestamp;
	private Measurement measurement;

	protected LatencyEvent(Measurement m, IEvent<?, ?> e, long nanoTimeStamp) {
		this.setEvent(e);
		this.setNanoTimestamp(nanoTimeStamp);
		this.setMeasurement(m);
	}

	public long getNanoTimestamp() {
		return nanoTimestamp;
	}

	public void setNanoTimestamp(long nanoTimestamp) {
		this.nanoTimestamp = nanoTimestamp;
	}

	public IEvent<?, ?> getEvent() {
		return event;
	}

	public void setEvent(IEvent<?, ?> event) {
		this.event = event;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}
}
