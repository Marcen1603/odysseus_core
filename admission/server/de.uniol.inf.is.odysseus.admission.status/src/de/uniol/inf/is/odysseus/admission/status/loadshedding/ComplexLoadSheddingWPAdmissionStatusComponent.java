package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for complex load shedding in consideration of priorities.
 * 
 * The CPU load together which the latency and queue lengths of each query is used in this status component.
 */
public class ComplexLoadSheddingWPAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {

	/**
	 * The name of this status component.
	 */
	private final String NAME = "complexWP";

	//These variables save the actual priority.
	private volatile int actIncreasingPriority = -1;
	private volatile int actDecreasingPriority = -1;
	
	/**
	 * The priorityHandler is used for the correct handling of priorities.
	 */
	private PriorityHandler priorityHandler = new PriorityHandler();
	
	//The admission monitors.
	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	//These variables are used for the saparately strategy.
	private volatile int actSheddingQueryUp = -1;
	private volatile int actSheddingQueryDown = -1;
	
	//This is used in the equally strategy.
	private volatile int actSheddingFactor = 0;
	
	@Override
	public boolean addQuery(int queryID) {
		if(super.addQuery(queryID)) {
			//The query is added to the admission monitors.
			IPhysicalQuery query = getQueryByID(queryID);
			QUEUE_LENGTHS_MONITOR.addQuery(query);
			LATENCY_MONITOR.addQuery(query);
			
			//The query is added to the priority handling.
			priorityHandler.addQuery(queryID);
			actIncreasingPriority = priorityHandler.getSmallestPriority();
			actDecreasingPriority = priorityHandler.getGreatestPriority();
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeQuery(int queryID) {
		if (super.removeQuery(queryID)) {
			//The query is removed from the admission monitors.
			IPhysicalQuery query = getQueryByID(queryID);
			QUEUE_LENGTHS_MONITOR.removeQuery(query);
			LATENCY_MONITOR.removeQuery(query);
			
			//The query is removed from the priority handling.
			priorityHandler.removeQuery(queryID);
			actIncreasingPriority = priorityHandler.getSmallestPriority();
			actDecreasingPriority = priorityHandler.getGreatestPriority();
			
			if (actSheddingQueryDown == queryID) {
				actSheddingQueryDown = -1;
			}
			if (actSheddingQueryUp == queryID) {
				actSheddingQueryUp = -1;
			}
			
			return true;
		}
		return false;
	}

	@Override
	public void runLoadShedding() {
		if (!isSheddingPossible()) {
			return;
		}
		int queryID = getSheddingQueryID();
		if (queryID < 0) {
			return;
		}
		increaseFactor(queryID);
	}

	@Override
	public void rollbackLoadShedding() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return;
		}
		int partialQuery = getQueryIDToRollback();
		if (partialQuery < 0) {
			return;
		}
		decreaseFactor(partialQuery);

	}

	@Override
	public void measureStatus() {
		//The queue lengths have to update their status.
		QUEUE_LENGTHS_MONITOR.updateMeasurements();
	}
	
