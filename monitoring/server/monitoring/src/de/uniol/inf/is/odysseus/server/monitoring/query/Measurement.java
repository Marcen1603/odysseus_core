package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class Measurement {
	private static final Logger LOG = LoggerFactory.getLogger(Measurement.class);

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
	 * Calculates the latency for one operator if an Event occurs.
	 * 
	 * @param event
	 *            Name of the Operator.
	 * @param nanoTimestamp
	 *            Timestamp of the Event in ns.
	 */
	public synchronized void processEvent(IEvent<?, ?> event, long nanoTimestamp) {
		IPhysicalOperator operator = (IPhysicalOperator) event.getSender();
		if (!this.temporaryLatencys.isEmpty()) {
			boolean added=false;
			loop: for (HashMap<IPhysicalOperator, OperatorLatency> map : this.temporaryLatencys) {
				if (event.getEventType().equals(POEventType.ProcessInit)) {
					if (map.containsKey(operator)) {
						if (map.get(operator).isStarted()) {
							continue;
						} else {
							map.get(operator).startMeasurement(nanoTimestamp);
							return;
						}
					} else {
						OperatorLatency l = new OperatorLatency(operator);
						l.startMeasurement(nanoTimestamp);
						map.put(operator, l);
						return;
					}
				}

				if (event.getEventType().equals(POEventType.PushInit)) {
					if (map.containsKey(operator)) {
						if (map.get(operator).isStopped()) {
							continue;
						} else {
							map.get(operator).stopMeasurement(nanoTimestamp);
							break loop;
						}
					} else {
						//Handle PushInit Events for operator with integrated buffer
						if (isBufferedOperator(operator)){
							return;
						}
						map.get(operator).stopMeasurement(nanoTimestamp);
						return;
					}
				}

				if (event.getEventType().equals(POEventType.ProcessDone)) {
					if (map.containsKey(operator)) {
						if (map.get(operator).isDone()) {
							continue;
						} else {
							map.get(operator).setDone();
							added = true;
							break loop;
						}
					} else {
						OperatorLatency l = new OperatorLatency(operator);
						l.setDone();
						map.put(operator, l);
						return;
					}
				}
			}
			if (!added){
				addInitialInnerHashMap(operator, nanoTimestamp, event);
			}
		} else {
			addInitialInnerHashMap(operator, nanoTimestamp, event);
		}
		if (event.getEventType().equals(POEventType.ProcessDone) && !operator.getName().contains("Buffer")) {
			checkForRemovableEntrys();
		}
		if (event.getEventType().equals(POEventType.PushInit) && operator.getName().contains("Buffer")){
			checkForRemovableEntrys();
		}
	}

	/**
	 * Returns true if the operator contains buffer
	 * TODO: Remove identification based on their names
	 * @param operator
	 * @return
	 */
	private boolean isBufferedOperator(IPhysicalOperator operator){
		if (   operator.getName().contains("Aggregate")
			|| operator.getName().contains("Join")							
			|| operator.getName().contains("Buffer")
			|| operator.getName().contains("Union")){
			return true;
		}
		else {
			return false;
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
				//Case all operator have a latency
				if (this.temporaryLatencys.get(i).containsKey(root)
						&& this.temporaryLatencys.get(i).get(root).isConfirmed()) {
					this.latencys.putAll(this.temporaryLatencys.get(i));
					this.temporaryLatencys.remove(i);
					calcQueryLatency();
					return;
				}
				else{
					this.temporaryLatencys.remove(i);
				}
			}
		}
	}

	/**
	 * Calculates the latency for this measurement and the average latency
	 */
	private synchronized void calcQueryLatency() {
		long latency = 0;
		for (IPhysicalOperator o : this.latencys.keySet()) {
			latency += this.latencys.get(o).getLatency();
			Measurement.LOG.info(o + " Latency: " + this.latencys.get(o).getLatency());
		}
		if (getCurrentLatency() != 0) {
			setAverageLatency((latency + getCurrentLatency()) / 2);
		}
		setCurrentLatency(latency);
	}

	/**
	 * Sets the Latency in QueryManager for this measurement 
	 * @param currentLatency
	 */
	private synchronized void setCurrentLatency(long currentLatency) {
		this.currentLatency = currentLatency;
		QueryManager.getInstance().setLatencyForSubQuery(this, currentLatency);
	}

	public synchronized long getCurrentLatency() {
		return this.currentLatency;
	}

	/**
	 * Returns the average latency for this measurement
	 * @return
	 */
	public synchronized long getAverageLatency() {
		return averageLatency;
	}

	public synchronized void setAverageLatency(long averageLatency) {
		this.averageLatency = averageLatency;
	}

}