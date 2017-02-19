package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for complex load shedding.
 * 
 * The CPU load together which the latency and queue lengths of each query is used in this status component.
 * 
 * @author Jannes
 *
 */
public class ComplexLoadSheddingAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {
	
	private final String NAME = "complex";
	
	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	private List<Integer> simpleActiveQueries = new ArrayList<>();
	
	private int actSheddingQueryUp = -1;
	private int actSheddingQueryDown = -1;
	
	private int actComplexFactor = 0;
	private int actSimpleFactor = 0;
	
	@Override
	public boolean addQuery(int queryID) {
		if(super.addQuery(queryID)) {
			IPhysicalQuery query = getQueryByID(queryID);
			QUEUE_LENGTHS_MONITOR.addQuery(query);
			LATENCY_MONITOR.addQuery(query);
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
			
			if (simpleActiveQueries.contains(queryID)) {
				simpleActiveQueries.remove(Integer.valueOf(queryID));
			}
			
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
		if(queryID < 0) {
			return;
		}
		
		int maxSheddingFactor = allowedQueries.get(queryID);
		int sheddingFactor;
		if (activeQueries.containsKey(queryID)) {
			sheddingFactor = activeQueries.get(queryID) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(queryID);
			}
			activeQueries.replace(queryID, sheddingFactor);
		} else {
			sheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(queryID);
			}
			activeQueries.put(queryID, sheddingFactor);
		}
		
