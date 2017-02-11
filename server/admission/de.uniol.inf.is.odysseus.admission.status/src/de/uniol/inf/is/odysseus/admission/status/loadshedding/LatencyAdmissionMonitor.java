package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.impl.AdmissionSink;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
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
	
	private Map<IPhysicalQuery, List<Long>> latencies = new HashMap<IPhysicalQuery, List<Long>>();
	
	@Override
	public void addQuery(IPhysicalQuery query) {
		if (!latencies.containsKey(query)) {
			latencies.put(query, new ArrayList<Long>());
			AdmissionSink<?> admissionSink = new AdmissionSink();
			admissionSink.setLatencyAdmissionMonitor(this);
			ISource source = null;
			for(IPhysicalOperator operator : query.getRoots()) {
				if(operator.isSource()) {
					source = (ISource) operator;
					break;
				} else if (operator.isSink()) {
					source = getPreviousSource(operator, query); 
					if(source != null) {
						break;
					}
				}
			}
			if (source != null) {
				//subscribe und open
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
	
	public void updateMeasurement(IPhysicalQuery query, long latency) {
		//LoggerFactory.getLogger(this.getClass()).info("latency measurement on " + query.getID() + ". Latency : " + latency);
		if (latencies.isEmpty()) {
			return;
		}
		List<Long> list = latencies.get(query);
		list.add(latency);
		while (list.size() > LATENCY_MEASUREMENT_SIZE) {
			list.remove(0);
		}
	}
	
	@Override
	public List<IPhysicalQuery> getQuerysWithIncreasingTendency() {
		
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
	 * @return
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
	
	private ISource getPreviousSource(IPhysicalOperator operator, IPhysicalQuery query) {
		for (IPhysicalOperator previous : query.getAllOperators()) {
			if (previous.isSource()) {
				// follow outgoing paths 
				ISource<?> source = (ISource<?>) previous;
				Collection<?> subscriptions = source.getSubscriptions();
				
				for (Object obj : subscriptions) {
					@SuppressWarnings("unchecked")
					ISubscription subscription = (ISubscription) obj;
					IPhysicalOperator targetOperator = (IPhysicalOperator) subscription.getTarget();
					if(targetOperator.equals(operator)) {
						return source;
					}
				}
			}
		}
		return null;
	}
}
