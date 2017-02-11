package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for simple load shedding in consideration of priorities.
 * 
 * Only the CPU load is used in this status component.
 * 
 * @author Jannes
 *
 */
public class SimpleLoadSheddingWPAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {

	private final String NAME = "simpleWP";
	
	private volatile int actIncreasingPriority = -1;
	private volatile int actDecreasingPriority = -1;

	private volatile Map<Integer, List<Integer>> priorityMap = new TreeMap<Integer, List<Integer>>();
	
	@Override
	public boolean addQuery(int queryID) {
		if (super.addQuery(queryID)) {
			IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
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
			if (priorityMap.containsKey(query.getPriority())) {
				List<Integer> list = priorityMap.get(query.getPriority());
				if (list.contains(queryID)) {
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
	public void measureStatus() {
		//Nothing to measure
	}

	@Override
	public void runLoadShedding() {
		if(!isSheddingPossible()) {
			return;
		}
		int queryID = getIncreasingPriorityRandomQueryID();
		if (queryID < 0) {
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
		int queryID = getDecreasingPriorityRandomQueryID();
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
			}
			setSheddingFactor(queryID, sheddingFactor);
		}
	}
	
	/**
	 * Recursive method to find a query with minimal priority, which has not its maximal shedding factor. 
	 * @return
	 */
	private int getIncreasingPriorityRandomQueryID() {
		if (actIncreasingPriority < 0) {
			return -1;
		}
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityMap.get(actIncreasingPriority)) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			int nextPrio = getNextPriority(actIncreasingPriority);
			if (nextPrio < 0) {
				return -1;
			}
			actIncreasingPriority = nextPrio;
			if (actIncreasingPriority > actDecreasingPriority) {
				actDecreasingPriority = actIncreasingPriority;
			}
			
			return getIncreasingPriorityRandomQueryID();
		}
	}
	
	/**
	 * Recursive method to find the active query with the maximal priority.
	 * @param actPriority
	 * @return
	 */
	private int getDecreasingPriorityRandomQueryID() {
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return -1;
		}
		
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityMap.get(actDecreasingPriority)) {
			if (activeQueries.containsKey(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			int previousPrio = getPreviousPriority(actDecreasingPriority);
			if (previousPrio < 0) {
				return -1;
			}
			actDecreasingPriority = previousPrio;
			if (actDecreasingPriority < actIncreasingPriority) {
				actIncreasingPriority = actDecreasingPriority;
			}
			return getDecreasingPriorityRandomQueryID();
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
	
	private int getGreatestPriority() {
		return Collections.max(priorityMap.keySet());
	}
	
	private int getSmallestPriority() {
		return Collections.min(priorityMap.keySet());
	}

	@Override
	public String getComponentName() {
		return NAME;
	}

}