		setSheddingFactor(queryID, sheddingFactor);
	}

	@Override
	public void rollbackLoadShedding() {
		if (activeQueries.isEmpty()) {
			return;
		}
		
		int queryID = getQueryIDToRollback();
		if (queryID < 0) {
			return;
		}
		
		if (activeQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(Integer.valueOf(queryID));
			}
			int sheddingFactor = activeQueries.get(queryID) - LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor <= 0) {
				sheddingFactor = 0;
				activeQueries.remove(queryID);
				if (simpleActiveQueries.contains(queryID)) {
					simpleActiveQueries.remove(Integer.valueOf(queryID));
				}
			} else {
				activeQueries.replace(queryID, sheddingFactor);
			}
			setSheddingFactor(queryID, sheddingFactor);
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
	
	private int getSheddingQueryIDDefault() {
		Map<Integer, Integer> queryRanks = getQueryRanks();
		int partialQuery = getQueryWithGreatestRank(queryRanks);
		if (partialQuery < 0) {
			partialQuery = queryFromSimpleLoadShedding();
			if (partialQuery > 0) {
				addToSimpleQueries(partialQuery);
			}
		} else {
			removeFromSimpleQueries(partialQuery);
		}
		return partialQuery;
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
		if (actComplexFactor <= 0) {
			actComplexFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		Map<Integer, Integer> queryRanks = getQueryRanks();
		int partialQuery = getQueryWithGreatestRank(queryRanks);
			
		while (partialQuery < 0) {
			if (actComplexFactor < 100) {
				actComplexFactor += LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if (actComplexFactor > 100) {
					actComplexFactor = 100;
				}
			} else {
				break;
			}
			partialQuery = getQueryWithGreatestRank(queryRanks);
		}
		if (partialQuery > 0) {
			removeFromSimpleQueries(partialQuery);
		} else {
			partialQuery = queryFromSimpleLoadSheddingEqually();
			if (partialQuery > 0) {
				addToSimpleQueries(partialQuery);
			}
		}
		return partialQuery;
	}
	
	private int getQueryIDToRollbackDefault() {
		int queryID;
		if (!simpleActiveQueries.isEmpty()) {
			queryID = getSimpleActiveQueryID();
		} else {
			queryID = getComplexActiveQueryID();
		}
		return queryID;
	}
	
	private int getQueryIDToRollbackSeparately() {
		int queryID = -1;
		if (actSheddingQueryDown > -1 && activeQueries.containsKey(actSheddingQueryDown)) {
			queryID = actSheddingQueryDown;
		} else {
			queryID = getQueryIDToRollbackDefault();
			actSheddingQueryDown = queryID;
		}
		return queryID;
	}
	
	private int getQueryIDToRollbackEqually() {
		int queryID = -1;
		if (!simpleActiveQueries.isEmpty()) {
			queryID = getSimpleActiveQueryIDEqually();
		} else {
			queryID = getComplexActiveQueryIDEqually();
		}
		return queryID;
	}
	
	private Map<Integer, Integer> getQueryRanks() {
		
		List<IPhysicalQuery> queuelengths = removeQueriesWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQuerysWithIncreasingTendency());
		
		List<IPhysicalQuery> latencies = removeQueriesWithMaxSheddingFactor
				(LATENCY_MONITOR.getQuerysWithIncreasingTendency());
		
		Map<Integer, Integer> queryRanks = new HashMap<>();
		
		for(int q = 0; q < queuelengths.size(); q++) {
			queryRanks.put(queuelengths.get(q).getID(), (q));
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
		
		if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			if (activeQueries.containsKey(partialQuery)) {
				if (activeQueries.get(partialQuery) >= actComplexFactor) {
					Map<Integer, Integer> newQueryRanks = new HashMap<>(queryRanks);
					newQueryRanks.remove(partialQuery);
					return getQueryWithGreatestRank(newQueryRanks);
				}
			}
		}
		
		return partialQuery;
	}
	
	private int queryFromSimpleLoadShedding() {
		List<Integer> list = new ArrayList<Integer>(allowedQueries.keySet());
		
		Iterator<Integer> iter = list.iterator();
		
		while (iter.hasNext()) {
			int queryID = iter.next();
			if (maxSheddingQueries.contains(queryID) || 
					(activeQueries.containsKey(queryID) && !simpleActiveQueries.contains(queryID))) {
				iter.remove();
			}
		}
		
		if (list.isEmpty()){
			return -1;
		}
		Collections.shuffle(list);
		return list.get(0);
	}
	
	private int queryFromSimpleLoadSheddingEqually() {
		if (actSimpleFactor <= 0) {
			actSimpleFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		List<Integer> list = new ArrayList<Integer>(allowedQueries.keySet());
		
		Iterator<Integer> iter = list.iterator();
		
		while (iter.hasNext()) {
			int queryID = iter.next();
			if (!maxSheddingQueries.contains(queryID)) {
				if (activeQueries.containsKey(queryID)) {
					if (!simpleActiveQueries.contains(queryID) || activeQueries.get(queryID) >= actSimpleFactor) {
						iter.remove();
					}
				}
			} else {
				iter.remove();
			}
		}
		
		if (list.isEmpty()){
			if (actSimpleFactor < 100) {
				actSimpleFactor += LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if (actSimpleFactor > 100) {
					actSimpleFactor = 100;
				}
				return queryFromSimpleLoadShedding();
			} else {
				return -1;
			}
		} else {
			Collections.shuffle(list);
			return list.get(0);
		}
	}
	
	private List<IPhysicalQuery> removeQueriesWithMaxSheddingFactor(List<IPhysicalQuery> list) {
		Iterator<IPhysicalQuery> iter = list.iterator();

		while (iter.hasNext()) {
			IPhysicalQuery query = iter.next();
			if (maxSheddingQueries.contains(query.getID())) {
				iter.remove();
			}
		}
		return list;
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated and 
	 * was activated through simple load shedding.
	 * @return
	 */
	private int getSimpleActiveQueryID() {
		Collections.shuffle(simpleActiveQueries);
		return simpleActiveQueries.get(0);
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated and 
	 * was activated through complex load shedding.
	 * @return
	 */
	private int getComplexActiveQueryID() {
		List<Integer> list = new ArrayList<>(activeQueries.keySet());
		Collections.shuffle(list);
		return list.get(0);
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated and 
	 * was activated through simple load shedding.
	 * @return
	 */
	private int getSimpleActiveQueryIDEqually() {
		List<Integer> list = new ArrayList<Integer>(simpleActiveQueries);
		
		Iterator<Integer> iter = list.iterator();
		
		while (iter.hasNext()) {
			int queryID = iter.next();
			if (activeQueries.get(queryID) < actSimpleFactor) {
				iter.remove();
			}
		}
		
		if (list.isEmpty()) {
			if (actSimpleFactor > 0) {
				actSimpleFactor -= LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if (actSimpleFactor < 0) {
					actSimpleFactor = 0;
				}
				return getSimpleActiveQueryIDEqually();
			} else {
				return -1;
			}
		} else {
			Collections.shuffle(list);
			return list.get(0);
		}
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated and 
	 * was activated through complex load shedding.
	 * @return
	 */
	private int getComplexActiveQueryIDEqually() {
		List<Integer> list = new ArrayList<Integer>(activeQueries.keySet());
		
		Iterator<Integer> iter = list.iterator();
		
		while (iter.hasNext()) {
			int queryID = iter.next();
			if (activeQueries.get(queryID) < actComplexFactor) {
				iter.remove();
			}
		}
		
		if (list.isEmpty()) {
			if (actComplexFactor > 0) {
				actComplexFactor -= LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if (actComplexFactor < 0) {
					actComplexFactor = 0;
				}
				return getComplexActiveQueryIDEqually();
			} else {
				return -1;
			}
		} else {
			Collections.shuffle(list);
			return list.get(0);
		}
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
