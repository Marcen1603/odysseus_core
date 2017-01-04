package de.uniol.inf.is.odysseus.admission.status;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Measures the latency of all active queries.
 * 
 * @author Jannes
 *
 */
public class LatencyAdmissionMonitor implements IAdmissionMonitor {
	
	static private final int LATENCY_MEASUREMENT_SIZE = 5;
	static private final double TRESHOLD = 0.5;
	
	private HashMap<IPhysicalQuery, ArrayList<Integer>> latencies = new HashMap<IPhysicalQuery, ArrayList<Integer>>();
	
	@Override
	public void addQuery(IPhysicalQuery query) {
		if (!latencies.containsKey(query)) {
			latencies.put(query, new ArrayList<Integer>());
			//query.addMonitoringData(type, item);
		}
	}
	
	@Override
	public void removeQuery(IPhysicalQuery query) {
		if (latencies.containsKey(query)) {
			latencies.remove(query);
		}
	}
	
	@Override
	public void updateMeasurements() {
		if (latencies.isEmpty()) {
			return;
		}
		for (IPhysicalQuery query : latencies.keySet()) {
			ArrayList<Integer> list = latencies.get(query);
			//list.add();
			while (list.size() > LATENCY_MEASUREMENT_SIZE) {
				list.remove(0);
			}
		}
	}
	
	@Override
	public ArrayList<IPhysicalQuery> getQuerysWithIncreasingTendency() {
		ArrayList<IPhysicalQuery> increasingLatencies = new ArrayList<>();
		for (IPhysicalQuery query : latencies.keySet()) {
			int tendency = estimateTendency(latencies.get(query));
			if (tendency > TRESHOLD) {
				increasingLatencies.add(query);
			}
		}

		return increasingLatencies;
	}

	/**
	 * Returns the tendency for the given list as int.
	 * @param list
	 * @return
	 */
	private int estimateTendency(ArrayList<Integer> list) {
		int tendency = 0;
		for (int i = 0; i < (list.size() - 1); i++) {
			tendency = tendency + (list.get(i + 1) - list.get(i));
		}
		
		return tendency;
	}
}
