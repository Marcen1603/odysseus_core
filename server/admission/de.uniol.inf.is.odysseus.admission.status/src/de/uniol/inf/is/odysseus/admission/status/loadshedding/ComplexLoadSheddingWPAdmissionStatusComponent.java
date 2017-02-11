package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
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

	private volatile Map<Integer, List<Integer>> priorityMap = new TreeMap<Integer, List<Integer>>();
	
	private List<Integer> simpleActiveQueries = new ArrayList<>();
	
	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	@Override
	public boolean addQuery(int queryID) {
		if(super.addQuery(queryID)) {
			IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
			QUEUE_LENGTHS_MONITOR.addQuery(query);
			LATENCY_MONITOR.addQuery(query);
			
			if(!priorityMap.containsKey(query.getPriority())) {
				List<Integer> list = new ArrayList<>();
				list.add(queryID);
				priorityMap.put(query.getPriority(), list);
			} else {
				List<Integer> list = priorityMap.get(query.getPriority());
				list.add(queryID);
				priorityMap.replace(query.getPriority(), list);
			}
			actIncreasingPriority = getSmallestPriority();
			actDecreasingPriority = getGreatestPriority();
			
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
			
			if (priorityMap.containsKey(query.getPriority())) {
				List<Integer> list = priorityMap.get(query.getPriority());
				if (list.contains(Integer.valueOf(queryID))) {
					list.remove(Integer.valueOf(queryID));
					if (list.isEmpty()) {
						priorityMap.remove(query.getPriority());
					}
				}
			}
			actIncreasingPriority = getSmallestPriority();
			actDecreasingPriority = getGreatestPriority();
			
			return true;	
		}
		return false;
	}

	@Override
	public void runLoadShedding() {
		if (!isSheddingPossible()) {
			return;
		}
		List<Integer> priorityIDList = getIncreasingPriorityQueryList();
		if (priorityIDList == null) {
			return;
		}
		List<Integer> queuelengths = getIDList(removeQuerysWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQuerysWithIncreasingTendency()));
		
		List<Integer> latencies = getIDList(removeQuerysWithMaxSheddingFactor
				(LATENCY_MONITOR.getQuerysWithIncreasingTendency()));
		for (int queryID : priorityIDList) {
			if (!queuelengths.contains(queryID) || !latencies.contains(queryID)) {
				queuelengths.remove(Integer.valueOf(queryID));
				latencies.remove(Integer.valueOf(queryID));
			}
		}
		
		boolean simple = false;
		int partialQuery = -1;
		if (!queuelengths.isEmpty() && !latencies.isEmpty()) {
			partialQuery = getQueryIDWithGreatestRanking(queuelengths, latencies);
		}
		if (partialQuery < 0) {
			simple = true;
			Collections.shuffle(priorityIDList);
			partialQuery = priorityIDList.get(0);
		}
		
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
		List<Integer> priorityList = getDecreasingPriorityQueryIDList();
		if (priorityList == null) {
			return;
		}
		List<Integer> simpleList = new ArrayList<>();
		for (int queryID : priorityList) {
			if (activeQueries.containsKey(queryID) && simpleList.contains(queryID)) {
				simpleList.add(queryID);
			}
		}
		int partialQuery;
		if (!simpleList.isEmpty()) {
			Collections.shuffle(simpleList);
			partialQuery = simpleList.get(0);
		} else {
			Collections.shuffle(priorityList);
			partialQuery = priorityList.get(0);
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
			}
			setSheddingFactor(partialQuery, sheddingFactor);
		}

	}

	@Override
	public void measureStatus() {
		QUEUE_LENGTHS_MONITOR.updateMeasurements();
	}
	
	private int getQueryIDWithGreatestRanking(List<Integer> queuelengths, List<Integer> latencies) {
		
		Map<Integer, Integer> queryRanks = new HashMap<>();
		
		for(int q = 0; q < queuelengths.size(); q++) {
			for(int l = 0; l < latencies.size(); l++) {
				if(queuelengths.get(q) == latencies.get(l)) {
					queryRanks.put(queuelengths.get(q), (q + l));
					latencies.remove(l);
					break;
				}
			}
			if(latencies.isEmpty()) {
				break;
			}
		}
		if(!queryRanks.isEmpty()) {
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
		return -1;
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
		for (int queryID : priorityMap.get(actIncreasingPriority)) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			return list;
		} else {
			int nextPrio = getNextPriority(actIncreasingPriority);
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
	 * Recursive method to find the active query with the maximal priority.
	 * @param actPriority
	 * @return
	 */
	private List<Integer> getDecreasingPriorityQueryIDList() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return null;
		}
		
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityMap.get(actDecreasingPriority)) {
			if (activeQueries.containsKey(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			return list;
		} else {
			int previousPrio = getPreviousPriority(actDecreasingPriority);
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
	
	private int getNextPriority(int oldPrio) {
		int priority = -1;
		Iterator<Integer> iterator = priorityMap.keySet().iterator();
		while (iterator.hasNext()) {
			int next = iterator.next();
			if(next == oldPrio) {
				if (iterator.hasNext()) {
					priority = iterator.next();
				} 
				break;
			}
		}
		return priority;
	}
	
	private int getPreviousPriority(int oldPrio) {
		int priority = -1;
		List<Integer> list = new ArrayList<>(priorityMap.keySet());
		Collections.reverse((List<?>) list);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == oldPrio) {
				if ((i + 1) < list.size()) {
					priority = list.get(i + 1);
				}
				break;
			}
		}
		return priority;
	}
	
	private int getGreatestPriority() {
		return Collections.max(priorityMap.keySet());
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
	
	private int getSmallestPriority() {
		return Collections.min(priorityMap.keySet());
	}
	
	private List<Integer> getIDList(List<IPhysicalQuery> physList) {
		List<Integer> idList = new ArrayList<>();
		for(IPhysicalQuery query : physList) {
			idList.add(query.getID());
		}
		return idList;
	}

	@Override
	public String getComponentName() {
		return NAME;
	}

}
