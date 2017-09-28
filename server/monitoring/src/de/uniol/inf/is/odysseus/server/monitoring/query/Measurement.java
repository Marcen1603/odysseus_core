package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class Measurement {

	private Map<String, OperatorLatency> latencys;
	private long currentLatency, averageLatency;
	//TODO: Doppelte Verkettung auflösen durch Liste mit HashMaps 
	private List<HashMap<String, OperatorLatency>> temporaryLatencys;
	private List<String> roots;

	public Measurement(IPhysicalOperator root) {
		this.temporaryLatencys = new ArrayList<HashMap<String, OperatorLatency>>();
		this.latencys = new HashMap<String, OperatorLatency>();
		this.roots = new ArrayList<String>();

		this.roots.add(root.toString());
	}

	public Measurement() {
		this.temporaryLatencys = new ArrayList<HashMap<String, OperatorLatency>>();
		this.latencys = new HashMap<String, OperatorLatency>();
		this.roots = new ArrayList<String>();
	}

	public synchronized void addRoot(IPhysicalOperator root) {
		this.roots.add(root.toString());
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

		if (!this.temporaryLatencys.isEmpty()) {
			loop: for (HashMap i : this.temporaryLatencys) {
				HashMap<String, OperatorLatency> entrys = this.temporaryLatencys.get(i);
				if (event.getEventType().equals(POEventType.ProcessInit)) {
					if (entrys.containsKey(operatorName)) {
						if (entrys.get(operatorName).isStarted()) {
							continue;
						} else {
							entrys.get(operatorName).startMeasurement(nanoTimestamp);
							break loop;
						}
					} else {
						OperatorLatency l = new OperatorLatency();
						l.startMeasurement(nanoTimestamp);
						entrys.put(operatorName, l);
						break loop;
					}
				}

				if (event.getEventType().equals(POEventType.PushInit)) {
					if (entrys.containsKey(operatorName)) {
						if (entrys.get(operatorName).isStopped()) {
							continue;
						} else {
							entrys.get(operatorName).stopMeasurement(nanoTimestamp);
							break loop;
						}
					} else {
						OperatorLatency l = new OperatorLatency();
						l.stopMeasurement(nanoTimestamp);
						entrys.put(operatorName, l);
						break loop;
					}
				}

				if (event.getEventType().equals(POEventType.ProcessDone)) {
					if (entrys.containsKey(operatorName)) {
						if (entrys.get(operatorName).isDone()) {
							continue;
						} else {
							entrys.get(operatorName).setDone();
							break loop;
						}
					} else {
						addInitialInnerHashMap(operatorName, nanoTimestamp, event, i + 1);
					}
				}
			}
		} else {
			addInitialInnerHashMap(operatorName, nanoTimestamp, event, 0);
		}
		if (event.getEventType().equals(POEventType.ProcessDone)) {
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
		if (event.getEventType().equals(POEventType.ProcessDone)) {
			OperatorLatency l = new OperatorLatency(operatorName);
			l.setDone();
			tempMap.put(operatorName, l);
			this.temporaryLatencys.put(position, tempMap);
		}
	}

	/**
	 * Runs through temporaryLatencys and searches for completed entries to
	 * delete them.
	 */
	private synchronized void checkForRemovableEntrys() {
		for (Integer i : this.temporaryLatencys.keySet()) {
			int count = 0;
			for (String s : this.temporaryLatencys.get(i).keySet()) {
				OperatorLatency l = this.temporaryLatencys.get(i).get(s);
				if (l.isDone() && l.isStarted()) {
					count++;
				}
			}
			if (count == this.temporaryLatencys.get(i).size()) {
				boolean deleted = false;
				for (String o : roots) {
					if (this.temporaryLatencys.get(i).containsKey(o)
							&& this.temporaryLatencys.get(i).get(o).isConfirmed()) {
						this.latencys.putAll(this.temporaryLatencys.get(i));
						this.temporaryLatencys.remove(i);
						calcQueryLatency();
						deleted = true;
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
		if (getCurrentLatency() != 0) {
			setAverageLatency((latency + getCurrentLatency()) / 2);
		}

		System.out.println("Total Latency: " + latency);
		setCurrentLatency(latency);
		// TODO:Maybe error handling could be implemented
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

	public synchronized long getAverageLatency() {
		return averageLatency;
	}

	public synchronized void setAverageLatency(long averageLatency) {
		this.averageLatency = averageLatency;
	}

}