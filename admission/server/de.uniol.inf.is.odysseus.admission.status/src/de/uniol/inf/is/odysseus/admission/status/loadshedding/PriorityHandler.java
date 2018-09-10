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
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PriorityHandler {
	
	/**
	 * The ISession superUser is used to get access to the execution plan of the queries.
	 */
	static private final ISession superUser = SessionManagement.instance.loginSuperUser(null);

	/**
	 * All queries are assigned to their priority in this map.
	 */
	private volatile Map<Integer, List<Integer>> priorityMap = new TreeMap<Integer, List<Integer>>();
	
	//The priority for each Query is saved here, because in the removing process 
	//the physical query can already be removed, before the priority is queried.
	private volatile Map<Integer, Integer> queryPriority = new HashMap<>();
	
	/**
	 * Adds the query to this handler.
	 * @param queryID
	 */
	public void addQuery(int queryID) {
		IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		queryPriority.put(queryID, query.getPriority());
		
		//Check if the priorityMap already contains a list with the priority of the given query.
		if(!priorityMap.containsKey(query.getPriority())) {
			List<Integer> list = new ArrayList<>();
			list.add(queryID);
			priorityMap.put(query.getPriority(), list);
		} else {
			List<Integer> list = priorityMap.get(query.getPriority());
			list.add(queryID);
			priorityMap.replace(query.getPriority(), list);
		}
	}
	
	/**
	 * Removes the query from this handler.
	 * @param queryID
	 */
	public void removeQuery(int queryID) {
		LoggerFactory.getLogger(getClass()).info("remove : " + queryID);
		if (queryPriority.containsKey(queryID)) {
			int priority = queryPriority.get(queryID);
			if (priorityMap.containsKey(priority)) {
				List<Integer> list = priorityMap.get(priority);
				if (list.contains(queryID)) {
					list.remove(Integer.valueOf(queryID));
					if (list.isEmpty()) {
						//The list has to be removed, if there is no query left.
						priorityMap.remove(priority);
					} else {
						priorityMap.replace(priority, list);
					}
				}
			}
		}
	}
	
	/**
	 * Returns a list, which contains all queries with the given priority.
	 * @param priority
	 * @return List with queryIDs
	 */
	public List<Integer> getQueriesByPriority(int priority) {
		return priorityMap.get(priority);
	}
	
	/**
	 * Returns the next greater priority of the given priority.
	 * @param oldPrio
	 * @return priority
	 */
	public int getNextPriority(int oldPrio) {
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
	
	/**
	 * Returns the next smaller priority of the given priority.
	 * @param oldPrio
	 * @return priority
	 */
	public int getPreviousPriority(int oldPrio) {
		int priority = -1;
		List<Integer> list = new ArrayList<>(priorityMap.keySet());
		Collections.reverse(list);
		Iterator<Integer> iterator = list.iterator();
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
	
	/**
	 * Returns the greatest saved priority.
	 * @return Priority
	 */
	public int getGreatestPriority() {
		if (!priorityMap.isEmpty()) {
			return Collections.max(priorityMap.keySet());
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns the smallest saved priority.
	 * @return Priority
	 */
	public int getSmallestPriority() {
		if (!priorityMap.isEmpty()) {
			return Collections.min(priorityMap.keySet());
		} else {
			return -1;
		}
	}
}