	/**
	 * Returns a query, which should increase its shedding factor, depending on the selected load shedding strategy.
	 * @return queryID
	 */
	private int getSheddingQueryID() {
		if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.DEFAULT) {
			return getSheddingQueryIDDefault();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			return getSheddingQueryIDEqually();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.SEPARATELY) {
			return getSheddingQueryIDSeparately();
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns a query, which should decrease its shedding factor, depending on the selected load shedding strategy.
	 * @return queryID
	 */
	private int getQueryIDToRollback() {
		if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.DEFAULT) {
			return getQueryIDToRollbackDefault();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			return getQueryIDToRollbackEqually();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.SEPARATELY) {
			return getQueryIDToRollbackSeparately();
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns the next query with the separately strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDSeparately() {
		int partialQuery = -1;
		if (actSheddingQueryUp > -1 && !maxSheddingQueries.contains(actSheddingQueryUp)) {
			//The shedding factor of the chosen query can still be increased.
			partialQuery = actSheddingQueryUp;
		} else {
			partialQuery = getSheddingQueryIDDefault();
			actSheddingQueryUp = partialQuery;
		}
		return partialQuery;
	}
	
	/**
	 * Returns the next query with the equally strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDEqually() {
		int partialQuery = -1;
		List<Integer> priorityIDList = getIncreasingPriorityQueryListEqually();
		
		Map<Integer, Integer> queryRanks = getQueryRanks();
		
		//All queries with the right priority and increasing tendencies are saved in this map.
		Map<Integer, Integer> queryRanksWithRightPriority = new HashMap<>();
		
		for(int query : queryRanks.keySet()) {
			if (priorityIDList.contains(query)) {
				queryRanksWithRightPriority.put(query, queryRanks.get(query));
			}
		}
		
		partialQuery = getQueryWithGreatestRank(queryRanksWithRightPriority);
		
		//No query with increasing tendencies was found.
		if (partialQuery < 0) {
			Collections.shuffle(priorityIDList);
			partialQuery = priorityIDList.get(0);
			if (partialQuery > 0) {
				addToSimpleQueries(partialQuery);
			}
		} else {
			removeFromSimpleQueries(partialQuery);
		}
		return partialQuery;
	}
	
	/**
	 * Returns the next query with the default strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDDefault() {
		int partialQuery = -1;
		List<Integer> priorityIDList = getIncreasingPriorityQueryList();
		if (priorityIDList == null) {
			return partialQuery;
		}
		
		Map<Integer, Integer> queryRanks = getQueryRanks();
		
		//All queries with the right priority and increasing tendencies are saved in this map.
		Map<Integer, Integer> queryRanksWithRightPriority = new HashMap<>();
		
		for(int query : queryRanks.keySet()) {
			if (priorityIDList.contains(query)) {
				queryRanksWithRightPriority.put(query, queryRanks.get(query));
			}
		}
		
		partialQuery = getQueryWithGreatestRank(queryRanksWithRightPriority);
		
		//No query with increasing tendencies was found.
		if (partialQuery < 0) {
			Collections.shuffle(priorityIDList);
			partialQuery = priorityIDList.get(0);
			if (partialQuery > 0) {
				addToSimpleQueries(partialQuery);
			}
		} else {
			removeFromSimpleQueries(partialQuery);
		}
		return partialQuery;
	}
	
	/**
	 * Returns the query to roll back with the default strategy.
	 * @return queryID
	 */
	private int getQueryIDToRollbackDefault() {
		int partialQuery = -1;
		List<Integer> priorityList = getDecreasingPriorityQueryIDList();
		if (priorityList == null) {
			return partialQuery;
		}
		
		//All queries, which were activated by simple load shedding are saved in this list.
		List<Integer> simpleList = new ArrayList<>();
		for (int queryID : priorityList) {
			if (activeQueries.containsKey(queryID) && simpleList.contains(queryID)) {
				simpleList.add(queryID);
			}
		}
		if (!simpleList.isEmpty()) {
			Collections.shuffle(simpleList);
			partialQuery = simpleList.get(0);
		} else {
			Collections.shuffle(priorityList);
			partialQuery = priorityList.get(0);
		}
		return partialQuery;
	}
	
	/**
	 * Returns the query to roll back with the separately strategy.
	 * @return queryID
	 */
	private int getQueryIDToRollbackSeparately() {
		if (actSheddingQueryDown > -1 && activeQueries.containsKey(actSheddingQueryDown)) {
			//The actual selected query has still active load shedding.
			return actSheddingQueryDown;
		} else {
			actSheddingQueryDown = getQueryIDToRollbackDefault();
			return actSheddingQueryDown;
		}
	}
	
	/**
	 * Returns the query to roll back with the equally strategy.
	 * @return queryID
	 */
	private int getQueryIDToRollbackEqually() {
		int partialQuery = -1;
		List<Integer> priorityList = getDecreasingPriorityQueryIDListEqually();
		if (priorityList == null) {
			return partialQuery;
		}
		List<Integer> simpleList = new ArrayList<>();
		for (int queryID : priorityList) {
			if (activeQueries.containsKey(queryID) && simpleList.contains(queryID)) {
				simpleList.add(queryID);
			}
		}
		if (!simpleList.isEmpty()) {
			Collections.shuffle(simpleList);
			partialQuery = simpleList.get(0);
		} else {
			Collections.shuffle(priorityList);
			partialQuery = priorityList.get(0);
		}
		return partialQuery;
	}
	
	/**
	 * Returns a map with all queries, which have increasing tendencies.
	 * @return queryRanks
	 */
	private Map<Integer, Integer> getQueryRanks() {
		
		List<IPhysicalQuery> queuelengths = removeQueriesWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQueriesWithIncreasingTendency());
		
		List<IPhysicalQuery> latencies = removeQueriesWithMaxSheddingFactor
				(LATENCY_MONITOR.getQueriesWithIncreasingTendency());
				
		Map<Integer, Integer> queryRanks = new HashMap<>();
		
		//The rank is calculated here.
		for(int q = 0; q < queuelengths.size(); q++) {
			queryRanks.put(queuelengths.get(q).getID(), q);
		}
		for(int l = 0; l < latencies.size(); l++) {
			if (queryRanks.containsKey(latencies.get(l).getID())) {
				int q = queryRanks.get(latencies.get(l).getID());
				queryRanks.replace(latencies.get(l).getID(), q + l);
			} else {
				queryRanks.put(latencies.get(l).getID(), l);
			}
		}
		
		return queryRanks;
	}
	
	/**
	 * Recursive method to find queries with minimal priority, which have not their maximal shedding factor. 
	 * @return List with queries
	 */
	private List<Integer> getIncreasingPriorityQueryList() {
		if (actIncreasingPriority < 0) {
			return null;
		}
		
		List<Integer> list = new ArrayList<>();
		//All queries with the actual priority are added to the list, if they have not reached
		//their maximal shedding factor.
		for (int queryID : priorityHandler.getQueriesByPriority(actIncreasingPriority)) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			return list;
		} else {
			//The next greater priority has to be checked.
			int nextPrio = priorityHandler.getNextPriority(actIncreasingPriority);
			if (nextPrio < 0) {
				return null;
			}
			actIncreasingPriority = nextPrio;
			if (actIncreasingPriority > actDecreasingPriority) {
				actDecreasingPriority = actIncreasingPriority;
			}
			return getIncreasingPriorityQueryList();
		}
	}
	
