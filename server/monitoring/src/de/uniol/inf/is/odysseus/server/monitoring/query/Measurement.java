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

	private Map<IPhysicalOperator, OperatorLatency> latencys;
	private long currentLatency, averageLatency;
	private List<HashMap<IPhysicalOperator, OperatorLatency>> temporaryLatencys;
	private IPhysicalOperator root;

	public Measurement(IPhysicalOperator root) {
		this.temporaryLatencys = new ArrayList<HashMap<IPhysicalOperator, OperatorLatency>>();
		this.latencys = new HashMap<IPhysicalOperator, OperatorLatency>();

		this.root = root;
	}

	public Measurement() {
		this.temporaryLatencys = new ArrayList<HashMap<IPhysicalOperator, OperatorLatency>>();
		this.latencys = new HashMap<IPhysicalOperator, OperatorLatency>();
	}

	public synchronized void addRoot(IPhysicalOperator root) {
		this.root = root;
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
		IPhysicalOperator operator = (IPhysicalOperator) event.getSender();

		if (!this.temporaryLatencys.isEmpty()) {
			loop: for (HashMap<IPhysicalOperator, OperatorLatency> entrys : this.temporaryLatencys) {
				if (event.getEventType().equals(POEventType.ProcessInit)) {
					if (entrys.containsKey(operator)) {
						if (entrys.get(operator).isStarted()) {
							continue;
						} else {
							entrys.get(operator).startMeasurement(nanoTimestamp);
							break loop;
						}
					} else {
						OperatorLatency l = new OperatorLatency(operator);
						l.startMeasurement(nanoTimestamp);
						entrys.put(operator, l);
						break loop;
					}
				}

				if (event.getEventType().equals(POEventType.PushInit)) {
					if (entrys.containsKey(operator)) {
						if (entrys.get(operator).isStopped()) {
							if (entrys.get(operator).isStarted()) {
								OperatorLatency l = new OperatorLatency(operator);
								l.stopMeasurement(nanoTimestamp);
								long lastPushInitEvent = entrys.get(operator).getEndTime();
								l.startMeasurement(lastPushInitEvent);
								entrys.put(operator, l);
								break loop;
							}
							continue;
						} else {
							entrys.get(operator).stopMeasurement(nanoTimestamp);
							break loop;
						}
					} else {
						OperatorLatency l = new OperatorLatency(operator);
						l.stopMeasurement(nanoTimestamp);
						entrys.put(operator, l);
						break loop;
					}
				}

				if (event.getEventType().equals(POEventType.ProcessDone)) {
					// TODO: Handle Buffer

					if (entrys.containsKey(operator)) {
						if (entrys.get(operator).isDone()) {
							continue;
						} else {
							entrys.get(operator).setDone();
							if (operator.getName().contains("Buffer")) {
								entrys.get(operator).stopMeasurement(nanoTimestamp);
							}
							break loop;
						}
					} else {
						addInitialInnerHashMap(operator, nanoTimestamp, event);
					}
				}
			}
		} else {
			addInitialInnerHashMap(operator, nanoTimestamp, event);
		}
		if (event.getEventType().equals(POEventType.ProcessDone)) {
			checkForRemovableEntrys();
		}
	}

	/**
	 * Initializes and adds a new entry to temporaryLatencys
	 * 
	 * @param operator
	 * @param nanoTimestamp
	 * @param event
	 */
	private void addInitialInnerHashMap(IPhysicalOperator operator, long nanoTimestamp, IEvent<?, ?> event) {
		HashMap<IPhysicalOperator, OperatorLatency> tempMap = new HashMap<IPhysicalOperator, OperatorLatency>();

		if (event.getEventType().equals(POEventType.ProcessInit)) {
			OperatorLatency l = new OperatorLatency();
			l.startMeasurement(nanoTimestamp);
			tempMap.put(operator, l);
			this.temporaryLatencys.add(tempMap);
		}
		// TODO: Maybe remove this for the Join Operator
		if (event.getEventType().equals(POEventType.PushInit)) {
			OperatorLatency l = new OperatorLatency(operator);
			l.stopMeasurement(nanoTimestamp);
			tempMap.put(operator, l);
			this.temporaryLatencys.add(tempMap);
		}
		if (event.getEventType().equals(POEventType.ProcessDone)) {
			OperatorLatency l = new OperatorLatency(operator);
			l.setDone();
			tempMap.put(operator, l);
			this.temporaryLatencys.add(tempMap);
		}
	}

	/**
	 * Runs through temporaryLatencys and searches for completed entries to
	 * delete them.
	 */
	private synchronized void checkForRemovableEntrys() {
		for (int i = 0; i < this.temporaryLatencys.size(); i++) {
			int count = 0;
			for (IPhysicalOperator o : this.temporaryLatencys.get(i).keySet()) {
				OperatorLatency l = this.temporaryLatencys.get(i).get(o);
				if (l.isDone() && l.isStarted()) {
					count++;
				}
			}
			if (count == this.temporaryLatencys.get(i).size()) {
				boolean deleted = false;
				if (this.temporaryLatencys.get(i).containsKey(root)
						&& this.temporaryLatencys.get(i).get(root).isConfirmed()) {
					this.latencys.putAll(this.temporaryLatencys.get(i));
					this.temporaryLatencys.remove(i);
					calcQueryLatency();
					deleted = true;
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
		for (IPhysicalOperator o : this.latencys.keySet()) {
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