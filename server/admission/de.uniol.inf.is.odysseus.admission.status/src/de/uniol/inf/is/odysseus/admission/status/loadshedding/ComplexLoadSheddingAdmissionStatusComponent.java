package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
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
	
	@Override
	public boolean addQuery(int queryID) {
		if(super.addQuery(queryID)) {
			IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
			QUEUE_LENGTHS_MONITOR.addQuery(query);
			LATENCY_MONITOR.addQuery(query);
			return true;
		}
		return false;
		
	}

	@Override
	public boolean removeQuery(int queryID) {
		if (super.removeQuery(queryID)) {
			IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
			QUEUE_LENGTHS_MONITOR.removeQuery(query);
			LATENCY_MONITOR.removeQuery(query);
			
			if (simpleActiveQueries.contains(queryID)) {
				simpleActiveQueries.remove(Integer.valueOf(queryID));
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
		int queryID = queryFromComplexLoadShedding();
		if (queryID < 0) {
			queryID = queryFromSimpleLoadShedding();
			simple = true;
			if (queryID < 0) {
				return;
			}
		}
		int maxSheddingFactor = allowedQueries.get(queryID);
		int sheddingFactor;
		boolean first;
		if (activeQueries.containsKey(queryID)) {
			first = false;
			sheddingFactor = activeQueries.get(queryID) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(queryID);
			}
			activeQueries.replace(queryID, sheddingFactor);
		} else {
			first = true;
			sheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(queryID);
			}
			activeQueries.put(queryID, sheddingFactor);
		}
		if (simple) {
			if (first) {
				simpleActiveQueries.add(queryID);
			}
		} else {
			if (simpleActiveQueries.contains(queryID)) {
				simpleActiveQueries.remove(Integer.valueOf(queryID));
			}
		}
		setSheddingFactor(queryID, sheddingFactor);
	}

	@Override
	public void rollbackLoadShedding() {
		if (activeQueries.isEmpty()) {
			return;
		}
		int queryID;
		if (!simpleActiveQueries.isEmpty()) {
			queryID = getSimpleActiveQueryID();
		} else {
			queryID = getComplexActiveQueryID();
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
			}
			setSheddingFactor(queryID, sheddingFactor);
		}
	}

	@Override
	public void measureStatus() {
		QUEUE_LENGTHS_MONITOR.updateMeasurements();
	}
	
	private int queryFromComplexLoadShedding() {
		List<IPhysicalQuery> queuelengths = removeQuerysWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQuerysWithIncreasingTendency());
		
		List<IPhysicalQuery> latencies = removeQuerysWithMaxSheddingFactor
				(LATENCY_MONITOR.getQuerysWithIncreasingTendency());
		
		Map<Integer, Integer> queryRanks = new HashMap<>();
		
		if(!queuelengths.isEmpty() && !latencies.isEmpty()) {
			for(int q = 0; q < queuelengths.size(); q++) {
				for(int l = 0; l < latencies.size(); l++) {
					if(queuelengths.get(q) == latencies.get(l)) {
						queryRanks.put(queuelengths.get(q).getID(), (q + l));
						latencies.remove(l);
						break;
					}
				}
				if(latencies.isEmpty()) {
					break;
				}
			}

			return getQueryWithGreatestRank(queryRanks);
		}
		return -1;
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

	@Override
	public String getComponentName() {
		return NAME;
	}

}
