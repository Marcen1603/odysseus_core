package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.admission.status.impl.AdmissionSink;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Measures the latency of all added active queries.
 */
public class LatencyAdmissionMonitor implements IAdmissionMonitor {
	
	/**
	 * The size of the latency measurements is set here.
	 */
	private final int LATENCY_MEASUREMENT_SIZE = 50;
	
	/**
	 * Each query with a list of its latencies is saved here.
	 */
	private Map<IPhysicalQuery, List<Long>> latencies = new HashMap<IPhysicalQuery, List<Long>>();
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addQuery(IPhysicalQuery query) {
		if (!latencies.containsKey(query)) {
			latencies.put(query, new ArrayList<Long>());
			
			//A new AdmissionSink is created here.
			AdmissionSink<?> admissionSink = new AdmissionSink();
			admissionSink.setLatencyAdmissionMonitor(this);
			ISource source = null;
			
			//All last operators of the query plans are checked.
			for(IPhysicalOperator operator : query.getRoots()) {
				if(operator.isSource()) {
					//The last operator is a source so the AdmissionSink can directly be subscribed.
					source = (ISource) operator;
					break;
				} else if (operator.isSink()) {
					//The last operator is not a source so the AdmissionSink has to be added to the operator before this.
					source = getPreviousSource(operator, query); 
					if(source != null) {
						break;
					}
				}
			}
			if (source != null) {
				//Subscribe the AdmissionSink to the source and open it.
				source.connectSink(admissionSink, 0, 0, source.getOutputSchema());
			}
		}
	}
	
	@Override
	public void removeQuery(IPhysicalQuery query) {
		if (latencies.containsKey(query)) {
			latencies.remove(query);
		}
	}
	
	/**
	 * Adds the given latency measurement to the given query.
	 * @param query IPhysicalQuery
	 * @param latency long
	 */
	public void updateMeasurement(IPhysicalQuery query, long latency) {
		if (latencies.isEmpty()) {
			return;
		}
		List<Long> list = latencies.get(query);
		list.add(latency);
		while (list.size() > LATENCY_MEASUREMENT_SIZE) {
			list.remove(0);
		}
		latencies.replace(query, list);
	}
	
	@Override
	public List<IPhysicalQuery> getQueriesWithIncreasingTendency() {
		HashMap<IPhysicalQuery, Long> map = new HashMap<>();
		for (IPhysicalQuery query : latencies.keySet()) {
			long tendency = estimateTendency(latencies.get(query));
			if (tendency > 0) {
				map.put(query, tendency);
			}
		}
		return getSortedListByValues(map);
	}

	/**
	 * Returns the tendency for the given list as long.
	 * @param list
	 * @return tendency
	 */
	private long estimateTendency(List<Long> list) {
		long tendency = 0;
		for (int i = 0; i < (list.size() - 1); i++) {
			tendency = tendency + (list.get(i + 1) - list.get(i));
		}
		
		return tendency;
	}
	
	/**
	 * Returns an arrayList with the queryIDs sorted by their tendency in ascending order.
	 * @param map
	 * @return sorted List
	 */
	private List<IPhysicalQuery> getSortedListByValues(Map<IPhysicalQuery, Long> map) {
	    List<IPhysicalQuery> queries = new ArrayList<>(map.keySet());
	    List<Long> tendencies = new ArrayList<>(map.values());
	    Collections.sort(tendencies);

	    List<IPhysicalQuery> sortedList = new ArrayList<>();

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
	
	/**
	 * Returns a previous operator of the given operator from the given physical query. 
	 * @param operator IPhysicalOperator
	 * @param query IPhysicalQuery
	 * @return ISource
	 */
	@SuppressWarnings("rawtypes")
	private ISource getPreviousSource(IPhysicalOperator operator, IPhysicalQuery query) {
		for (IPhysicalOperator previous : query.getAllOperators()) {
			if (previous.isSource()) {
				// follow outgoing paths 
				ISource<?> source = (ISource<?>) previous;
				Collection<?> subscriptions = source.getSubscriptions();
				
				for (Object obj : subscriptions) {
					ISubscription subscription = (ISubscription) obj;
					IPhysicalOperator targetOperator = (IPhysicalOperator) subscription.getSink();
					if(targetOperator.equals(operator)) {
						return source;
					}
				}
			}
		}
		return null;
	}
}
