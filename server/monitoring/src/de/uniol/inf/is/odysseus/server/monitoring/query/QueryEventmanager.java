package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class QueryEventmanager implements IPOEventListener {

	private long currentLatency;
	private IPhysicalQuery query;
	private List<String> roots = new ArrayList<String>();
	IPhysicalQuery subscribedTarget;
	private NavigableMap<String, OperatorLatency> latencys;
	private NavigableMap<Integer, NavigableMap<String, OperatorLatency>> temporaryLatencys;

	public QueryEventmanager(IPhysicalQuery q) {
		temporaryLatencys = new TreeMap<Integer, NavigableMap<String, OperatorLatency>>();
		latencys = new TreeMap<String, OperatorLatency>();
		query = q;
		for (IPhysicalOperator o : query.getPhysicalChilds()) {
			o.subscribe(this, POEventType.ProcessInit);
			o.subscribe(this, POEventType.PushInit);
			o.subscribe(this, POEventType.ProcessDone);
		}
		for (IPhysicalOperator o : query.getRoots()) {
			roots.add(o.toString());
		}
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		System.out.println("Event fired: " + event.toString() + "Zeit: " + nanoTimestamp);
		IPhysicalOperator o = (IPhysicalOperator) event.getSender();
		if (event.getEventType().equals(POEventType.ProcessInit)) {
			createNewTempLatency(o.toString(), nanoTimestamp);
		}
		if (event.getEventType().equals(POEventType.PushInit)) {
			calcTemporaryOperatorLatency(o.toString(), nanoTimestamp);
		}
		if (event.getEventType().equals(POEventType.ProcessDone)) {
			calcTemporaryOperatorLatency(o.toString(), nanoTimestamp);
			commitTempElement(o.toString());
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
	private void createNewTempLatency(String operatorName, long nanoTimestamp) {
		synchronized (temporaryLatencys) {
			if (temporaryLatencys.size() != 0) {
				for (int i = 1; i <= temporaryLatencys.size(); i++) {
					if (temporaryLatencys.get(i) != null && temporaryLatencys.get(i).containsKey(operatorName)) {
						continue;
					} else {
						OperatorLatency l = new OperatorLatency();
						l.startMeasurement(nanoTimestamp);
						NavigableMap<String, OperatorLatency> temp = new TreeMap<String, OperatorLatency>();
						temp.put(operatorName, l);
						temporaryLatencys.put(i, temp);
						
						i= temporaryLatencys.size()+1;
					}
				}
			} else {
				OperatorLatency l = new OperatorLatency();
				l.startMeasurement(nanoTimestamp);
				NavigableMap<String, OperatorLatency> temp = new TreeMap<String, OperatorLatency>();
				temp.put(operatorName, l);
				temporaryLatencys.put(0, temp);
			}
		}
	}

	/**
	 * Calculates the latency for one operator if the PushInitEvent occurs.
	 * 
	 * @param operatorName
	 *            Name of the Operator.
	 * @param nanoTimestamp
	 *            Timestamp of the PushInitEvent in ns.
	 */
	private void calcTemporaryOperatorLatency(String operatorName, long nanoTimestamp) {
		synchronized (temporaryLatencys) {
			for (int i = 0; i <= temporaryLatencys.size(); i++) {
				if (temporaryLatencys.get(i) != null && temporaryLatencys.get(i).containsKey(operatorName)) {
					OperatorLatency l = temporaryLatencys.get(i).get(operatorName);
					if (l.isCalculated()) {
						continue;
					} else {
						l.stopMeasurement(nanoTimestamp);
						temporaryLatencys.get(i).put(operatorName, l);
						i = temporaryLatencys.size()+1;
					}
				}
			}
		}
	}

	/**
	 * Runs through the temporaryLatencys on ProcessDoneEvent to look which
	 * Latency can be calculated. Copys the temporaryLatency to the
	 * currentLatencys and calculates it.
	 * 
	 * @param operatorName
	 *            Name of the Operator.
	 */
	private void commitTempElement(String operatorName) {
		synchronized (temporaryLatencys) {
			for (int i = 0; i <= temporaryLatencys.size(); i++) {
				if (temporaryLatencys.get(i) != null && temporaryLatencys.get(i).containsKey(operatorName)) {
					if (roots.contains(operatorName)) {
						latencys.putAll(temporaryLatencys.get(i));
						temporaryLatencys.remove(i);
						calcQueryLatency();
						System.out.println("Total Latency: " + currentLatency);

						i = temporaryLatencys.size()+1;
					} else {
						temporaryLatencys.remove(i);
						i = temporaryLatencys.size()+1;
					}
				}
			}
		}
	}

	/**
	 * Calculates the Latency in latencys
	 */
	private void calcQueryLatency() {
		long latency = 0;
		for (String o : latencys.keySet()) {
			latency += latencys.get(o).getLatency();
		}
		setCurrentLatency(latency);
	}

	public long getCurrentLatency() {
		return currentLatency;
	}

	private void setCurrentLatency(long currentLatency) {
		this.currentLatency = currentLatency;
	}
}
