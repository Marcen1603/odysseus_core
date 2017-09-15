package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class Measurement {

	private Map<String, OperatorLatency> latencys;
	private long currentLatency;
	private Map<Integer, HashMap<String, OperatorLatency>> temporaryLatencys;
	private List<String> roots;

	public Measurement(IPhysicalQuery query) {
		this.temporaryLatencys = new HashMap<Integer, HashMap<String, OperatorLatency>>();
		this.latencys = new HashMap<String, OperatorLatency>();
		this.roots = new ArrayList<String>();

		for (IPhysicalOperator o : query.getRoots()) {
			this.roots.add(o.toString());
		}
	}

	/**
	 * Calculates the latency for one operator if the PushInitEvent occurs.
	 * 
	 * @param event
	 *            Name of the Operator.
	 * @param nanoTimestamp
	 *            Timestamp of the PushInitEvent in ns.
	 */
	public synchronized void processEvent(IEvent<?, ?> event, long nanoTimestamp) {
		String operatorName = event.getSender().toString();
		boolean added = false;

		if (!this.temporaryLatencys.isEmpty()) {
			outerloop: for (Integer i : this.temporaryLatencys.keySet()) {
				HashMap<String, OperatorLatency> entrys = this.temporaryLatencys.get(i);
				if (event.getEventType().equals(POEventType.ProcessInit)) {
					if (entrys.containsKey(operatorName)) {
						if (entrys.get(operatorName).isStarted()) {
							continue;
						} else {
							entrys.get(operatorName).startMeasurement(nanoTimestamp);
							added = true;
							break outerloop;
						}
					} else {
						OperatorLatency l = new OperatorLatency();
						l.startMeasurement(nanoTimestamp);
						entrys.put(operatorName, l);
						added = true;
						break outerloop;
					}
				}

				if (event.getEventType().equals(POEventType.PushInit)) {
					if (entrys.containsKey(operatorName)) {
						if (entrys.get(operatorName).isStopped()) {
							continue;
						} else {
							entrys.get(operatorName).stopMeasurement(nanoTimestamp);
							added = true;
							break outerloop;
						}
					} else {
						OperatorLatency l = new OperatorLatency();
						l.stopMeasurement(nanoTimestamp);
						entrys.put(operatorName, l);
						added = true;
						break outerloop;
					}
				}

				if (event.getEventType().equals(POEventType.ProcessDone)) {
					if (entrys.containsKey(operatorName)) {
						if (entrys.get(operatorName).isDone()) {
							continue;
						} else {
							entrys.get(operatorName).setDone();
							added = true;
							break outerloop;
						}
					}
				}
				if (!added) {
					// No matching Event found, a new one has to be created
					addInitialInnerHashMap(operatorName, nanoTimestamp, event, i);
				}
			}
		} else {
			addInitialInnerHashMap(operatorName, nanoTimestamp, event, 0);
		}
		if (event.getEventType().equals(POEventType.PushInit)
				||event.getEventType().equals(POEventType.ProcessDone)) {
			checkForRemovableEntrys();
		}
	}

	/**
	 * Initializes and adds a new entry to temporaryLatencys
	 * 
	 * @param operatorName
	 * @param nanoTimestamp
	 * @param event
	 */
	private void addInitialInnerHashMap(String operatorName, long nanoTimestamp, IEvent<?, ?> event, int position) {
		HashMap<String, OperatorLatency> tempMap = new HashMap<String, OperatorLatency>();

		if (event.getEventType().equals(POEventType.ProcessInit)) {
			OperatorLatency l = new OperatorLatency();
			l.startMeasurement(nanoTimestamp);
			tempMap.put(operatorName, l);
			this.temporaryLatencys.put(position, tempMap);
		}
		if (event.getEventType().equals(POEventType.PushInit)) {
			OperatorLatency l = new OperatorLatency(operatorName);
			l.stopMeasurement(nanoTimestamp);
			tempMap.put(operatorName, l);
			this.temporaryLatencys.put(position, tempMap);
		}
		if (event.getEventType().equals(POEventType.PushInit)) {
			OperatorLatency l = new OperatorLatency(operatorName);
			l.setDone();
			tempMap.put(operatorName, l);
			this.temporaryLatencys.put(position, tempMap);
		}
	}

	private synchronized void checkForRemovableEntrys() {
		int count = 0;
		for (Integer i : this.temporaryLatencys.keySet()) {
			for (String s : this.temporaryLatencys.get(i).keySet()) {
				OperatorLatency l = this.temporaryLatencys.get(i).get(s);
				if (l.isDone() && l.isStarted()) {
					count++;
				}
			}
			if (count == this.temporaryLatencys.get(i).size()) {
				boolean deleted = false;
				for (String o : roots) {
					if (this.temporaryLatencys.get(i).containsKey(o)) {
						if (this.temporaryLatencys.get(i).get(o).isConfirmed()) {
							this.latencys.putAll(this.temporaryLatencys.get(i));
							this.temporaryLatencys.remove(i);
							calcQueryLatency();
							deleted = true;
						}
					}
				}
				if (!deleted) {
					this.temporaryLatencys.remove(i);
				}
			}
		}
	}

	/**
	 * Calculates the Latency in latencys
	 */
	private synchronized void calcQueryLatency() {
		long latency = 0;
		for (String o : this.latencys.keySet()) {
			latency += this.latencys.get(o).getLatency();
			System.out.println(o + " Latenz: " + this.latencys.get(o).getLatency());
		}
		System.out.println("Total Latency: " + latency);
		setCurrentLatency(latency);
		if (latency <= 0) {
			System.out.println("Fehler");
		}
	}

	private synchronized void setCurrentLatency(long currentLatency) {
		this.currentLatency = currentLatency;
	}

	public synchronized long getCurrentLatency() {
		return this.currentLatency;
	}

}