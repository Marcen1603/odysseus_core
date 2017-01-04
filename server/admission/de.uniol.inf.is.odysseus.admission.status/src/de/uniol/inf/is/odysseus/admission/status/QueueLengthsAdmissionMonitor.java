package de.uniol.inf.is.odysseus.admission.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Measures the queue lengths for each active query.
 * 
 * @author Jannes
 *
 */
public class QueueLengthsAdmissionMonitor implements IAdmissionMonitor {
	
	static private final int QUEUE_MEASUREMENT_SIZE = 5;
	static private final int TRESHOLD = 5;
	
	private HashMap<IPhysicalQuery, HashMap<ControllablePhysicalSubscription<ISink<?>>, ArrayList<Integer>>> queuelengths = new HashMap<IPhysicalQuery, HashMap<ControllablePhysicalSubscription<ISink<?>>, ArrayList<Integer>>>();
	
	@Override
	public void addQuery(IPhysicalQuery query) {
		if(!queuelengths.containsKey(query)) {
			getSubscriptionsForQuery(query);
		}
	}
	
	@Override
	public void removeQuery(IPhysicalQuery query) {
		if(queuelengths.containsKey(query)) {
			queuelengths.remove(query);
		}
	}
	
	@Override
	public void updateMeasurements() {
		if (queuelengths.isEmpty()) {
			return;
		}
		for (IPhysicalQuery query : queuelengths.keySet()) {
			for (ControllablePhysicalSubscription<ISink<?>> subscription : queuelengths.get(query).keySet()) {
				ArrayList<Integer> list = queuelengths.get(query).get(subscription);
				list.add(subscription.getBufferSize());
				while (list.size() > QUEUE_MEASUREMENT_SIZE) {
					list.remove(0);
				}
				queuelengths.get(query).replace(subscription, list);
			}
		}
	}
	
	@Override
	public ArrayList<IPhysicalQuery> getQuerysWithIncreasingTendency() {
		ArrayList<IPhysicalQuery> increasingLengths = new ArrayList<>();
		for (IPhysicalQuery query : queuelengths.keySet()) {
			for (ControllablePhysicalSubscription<ISink<?>> subscription : queuelengths.get(query).keySet()) {
				
				int tendency = estimateTendency(queuelengths.get(query).get(subscription));
				if (tendency > TRESHOLD) {
					if (!increasingLengths.contains(query)) {
						increasingLengths.add(query);
						break;
					}
				}
			}
		}
		
		return increasingLengths;
	}
	
	/**
	 * Adds a new map for the given query to queuelengths.
	 * @param query
	 */
	private void getSubscriptionsForQuery(IPhysicalQuery query) {
		HashMap<ControllablePhysicalSubscription<ISink<?>>, ArrayList<Integer>> map = new HashMap<ControllablePhysicalSubscription<ISink<?>>, ArrayList<Integer>>();
		queuelengths.put(query, map);
		for (IPhysicalOperator operator : query.getAllOperators()) {
			getOutgoingSubscriptions(operator, query);
		}
	}
	
	/**
	 * Adds all subscriptions from the given query to the map in queuelengths.
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
				ControllablePhysicalSubscription<ISink<?>> subscription = (ControllablePhysicalSubscription<ISink<?>>) obj;
				IPhysicalOperator target = subscription.getTarget();
				if (query.getAllOperators().contains(target)) {
					queuelengths.get(query).put(subscription, new ArrayList<Integer>());
				}
			}
		}
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
