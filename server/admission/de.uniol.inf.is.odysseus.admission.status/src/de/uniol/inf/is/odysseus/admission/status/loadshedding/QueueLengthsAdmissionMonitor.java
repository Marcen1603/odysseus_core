package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Measures the queue lengths of all added active queries.
 */
public class QueueLengthsAdmissionMonitor implements IAdmissionMonitor {
	
	/**
	 * The size of the queue length measurements is set here.
	 */
	private final int QUEUE_MEASUREMENT_SIZE = 10;
	
	/**
	 * This map stores all subscriptions with buffers to their queries.
	 * A list with measurements is assigned to each subscription. 
	 */
	private Map<IPhysicalQuery, Map<ControllablePhysicalSubscription<?,ISink<IStreamObject<?>>>, List<Integer>>> queuelengthsSubscriptions
		= new HashMap<IPhysicalQuery, Map<ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>, List<Integer>>>();
	
	/**
	 * This map stores all BufferPOs to their queries.
	 * A list with measurements is assigned to each buffer. 
	 */
	private Map<IPhysicalQuery, Map<BufferPO<?>, List<Integer>>> queuelengthsBufferPOs
		= new HashMap<IPhysicalQuery, Map<BufferPO<?>, List<Integer>>>();

	
	@Override
	public void addQuery(IPhysicalQuery query) {
		if(!queuelengthsSubscriptions.containsKey(query)) {
			getSubscriptionsAndBuffersForQuery(query);
		}
	}
	
	@Override
	public void removeQuery(IPhysicalQuery query) {
		if(queuelengthsSubscriptions.containsKey(query)) {
			queuelengthsSubscriptions.remove(query);
		}
		if(queuelengthsBufferPOs.containsKey(query)) {
			queuelengthsBufferPOs.remove(query);
		}
	}
	
	/**
	 * Updates the measurements of the queue lengths for each query. 
	 */
	public void updateMeasurements() {
		if (queuelengthsSubscriptions.isEmpty()) {
			return;
		}
		for (IPhysicalQuery query : queuelengthsSubscriptions.keySet()) {
			for (ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>> subscription : queuelengthsSubscriptions.get(query).keySet()) {
				List<Integer> list = queuelengthsSubscriptions.get(query).get(subscription);
				list.add(subscription.getBufferSize());
				while (list.size() > QUEUE_MEASUREMENT_SIZE) {
					list.remove(0);
				}
				queuelengthsSubscriptions.get(query).replace(subscription, list);
			}
		}
		
		for (IPhysicalQuery query : queuelengthsBufferPOs.keySet()) {
			for (BufferPO<?> buffer : queuelengthsBufferPOs.get(query).keySet()) {
				List<Integer> list = queuelengthsBufferPOs.get(query).get(buffer);
				list.add(buffer.size());
				while (list.size() > QUEUE_MEASUREMENT_SIZE) {
					list.remove(0);
				}
				queuelengthsBufferPOs.get(query).replace(buffer, list);
			}
		}
	}
	
	@Override
	public List<IPhysicalQuery> getQueriesWithIncreasingTendency() {

		Map<IPhysicalQuery, Integer> map = new HashMap<>();
		for (IPhysicalQuery query : queuelengthsSubscriptions.keySet()) {
			for (ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>> subscription : queuelengthsSubscriptions.get(query).keySet()) {
				
				int tendency = estimateTendency(queuelengthsSubscriptions.get(query).get(subscription));

				if (tendency > 0) {
					if (!map.containsKey(query)) {
						map.put(query, tendency);
					} else {
						if (map.get(query) < tendency) {
							map.replace(query, tendency);
						}
					}
				}
			}
			for (BufferPO<?> buffer : queuelengthsBufferPOs.get(query).keySet()) {
				int tendency = estimateTendency(queuelengthsBufferPOs.get(query).get(buffer));

				if (tendency > 0) {
					if (!map.containsKey(query)) {
						map.put(query, tendency);
					} else {
						if (map.get(query) < tendency) {
							map.replace(query, tendency);
						}
					}
				}
			}
		}
		
		return getSortedListByValues(map);
	}
	
	/**
	 * Adds a new maps for the subscriptions and buffers of given query.
	 * @param query
	 */
	private void getSubscriptionsAndBuffersForQuery(IPhysicalQuery query) {
		Map<ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>, List<Integer>> subMap = new HashMap<ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>, List<Integer>>();
		Map<BufferPO<?>, List<Integer>> bufferMap = new HashMap<>();
		
		queuelengthsSubscriptions.put(query, subMap);
		queuelengthsBufferPOs.put(query, bufferMap);
		for (IPhysicalOperator operator : query.getAllOperators()) {
			if (operator instanceof BufferPO) {
				queuelengthsBufferPOs.get(query).put((BufferPO<?>) operator, new ArrayList<Integer>());
			}
			getOutgoingSubscriptions(operator, query);
		}
	}
	
	/**
	 * Adds all subscriptions from the given query to the subscription map in queue lengths.
	 * @param operator
	 * @param query
	 */
	private void getOutgoingSubscriptions(IPhysicalOperator operator, IPhysicalQuery query) {
		if (operator.isSource()) {
			// follow outgoing paths 
			ISource<?> source = (ISource<?>) operator;
			Collection<?> subscriptions = source.getSubscriptions();
			
			for (Object obj : subscriptions) {
				@SuppressWarnings("unchecked")
				ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>> subscription = (ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>) obj;
				IPhysicalOperator target = subscription.getSink();
				if (query.getAllOperators().contains(target)) {
					queuelengthsSubscriptions.get(query).put(subscription, new ArrayList<Integer>());
				}
			}
		}
	}
	
	/**
	 * Returns the tendency for the given list as Integer.
	 * @param list
	 * @return tendency
	 */
	private int estimateTendency(List<Integer> list) {
		int tendency = 0;
		for (int i = 0; i < (list.size() - 1); i++) {
			tendency = tendency + (list.get(i + 1) - list.get(i));
		}
		return tendency;
	}
	
	/**
	 * Returns a list with the queries sorted by their tendency in ascending order.
	 * @param passedMap
	 * @return sorted List
	 */
	private List<IPhysicalQuery> getSortedListByValues(Map<IPhysicalQuery, Integer> passedMap) {
	    List<IPhysicalQuery> queries = new ArrayList<>(passedMap.keySet());
	    List<Integer> tendencies = new ArrayList<>(passedMap.values());
	    Collections.sort(tendencies);

	    List<IPhysicalQuery> sortedList = new ArrayList<>();

	    Iterator<Integer> tendencyIt = tendencies.iterator();
	    while (tendencyIt.hasNext()) {
	        int tendency = tendencyIt.next();
	        Iterator<IPhysicalQuery> queriesIt = queries.iterator();

	        while (queriesIt.hasNext()) {
	        	IPhysicalQuery query = queriesIt.next();
	            int comp1 = passedMap.get(query);
	            int comp2 = tendency;

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
