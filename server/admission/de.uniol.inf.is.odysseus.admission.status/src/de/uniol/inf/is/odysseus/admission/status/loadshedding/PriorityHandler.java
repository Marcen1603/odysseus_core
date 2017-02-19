package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PriorityHandler {
	
	/**
	 * The ISession superUser is used to get access to the execution plan of the queries.
	 */
	static private final ISession superUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);

	private volatile Map<Integer, List<Integer>> priorityMap = new TreeMap<Integer, List<Integer>>();
	
	public void addQuery(int queryID) {
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
	}
	
	public void removeQuery(int queryID) {
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
	}
	
	public List<Integer> getQueriesByPriority(int priority) {
		return priorityMap.get(priority);
	}
	
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
	
	public int getGreatestPriority() {
		return Collections.max(priorityMap.keySet());
	}
	
	public int getSmallestPriority() {
		return Collections.min(priorityMap.keySet());
	}
}
