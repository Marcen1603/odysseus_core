package de.uniol.inf.is.odysseus.admission.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for simple load shedding.
 * 
 * Only the CPU load is used in this status component.
 * 
 * @author Jannes
 *
 */
public class SimpleLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {
	
	/**
	 * Contains all queries with active load shedding.
	 */
	private HashMap<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	private ArrayList<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
	@Override
	public void addQuery(IPhysicalQuery query) {
	}
	
	@Override
	public void removeQuery(IPhysicalQuery query) {
	}
	
	@Override
	public void measureStatus() {
	}
	
	@Override
	public void runLoadShedding() {
		int queryID = getRandomPossibleQueryID();
		
		//It was not possible to find a queryID for load shedding.
		if (queryID < 0) {
			return;
		}
		
		int sheddingFactor;
		
		//The load shedding of this query is already active 
		if (activeQueries.containsKey(queryID)) {
			sheddingFactor = activeQueries.get(queryID) + 10;
			
			//The maximal shedding factor is reached.
			if (sheddingFactor >= MAX_SHEDDING_FACTOR) {
				maxSheddingQueries.add(queryID);
			}
			activeQueries.replace(queryID, sheddingFactor);
		} else {
			sheddingFactor = 10;
			activeQueries.put(queryID, sheddingFactor);
		}
		
		AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, superUser);
	}
	
	@Override
	public void rollBackLoadShedding() {
		int queryID = getRandomActiveQueryID();
		if (queryID < 0) {
			return;
		}
		
		if (activeQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(queryID);
			}
			int sheddingFactor = activeQueries.get(queryID) - 10;
			if (sheddingFactor <= 0) {
				activeQueries.remove(queryID);
			}
			AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, superUser);
		}
	}
	
	/**
	 * Estimates a possible random queryID.
	 * 
	 * Only queryIDs are returned, which query has not already its maximal shedding-factor.
	 * @return
	 */
	private int getRandomPossibleQueryID() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueries(superUser);
		if (queries.isEmpty()) {
			return -1;
		}
		
		// remove queries which already have their maximal shedding-factor
		for (IPhysicalQuery query : queries) {
			if (maxSheddingQueries.contains(query.getID())) {
				queries.remove(query);
			}
		}
		
		double numQueries = (double) queries.size();
		int place = (int) (Math.random() * numQueries);
		return queries.toArray(new IPhysicalQuery[queries.size()])[place].getID();
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated.
	 * @return
	 */
	private int getRandomActiveQueryID() {
		if (activeQueries.isEmpty()) {
			return -1;
		}
		
		double numActiveQueries = (double) activeQueries.size();
		int place = (int) (Math.random() * numActiveQueries);
		return activeQueries.keySet().toArray(new Integer[activeQueries.size()])[place];
	}
}
