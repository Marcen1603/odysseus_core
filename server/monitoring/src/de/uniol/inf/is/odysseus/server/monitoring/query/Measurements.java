package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.OperatorLatency;

public class Measurements {

	private Map<String, OperatorLatency> latencys;
	private long currentLatency;
	private Map<String, OperatorLatency> temporaryLatencys;
	private List<String> roots;

	public Measurements(IPhysicalQuery query) {
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
		temporaryLatencys.put(operatorName, l);
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
		for (String tempLatency : temporaryLatencys.keySet()) {
			if (tempLatency.equals(operatorName)) {
				OperatorLatency l = temporaryLatencys.get(tempLatency);
				l.stopMeasurement(nanoTimestamp);
				temporaryLatencys.put(operatorName, l);
				// TODO: Remove

				if (roots.contains(tempLatency)) {
					latencys.putAll(temporaryLatencys);
					temporaryLatencys.clear();
					calcQueryLatency();
					System.out.println("Total Latency: " + currentLatency);
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
	// public synchronized void commitTemporaryLatency(String operatorName) {
	// // Wait wenn Start oder Endzeit fehlt
	// for (int i = 0; i <= temporaryLatencys.size(); i++) {
	// if (temporaryLatencys.get(i) != null &&
	// temporaryLatencys.get(i).containsKey(operatorName)) {
	// if (!temporaryLatencys.get(i).get(operatorName).isCalculated()) {
	// try {
	// wait();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// if (roots.contains(operatorName)) {
	// latencys.putAll(temporaryLatencys.get(i));
	// temporaryLatencys.remove(i);
	// calcQueryLatency();
	// System.out.println("Total Latency: " + currentLatency);
	//
	// i = temporaryLatencys.size() + 1;
	// } else {
	// temporaryLatencys.remove(i);
	// i = temporaryLatencys.size() + 1;
	// System.out.println("Temps deleted");
	// }
	// }
	// }
	// }

	/**
	 * Calculates the Latency in latencys
	 */
	private synchronized void calcQueryLatency() {
		long latency = 0;
		for (String o : latencys.keySet()) {
			latency += latencys.get(o).getLatency();
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
