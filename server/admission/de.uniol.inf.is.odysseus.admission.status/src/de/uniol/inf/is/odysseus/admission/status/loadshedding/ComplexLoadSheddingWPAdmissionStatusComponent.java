package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for complex load shedding in consideration of priorities.
 * 
 * The CPU load together which the latency and queue lengths of each query is used in this status component.
 * 
 * @author Jannes
 *
 */
public class ComplexLoadSheddingWPAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {

	private final String NAME = "complexWP";

	private volatile int actIncreasingPriority = -1;
	private volatile int actDecreasingPriority = -1;
	
	/**
	 * This attribute is used for the correct handling of priorities.
	 */
	private PriorityHandler priorityHandler = new PriorityHandler();
	
	private List<Integer> simpleActiveQueries = new ArrayList<>();
	
	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	private int actSheddingQueryUp = -1;
	private int actSheddingQueryDown = -1;
	
	private int actSheddingFactor = 0;
	
	@Override
	public boolean addQuery(int queryID) {
		if(super.addQuery(queryID)) {
			IPhysicalQuery query = getQueryByID(queryID);
			QUEUE_LENGTHS_MONITOR.addQuery(query);
			LATENCY_MONITOR.addQuery(query);
			
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
			IPhysicalQuery query = getQueryByID(queryID);
			QUEUE_LENGTHS_MONITOR.removeQuery(query);
			LATENCY_MONITOR.removeQuery(query);
			
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
		
		boolean simple = false;
		int partialQuery = getSheddingQueryID();
		
		int maxSheddingFactor = allowedQueries.get(partialQuery);
		int sheddingFactor;
		boolean first;
		if (activeQueries.containsKey(partialQuery)) {
			first = false;
			sheddingFactor = activeQueries.get(partialQuery) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(partialQuery);
			}
			activeQueries.replace(partialQuery, sheddingFactor);
		} else {
			first = true;
			sheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(partialQuery);
			}
			activeQueries.put(partialQuery, sheddingFactor);
		}
		if (simple) {
			if (first) {
				simpleActiveQueries.add(partialQuery);
			}
		} else {
			if (simpleActiveQueries.contains(partialQuery)) {
				simpleActiveQueries.remove(Integer.valueOf(partialQuery));
			}
		}
		setSheddingFactor(partialQuery, sheddingFactor);
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
		
