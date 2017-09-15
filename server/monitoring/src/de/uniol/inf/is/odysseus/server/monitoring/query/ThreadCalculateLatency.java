package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.Iterator;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.IMeasurableValue;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class ThreadCalculateLatency extends Thread {

	Measurement measurement;
	private PriorityQueue<MyEvent> queue;
	private volatile boolean running = true;
	Comparator<MyEvent> comparator = new MyComparator();

	public ThreadCalculateLatency(Measurement m) {
	}

	public ThreadCalculateLatency() {
		// TODO Auto-generated constructor stub

		this.setName("Calculating_Latencys");
		this.queue = new PriorityQueue<MyEvent>(comparator);
	}

	public void shutdown() {
		this.running = false;
	}

	/**
	 * Looks at queue for new Events
	 */
	@Override
	public void run() {
		while (running) {
			if (!queue.isEmpty() && queue.size()>5) {
				processEvent();
			}
		}
	}

	private void processEvent() {
		MyEvent e = removeEvent();
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
			queue.add(new MyEvent(m, e, nanoTimestamp));
		}
	}

	public MyEvent removeEvent() {
		synchronized (queue) {
			return queue.poll();
		}
	}
}

class MyComparator implements Comparator<MyEvent> {

	@Override
	public int compare(MyEvent o1, MyEvent o2) {
		if (o1.getNanoTimestamp() < o2.getNanoTimestamp()) {
			return -1;
		}
		if (o1.getNanoTimestamp() > o2.getNanoTimestamp()) {
			return 1;
		}
		return 0;
	}
}

class MyEvent {
	private IEvent<?, ?> event;
	private long nanoTimestamp;
	private Measurement measurement;

	protected MyEvent(Measurement m, IEvent<?, ?> e, long nanoTimeStamp) {
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
