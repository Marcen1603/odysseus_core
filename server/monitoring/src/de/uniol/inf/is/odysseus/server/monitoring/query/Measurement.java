package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
	 * Creates new entry in temporaryLatencys if a new ProcessInitEvent occurs.
	 * 
	 * @param operatorName
	 *            Name of the Operator.
	 * @param nanoTimestamp
	 *            Timestamp of the PushInitEvent in ns.
	 */
	public synchronized void createNewTempLatency(String operatorName, long nanoTimestamp) {

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
		IPhysicalOperator o = (IPhysicalOperator) event.getSender();
		String operatorName = o.toString();
		boolean added = false;
		// Handle empty TreeMap
		if (!temporaryLatencys.isEmpty()) {
			// Handle innerList
			outerloop: for (Integer i : temporaryLatencys.keySet()) {
				// if (temporaryLatencys.get(i) != null &&
				// !temporaryLatencys.get(i).isEmpty()) {
				innerloop: for (String s : temporaryLatencys.get(i).keySet()) {
					// ProcessInit Event
					if (event.getEventType().equals(POEventType.ProcessInit)) {
						// List empty
						if (temporaryLatencys.get(i).containsKey(operatorName)) {
							// Next innerList
							if (temporaryLatencys.get(i).get(operatorName).isStarted()) {
								break innerloop;
							} else {
								temporaryLatencys.get(i).get(operatorName).startMeasurement(nanoTimestamp);
								added = true;
								break outerloop;
							}
						} else {
							OperatorLatency l = new OperatorLatency();
							l.startMeasurement(nanoTimestamp);
							temporaryLatencys.get(i).put(operatorName, l);
							added = true;
							break outerloop;
						}
					}
					if (event.getEventType().equals(POEventType.PushInit)) {
						// List empty
						if (temporaryLatencys.get(i).containsKey(operatorName)) {
							// Next innerList
							if (temporaryLatencys.get(i).get(operatorName).isStopped()) {
								break innerloop;
							} else {
								temporaryLatencys.get(i).get(operatorName).stopMeasurement(nanoTimestamp);

								added = true;
								break outerloop;
							}
						} else {
							OperatorLatency l = new OperatorLatency();
							l.stopMeasurement(nanoTimestamp);
							temporaryLatencys.get(i).put(operatorName, l);
							added = true;
							break outerloop;
						}
					}
					if (event.getEventType().equals(POEventType.ProcessDone)) {
						if (temporaryLatencys.get(i).containsKey(operatorName)) {
							if (temporaryLatencys.get(i).get(operatorName).isDone()) {
								break innerloop;
							} else {
								temporaryLatencys.get(i).get(operatorName).setDone();
								added = true;
								break outerloop;
							}
						}
					}
					// }
				}
				if (!added) {
					// If inner HashMap is empty
					HashMap<String, OperatorLatency> tempMap = new HashMap<String, OperatorLatency>();

					if (event.getEventType().equals(POEventType.ProcessInit)) {
						OperatorLatency l = new OperatorLatency();
						l.startMeasurement(nanoTimestamp);
						tempMap.put(operatorName, l);
						temporaryLatencys.put(i, tempMap);
						break outerloop;
					}
					if (event.getEventType().equals(POEventType.PushInit)) {
						OperatorLatency l = new OperatorLatency(operatorName);
						l.stopMeasurement(nanoTimestamp);
						tempMap.put(operatorName, l);
						temporaryLatencys.put(i, tempMap);
						break outerloop;
					}
					if (event.getEventType().equals(POEventType.PushInit)) {
						OperatorLatency l = new OperatorLatency(operatorName);
						l.setDone();
						tempMap.put(operatorName, l);
						temporaryLatencys.put(i, tempMap);
						break outerloop;
					}
				}
			}
		} else {
			// If HashMap is empty
			HashMap<String, OperatorLatency> tempMap = new HashMap<String, OperatorLatency>();

			if (event.getEventType().equals(POEventType.ProcessInit)) {
				OperatorLatency l = new OperatorLatency();
				l.startMeasurement(nanoTimestamp);
				tempMap.put(operatorName, l);
				this.temporaryLatencys.put(0, tempMap);
			}

			if (event.getEventType().equals(POEventType.PushInit)) {
				OperatorLatency l = new OperatorLatency();
				l.stopMeasurement(nanoTimestamp);
				tempMap.put(operatorName, l);
				this.temporaryLatencys.put(0, tempMap);
			}

			if (event.getEventType().equals(POEventType.ProcessDone)) {
				OperatorLatency l = new OperatorLatency();
				l.setDone();
				tempMap.put(operatorName, l);
				this.temporaryLatencys.put(0, tempMap);
			}
		}
		if (event.getEventType().equals(POEventType.ProcessDone)
				|| event.getEventType().equals(POEventType.ProcessDone)) {
			checkForRemovableEntrys();
		}
	}

	private synchronized void checkForRemovableEntrys() {
		int count = 0, completed = 0;
		for (Integer i : temporaryLatencys.keySet()) {
			for (String s : temporaryLatencys.get(i).keySet()) {
				OperatorLatency l = temporaryLatencys.get(i).get(s);
				if (l.isDone() && l.isStarted()) {
					if (l.isConfirmed()) {
						completed++;
					}
					count++;
				}
			}
			if (count == temporaryLatencys.get(i).size()) {
				boolean deleted = false;
				for (String o : roots) {
					if (temporaryLatencys.get(i).containsKey(o)) {
						if (!temporaryLatencys.get(i).get(o).isConfirmed()) {
							return;
						}
						this.latencys.putAll(temporaryLatencys.get(i));
						temporaryLatencys.remove(i);
						calcQueryLatency();
						deleted = true;
					}
				}
				if (!deleted) {
					temporaryLatencys.remove(i);
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