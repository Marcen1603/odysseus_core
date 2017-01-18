package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for simple load shedding in consideration of priorities.
 * 
 * Only the CPU load is used in this status component.
 * 
 * @author Jannes
 *
 */
public class SimpleLoadSheddingWPAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	private final String NAME = "simpleWP";
	
	private int minPriority = -1;
	private int maxPriority = -1;
	
	/**
	 * Contains all queries with active load shedding.
	 */
	private HashMap<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	private ArrayList<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
	public SimpleLoadSheddingWPAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}
	
	@Override
	public void addQuery(int queryID) {
		IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		if(minPriority < 0) {
			minPriority = query.getPriority();
			maxPriority = query.getPriority();
		} else {
			if (query.getPriority() < minPriority) {
				minPriority = query.getPriority();
			} else if(maxPriority < query.getPriority()) {
				maxPriority = query.getPriority();
			}
		}
	}

	@Override
	public void removeQuery(int queryID) {
		IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueries(superUser);
		if(!queries.isEmpty()) {
			if (query.getPriority() <= minPriority) {
				minPriority = 10;
				for (IPhysicalQuery q : queries) {
					if (q.getPriority() < minPriority) {
						minPriority = q.getPriority();
					}
				}
			} else if (query.getPriority() >= maxPriority) {
				maxPriority = 0;
				for (IPhysicalQuery q : queries) {
					if (q.getPriority() > maxPriority) {
						maxPriority = q.getPriority();
					}
				}
			}
		} else {
			minPriority = -1;
			maxPriority = -1;
		}
	}

	@Override
	public void runLoadShedding() {
		int queryID = getIncreasingRandomQueryID(minPriority);
		if (queryID < 0) {
			return;
		}
		
		IPhysicalQuery physQuery = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		ILogicalQuery logQuery = physQuery.getLogicalQuery();
		int maxSheddingFactor = (int) logQuery.getParameter("maxSheddingFactor");
		
		int sheddingFactor;
		if (activeQueries.containsKey(queryID)) {
			sheddingFactor = activeQueries.get(queryID) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor >= maxSheddingFactor) {
				maxSheddingQueries.add(queryID);
			}
			activeQueries.replace(queryID, sheddingFactor);
		} else {
			sheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			activeQueries.put(queryID, sheddingFactor);
		}
		AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, superUser);
	}

	@Override
	public void rollBackLoadShedding() {
		int queryID = getDecreasingRandomQueryID(maxPriority);
		if (queryID < 0) {
			return;
		}
		
		if (activeQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(queryID);
			}
			int sheddingFactor = activeQueries.get(queryID) - LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor <= 0) {
				activeQueries.remove(queryID);
			}
			AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, superUser);
		}
	}

	@Override
	public void measureStatus() {
	}
	
	/**
	 * Recursive method to find the queryID with the minimal priority.
	 * @param actPriority
	 * @return
	 */
	private int getIncreasingRandomQueryID(int actPriority) {
		if (actPriority < 0) {
			return -1;
		}
		
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueries(superUser);
		if (queries.isEmpty()) {
			return -1;
		}
		
		for(IPhysicalQuery query : queries) {
			if (query.getPriority() == actPriority &&
					!maxSheddingQueries.contains(query.getID())) {
				return query.getID();
			}
		}
		
		if(actPriority >= maxPriority) {
			return -1;
		}
		
		actPriority++;
		return getIncreasingRandomQueryID(actPriority);
	}
	
	/**
	 * Recursive method to find the queryID with the maximal priority.
	 * @param actPriority
	 * @return
	 */
	private int getDecreasingRandomQueryID(int actPriority) {
		if (actPriority < 0) {
			return -1;
		}
		
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueries(superUser);
		if (queries.isEmpty()) {
			return -1;
		}
		
		for(IPhysicalQuery query : queries) {
			if (query.getPriority() == actPriority &&
					activeQueries.containsKey(query.getID())) {
				return query.getID();
			}
		}
		
		if(actPriority <= minPriority) {
			return -1;
		}
		
		actPriority--;
		return getDecreasingRandomQueryID(actPriority);
	}

	@Override
	public String getComponentName() {
		return NAME;
	}

}
