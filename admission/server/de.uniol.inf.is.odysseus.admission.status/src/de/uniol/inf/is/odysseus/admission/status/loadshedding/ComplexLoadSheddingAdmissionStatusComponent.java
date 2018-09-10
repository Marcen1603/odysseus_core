package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for load shedding with latencies and queue lengths.
 * 
 * The CPU load together which the latency and queue lengths of each query is used in this status component.
 * 
 * @author Jannes
 *
 */
public class ComplexLoadSheddingAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {
	
	/**
	 * The name of this status component.
	 */
	private final String NAME = "complex";
	
	//The different admission monitors.
	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	//These variables are used for the separately load shedding strategy.
	private volatile int actSheddingQueryUp = -1;
	private volatile int actSheddingQueryDown = -1;
	
	//These variables are used for the equally load shedding strategy.
	private volatile int actComplexFactor = 0;
	private volatile int actSimpleFactor = 0;
	
	@Override
	public boolean addQuery(int queryID) {
		if(super.addQuery(queryID)) {
			//The query is added to the admission monitors.
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
			//The query is removed from the admission monitors.
			IPhysicalQuery query = getQueryByID(queryID);
			QUEUE_LENGTHS_MONITOR.removeQuery(query);
			LATENCY_MONITOR.removeQuery(query);
			
			//The query is removed from all variables.
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
		
		increaseFactor(queryID);
	}

	@Override
	public void rollbackLoadShedding() {
		if (activeQueries.isEmpty()) {
			return;
		}
		
		//Gets the next query, which should decrease its shedding factor.
		int queryID = getQueryIDToRollback();
		if (queryID < 0) {
			return;
		}
		decreaseFactor(queryID);
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
	 * Returns the next query with the default strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDDefault() {
		Map<Integer, Integer> queryRanks = getQueryRanks();
		int partialQuery = getQueryWithGreatestRank(queryRanks);
		
		//No query was found with complex load shedding.
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
		if (actComplexFactor <= 0) {
			actComplexFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		Map<Integer, Integer> queryRanks = getQueryRanks();
		int partialQuery = getQueryWithGreatestRank(queryRanks);
		
		//Try to get query from complex load shedding.
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
			//No query from complex load shedding was found.
			partialQuery = queryFromSimpleLoadSheddingEqually();
			if (partialQuery > 0) {
				addToSimpleQueries(partialQuery);
			}
		}
		return partialQuery;
	}
	
	/**
	 * Returns the query to roll back with the default strategy.
	 * @return queryID
	 */
	private int getQueryIDToRollbackDefault() {
		int queryID;
		if (!simpleActiveQueries.isEmpty()) {
			queryID = getSimpleActiveQueryID();
		} else {
			queryID = getComplexActiveQueryID();
		}
		return queryID;
	}
	
	/**
	 * Returns the query to roll back with the separately strategy.
	 * @return queryID
	 */
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
	
	/**
	 * Returns the query to roll back with the equally strategy.
	 * @return queryID
	 */
	private int getQueryIDToRollbackEqually() {
		int queryID = -1;
		if (!simpleActiveQueries.isEmpty()) {
			queryID = getSimpleActiveQueryIDEqually();
		} else {
			queryID = getComplexActiveQueryIDEqually();
		}
		return queryID;
	}
	
	/**
	 * Returns a map with all queries, which have increasing tendencies.
	 * @return queryID
	 */
	private Map<Integer, Integer> getQueryRanks() {
		
		List<IPhysicalQuery> queuelengths = removeQueriesWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQueriesWithIncreasingTendency());
		
		List<IPhysicalQuery> latencies = removeQueriesWithMaxSheddingFactor
				(LATENCY_MONITOR.getQueriesWithIncreasingTendency());
		
		Map<Integer, Integer> queryRanks = new HashMap<>();
		
		//The rank is calculated here.
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
	
	/**
	 * Returns the query with the greatest rank from the given map.
	 * @param queryRanks
	 * @return queryID
	 */
	protected int getQueryWithGreatestRank(Map<Integer, Integer> queryRanks) {
		int partialQuery = super.getQueryWithGreatestRank(queryRanks);
		if(partialQuery < 0) {
			return -1;
		}
		//For the equally strategy is it important to check if the shedding factor matches
		if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			if (activeQueries.containsKey(partialQuery)) {
				if (activeQueries.get(partialQuery) >= actComplexFactor) {
					//The shedding factor of this query is to great.
					Map<Integer, Integer> newQueryRanks = new HashMap<>(queryRanks);
					newQueryRanks.remove(partialQuery);
					return getQueryWithGreatestRank(newQueryRanks);
				}
			}
		}
		
		return partialQuery;
	}
	
	/**
	 * Returns a randomly selected query for the default and separately strategy.
	 * @return queryID
	 */
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
	
	/**
	 * Returns a randomly selected query for the equally strategy.
	 * @return queryID
	 */
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
		
		//No query was found with a smaller shedding factor than the actSimpleFactor.
		if (list.isEmpty()){
			if (actSimpleFactor < 100) {
				actSimpleFactor += LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if (actSimpleFactor > 100) {
					actSimpleFactor = 100;
				}
				return queryFromSimpleLoadSheddingEqually();
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
	 * was activated through simple load shedding.
	 * @return queryID
	 */
	private int getSimpleActiveQueryID() {
		Collections.shuffle(simpleActiveQueries);
		return simpleActiveQueries.get(0);
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated and 
	 * was activated through complex load shedding.
	 * @return queryID
	 */
	private int getComplexActiveQueryID() {
		List<Integer> list = new ArrayList<>(activeQueries.keySet());
		Collections.shuffle(list);
		return list.get(0);
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated and 
	 * was activated through simple load shedding with the equslly strategy.
	 * @return queryID
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
			//No query was found with a greater shedding factor than the actSimpleFactor.
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
	 * was activated through complex load shedding with the equally strategy.
	 * @return queryID
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
			//No query was found with a greater shedding factor than the actComplexFactor.
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

	@Override
	public String getComponentName() {
		return NAME;
	}
}
