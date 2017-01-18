package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
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
	
	private final String NAME = "simple";
	
	private HashMap<Integer, Integer> allowedQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with active load shedding.
	 */
	private HashMap<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	private ArrayList<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
	public SimpleLoadSheddingAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}
	
	@Override
	public void addQuery(int queryID) {
		LoggerFactory.getLogger(this.getClass()).info("addquerysimpleloadshedding : " + queryID);
		IPhysicalQuery physQuery = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		ILogicalQuery logQuery = physQuery.getLogicalQuery();
		Object o = logQuery.getParameter("LOADSHEDDINGENABLED");
		if (o != null) {
			LoggerFactory.getLogger(this.getClass()).info("LOADSHEDDINGENABLED != null : " + queryID);
			boolean enabled = (boolean) o;
			if (enabled) {
				LoggerFactory.getLogger(this.getClass()).info("LOADSHEDDINGENABLED : " + queryID);
				int maxSheddingFactor = (int) logQuery.getParameter("MAXSHEDDINGFACTOR");
				LoggerFactory.getLogger(this.getClass()).info("LOADSHEDDINGENABLED : " + queryID);
			}
		} else {
			LoggerFactory.getLogger(this.getClass()).info("LOADSHEDDINGENABLED == null : " + queryID);
		}
		
	}
	
	@Override
	public void removeQuery(int queryID) {
		LoggerFactory.getLogger(this.getClass()).info("removequerysimpleloadshedding");
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
		
		int sheddingFactor = 0;
		
		IPhysicalQuery physQuery = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		ILogicalQuery logQuery = physQuery.getLogicalQuery();
		int maxSheddingFactor = (int) logQuery.getParameter("MAXSHEDDINGFACTOR");
		
		//The load shedding of this query is already active 
		if (activeQueries.containsKey(queryID)) {
			sheddingFactor = activeQueries.get(queryID) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			
			//The maximal shedding factor is reached.
			if (sheddingFactor >= maxSheddingFactor) {
				maxSheddingQueries.add(queryID);
				sheddingFactor = sheddingFactor - (sheddingFactor - maxSheddingFactor);
			}
			
			activeQueries.replace(queryID, sheddingFactor);
		} else {
			sheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			
			//The maximal shedding factor is reached.
			if (sheddingFactor >= maxSheddingFactor) {
				maxSheddingQueries.add(queryID);
				sheddingFactor = sheddingFactor - (sheddingFactor - maxSheddingFactor);
			}
			
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
			int sheddingFactor = activeQueries.get(queryID) - LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor <= 0) {
				sheddingFactor = 0;
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
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		// remove queries which already have their maximal shedding-factor
		for (IPhysicalQuery query : queries) {
			if (!maxSheddingQueries.contains(query.getID())) {
				list.add(query.getID());
			}
		}
		Collections.shuffle(list);
		return list.get(0);
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated.
	 * @return
	 */
	private int getRandomActiveQueryID() {
		if (activeQueries.isEmpty()) {
			return -1;
		}
		ArrayList<Integer> list = new ArrayList<Integer>(activeQueries.keySet());
		Collections.shuffle(list);
		return list.get(0);
	}

	@Override
	public String getComponentName() {
		return NAME;
	}
}