	/**
	 * Recursive method to find queries with minimal priority, which have not their maximal shedding factor.
	 * This method is used in the equally strategy.
	 * @return List with queries
	 */
	private List<Integer> getIncreasingPriorityQueryListEqually() {
		if (actIncreasingPriority < 0) {
			return null;
		}
		
		if (actSheddingFactor <= 0) {
			actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		while (actSheddingFactor <= 100) {
			List<Integer> list = new ArrayList<>();
			//All queries with the actual priority are added to the list, if they have not reached
			//their maximal shedding factor and their shedding factor is smaller than the actual shedding factor.
			for (int queryID : priorityHandler.getQueriesByPriority(actIncreasingPriority)) {
				if (!maxSheddingQueries.contains(queryID)) {
					if (activeQueries.containsKey(queryID)) {
						if (activeQueries.get(queryID) < actSheddingFactor) {
							list.add(Integer.valueOf(queryID));
						}
					} else {
						list.add(Integer.valueOf(queryID));
					}
				}
			}
			if (!list.isEmpty()) {
				return list;
			} else {
				//No query with this priority has a smaller shedding factor than the actual shedding factor.
				if (actSheddingFactor < 100) {
					actSheddingFactor += LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
					if (actSheddingFactor > 100) {
						actSheddingFactor = 100;
					}
				} else {
					break;
				}
			}
		}
		//No query in the actual priority can increase its shedding factor so the next priority has to be checked.
		actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		int nextPrio = priorityHandler.getNextPriority(actIncreasingPriority);
		if (nextPrio < 0) {
			return null;
		}
		actIncreasingPriority = nextPrio;
		if (actIncreasingPriority > actDecreasingPriority) {
			actDecreasingPriority = actIncreasingPriority;
		}
		return getIncreasingPriorityQueryListEqually();
	}
	
	/**
	 * Recursive method to find active queries with the maximal priority.
	 * @return List with queries
	 */
	private List<Integer> getDecreasingPriorityQueryIDList() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return null;
		}
		
		//All queries with the right priority and with active load shedding are saved in this list.
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
			if (activeQueries.containsKey(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			return list;
		} else {
			//No query was found so the previous priority has to be checked.
			int previousPrio = priorityHandler.getPreviousPriority(actDecreasingPriority);
			if (previousPrio < 0) {
				return null;
			}
			actDecreasingPriority = previousPrio;
			if (actDecreasingPriority < actIncreasingPriority) {
				actIncreasingPriority = actDecreasingPriority;
			}
			return getDecreasingPriorityQueryIDList();
		}
	}
	
	/**
	 * Recursive method to find the active query with the maximal priority.
	 * @return List with queries
	 */
	private List<Integer> getDecreasingPriorityQueryIDListEqually() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return null;
		}
		
		while (actSheddingFactor >= 0) {
			//All queries with active load shedding, the right priority and a greater shedding factor
			//than the actual shedding factor is saved in this list.
			List<Integer> list = new ArrayList<>();
			for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
				if (activeQueries.containsKey(queryID) && activeQueries.get(queryID) >= actSheddingFactor) {
					list.add(Integer.valueOf(queryID));
				}
			}
			if (!list.isEmpty()) {
				return list;
			} else {
				//No query was found so the actual shedding factor has to be smaller.
				if (actSheddingFactor > 0) {
					actSheddingFactor -= LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
					if (actSheddingFactor < 0) {
						actSheddingFactor = 0;
					}
				} else {
					break;
				}
			}
		} 
		//No query with the actual priority has active load shedding so we check the previous priority.
		int previousPrio = priorityHandler.getPreviousPriority(actDecreasingPriority);
		if (previousPrio < 0) {
			return null;
		}
		actSheddingFactor = 100;
		actDecreasingPriority = previousPrio;
		if (actDecreasingPriority < actIncreasingPriority) {
			actIncreasingPriority = actDecreasingPriority;
		}
		return getDecreasingPriorityQueryIDListEqually();
	}

	@Override
	public String getComponentName() {
		return NAME;
	}
}
