package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.admission.status.impl.AdmissionSink;
import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Measures the latency of all active queries.
 * 
 * @author Jannes
 *
 */
public class LatencyAdmissionMonitor implements IAdmissionMonitor {
	
	static private final int LATENCY_MEASUREMENT_SIZE = 50;
	static private final long TRESHOLD = 100;
	
	private HashMap<IPhysicalQuery, ArrayList<Long>> latencies = new HashMap<IPhysicalQuery, ArrayList<Long>>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addQuery(IPhysicalQuery query) {
		if (!latencies.containsKey(query)) {
			latencies.put(query, new ArrayList<Long>());
			AdmissionSink<?> admissionSink = new AdmissionSink<>();
			IPhysicalOperator operator = query.getLeafSources().get(0);
			if(operator.isSink() && !operator.isSource()) {
				ISubscribable sink = (ISubscribable) operator;
				sink.connectSink(admissionSink, 0, 0, operator.getOutputSchema());
			}
		}
	}
	
	@Override
	public void removeQuery(IPhysicalQuery query) {
		if (latencies.containsKey(query)) {
			latencies.remove(query);
		}
	}
	
	public void updateMeasurement(IPhysicalQuery query, long latency) {
		if (latencies.isEmpty()) {
			return;
		}
		ArrayList<Long> list = latencies.get(query);
		list.add(latency);
		while (list.size() > LATENCY_MEASUREMENT_SIZE) {
			list.remove(0);
		}
	}
	
	@Override
	public ArrayList<IPhysicalQuery> getQuerysWithIncreasingTendency() {
		
		HashMap<IPhysicalQuery, Long> map = new HashMap<>();
		for (IPhysicalQuery query : latencies.keySet()) {
			long tendency = estimateTendency(latencies.get(query));
			if (tendency > TRESHOLD) {
				map.put(query, tendency);
			}
		}

		return getSortedListByValues(map);
	}

	/**
	 * Returns the tendency for the given list as int.
	 * @param list
	 * @return
	 */
	private long estimateTendency(ArrayList<Long> list) {
		long tendency = 0;
		for (int i = 0; i < (list.size() - 1); i++) {
			tendency = tendency + (list.get(i + 1) - list.get(i));
		}
		
		return tendency;
	}
	
	/**
	 * Returns an arrayList with the queryIDs sorted by their tendency in ascending order.
	 * @param map
	 * @return
	 */
	private ArrayList<IPhysicalQuery> getSortedListByValues(HashMap<IPhysicalQuery, Long> map) {
	    List<IPhysicalQuery> queries = new ArrayList<>(map.keySet());
	    List<Long> tendencies = new ArrayList<>(map.values());
	    Collections.sort(tendencies);

	    ArrayList<IPhysicalQuery> sortedList = new ArrayList<>();

	    Iterator<Long> tendencyIt = tendencies.iterator();
	    while (tendencyIt.hasNext()) {
	        long tendency = tendencyIt.next();
	        Iterator<IPhysicalQuery> queriesIt = queries.iterator();

	        while (queriesIt.hasNext()) {
	        	IPhysicalQuery query = queriesIt.next();
	            long comp1 = map.get(query);
	            long comp2 = tendency;

	            if (comp1 == comp2) {
	                queriesIt.remove();
	                sortedList.add(query);
	                break;
	            }
	        }
	    }
	    return sortedList;
	}
}
