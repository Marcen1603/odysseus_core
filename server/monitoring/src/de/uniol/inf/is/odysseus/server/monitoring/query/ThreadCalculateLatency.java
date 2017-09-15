package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.Comparator;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.event.IEvent;

public class ThreadCalculateLatency extends Thread {

	private PriorityQueue<LatencyEvent> queue;
	private volatile boolean running = true;
	Comparator<LatencyEvent> comparator = new EventComparator();

	public ThreadCalculateLatency() {
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
			//TODO: Remove size>5? : Has to be for very short querys.
			if (!queue.isEmpty() && queue.size()>5) {
				processEvent();
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
		System.out.println(time + " " + e.toString());
	}

	public void addEvent(Measurement m, IEvent<?, ?> e, long nanoTimestamp) {
		synchronized (queue) {
			queue.add(new LatencyEvent(m, e, nanoTimestamp));
		}
	}

	public LatencyEvent removeEvent() {
		synchronized (queue) {
			return queue.poll();
		}
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
