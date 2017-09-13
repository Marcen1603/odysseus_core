package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class Measurement {

	private Map<String, OperatorLatency> latencys;
	private long currentLatency;
	private Map<String, OperatorLatency> temporaryLatencys;
	private List<String> roots;

	public Measurement(IPhysicalQuery query) {
		this.temporaryLatencys = new HashMap<String, OperatorLatency>();
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
			OperatorLatency l = new OperatorLatency();
			l.startMeasurement(nanoTimestamp);
			this.temporaryLatencys.put(operatorName, l);
	}

	/**
	 * Calculates the latency for one operator if the PushInitEvent occurs.
	 * 
	 * @param operatorName
	 *            Name of the Operator.
	 * @param nanoTimestamp
	 *            Timestamp of the PushInitEvent in ns.
	 */
	public synchronized void calcTemporaryOperatorLatency(String operatorName, long nanoTimestamp) {
		for (String tempLatency : this.temporaryLatencys.keySet()) {
			if (tempLatency.equals(operatorName)) {
				OperatorLatency l = this.temporaryLatencys.get(tempLatency);
				l.stopMeasurement(nanoTimestamp);
				this.temporaryLatencys.put(operatorName, l);

				if (roots.contains(tempLatency)) {
					this.latencys.putAll(this.temporaryLatencys);
					this.temporaryLatencys.clear();
					calcQueryLatency();
					System.out.println("Total Latency: " + this.currentLatency);
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
			System.out.println(o + " Latenz: " + latency);
		}
		setCurrentLatency(latency);
	}

	private synchronized void setCurrentLatency(long currentLatency) {
		this.currentLatency = currentLatency;
	}

	public synchronized long getCurrentLatency() {
		return this.currentLatency;
	}

}