		if (activeQueries.containsKey(partialQuery)) {
			if (maxSheddingQueries.contains(partialQuery)) {
				maxSheddingQueries.remove(Integer.valueOf(partialQuery));
			}
			int sheddingFactor = activeQueries.get(partialQuery) - LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor <= 0) {
				sheddingFactor = 0;
				activeQueries.remove(partialQuery);
				if (simpleActiveQueries.contains(partialQuery)) {
					simpleActiveQueries.remove(Integer.valueOf(partialQuery));
				}
			} else {
				activeQueries.replace(partialQuery, sheddingFactor);
			}
			setSheddingFactor(partialQuery, sheddingFactor);
		}

	}

	@Override
	public void measureStatus() {
		QUEUE_LENGTHS_MONITOR.updateMeasurements();
	}
	
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
	
	private int getSheddingQueryIDSeparately() {
		int partialQuery = -1;
		if (actSheddingQueryUp > -1 && !maxSheddingQueries.contains(actSheddingQueryUp)) {
			partialQuery = actSheddingQueryUp;
		} else {
			partialQuery = getSheddingQueryIDDefault();
			
			actSheddingQueryUp = partialQuery;
		}
		return partialQuery;
	}
	
	private int getSheddingQueryIDEqually() {
		int partialQuery = -1;
		List<Integer> priorityIDList = getIncreasingPriorityQueryListEqually();
		
		Map<Integer, Integer> queryRanks = getQueryRanks();
		Map<Integer, Integer> queryRanksWithRightPriority = new HashMap<>();
		
		for(int query : queryRanks.keySet()) {
			if (priorityIDList.contains(query)) {
				queryRanksWithRightPriority.put(query, queryRanks.get(query));
			}
		}
		
		partialQuery = getQueryWithGreatestRank(queryRanksWithRightPriority);
		
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
	
	private int getSheddingQueryIDDefault() {
		int partialQuery = -1;
		List<Integer> priorityIDList = getIncreasingPriorityQueryList();
		if (priorityIDList == null) {
			return partialQuery;
		}
		
		Map<Integer, Integer> queryRanks = getQueryRanks();
		Map<Integer, Integer> queryRanksWithRightPriority = new HashMap<>();
		
		for(int query : queryRanks.keySet()) {
			if (priorityIDList.contains(query)) {
				queryRanksWithRightPriority.put(query, queryRanks.get(query));
			}
		}
		
		partialQuery = getQueryWithGreatestRank(queryRanksWithRightPriority);
		
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
	
	private int getQueryIDToRollbackDefault() {
		int partialQuery = -1;
		List<Integer> priorityList = getDecreasingPriorityQueryIDList();
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
	
	private int getQueryIDToRollbackSeparately() {
		int partialQuery = -1;
		if (actSheddingQueryDown > -1 && activeQueries.containsKey(actSheddingQueryDown)) {
			partialQuery = actSheddingQueryDown;
		} else {
			partialQuery = getQueryIDToRollbackDefault();
			actSheddingQueryDown = partialQuery;
		}
		return partialQuery;
	}
	
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
	
	private Map<Integer, Integer> getQueryRanks() {
		
		List<IPhysicalQuery> queuelengths = removeQuerysWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQuerysWithIncreasingTendency());
		
		List<IPhysicalQuery> latencies = removeQuerysWithMaxSheddingFactor
				(LATENCY_MONITOR.getQuerysWithIncreasingTendency());
				
		Map<Integer, Integer> queryRanks = new HashMap<>();
		
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
	
	private int getQueryWithGreatestRank(Map<Integer, Integer> queryRanks) {
		if (queryRanks.isEmpty()) {
			return -1;
		}
		
		int partialQuery = -1;
		int rank = -1;
		for (int queryID : queryRanks.keySet()) {
			if(queryRanks.get(queryID) > rank) {
				partialQuery = queryID;
				rank = queryRanks.get(queryID);
			}
		}
		
		return partialQuery;
	}
	
	/**
	 * Recursive method to find a query with minimal priority, which has not its maximal shedding factor. 
	 * @return
	 */
	private List<Integer> getIncreasingPriorityQueryList() {
		if (actIncreasingPriority < 0) {
			return null;
		}
		
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityHandler.getQueriesByPriority(actIncreasingPriority)) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			return list;
		} else {
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
	
	private List<Integer> getIncreasingPriorityQueryListEqually() {
		if (actIncreasingPriority < 0) {
			return null;
		}
		
		if (actSheddingFactor <= 0) {
			actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		while (actSheddingFactor <= 100) {
			List<Integer> list = new ArrayList<>();
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
	 * Recursive method to find the active query with the maximal priority.
	 * @param actPriority
	 * @return
	 */
	private List<Integer> getDecreasingPriorityQueryIDList() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return null;
		}
		
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
			if (activeQueries.containsKey(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			return list;
		} else {
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
	 * @param actPriority
	 * @return
	 */
	private List<Integer> getDecreasingPriorityQueryIDListEqually() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return null;
		}
		
		while (actSheddingFactor >= 0) {
			List<Integer> list = new ArrayList<>();
			for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
				if (activeQueries.containsKey(queryID) && activeQueries.get(queryID) >= actSheddingFactor) {
					list.add(Integer.valueOf(queryID));
				}
			}
			if (!list.isEmpty()) {
				return list;
			} else {
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
	
	private List<IPhysicalQuery> removeQuerysWithMaxSheddingFactor(List<IPhysicalQuery> list) {
		Iterator<IPhysicalQuery> iter = list.iterator();

		while (iter.hasNext()) {
			IPhysicalQuery query = iter.next();
			if (maxSheddingQueries.contains(query.getID())) {
				iter.remove();
			}
		}
		return list;
	}
	
	private void addToSimpleQueries(int queryID) {
		if (!activeQueries.containsKey(queryID)) {
			simpleActiveQueries.add(Integer.valueOf(queryID));
		}
	}
	
	private void removeFromSimpleQueries(int queryID) {
		if (simpleActiveQueries.contains(queryID)) {
			simpleActiveQueries.remove(Integer.valueOf(queryID));
		}
	}

	@Override
	public String getComponentName() {
		return NAME;
	}
}
