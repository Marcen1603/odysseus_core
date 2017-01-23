package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;

/**
 * Provides the status for simple load shedding.
 * 
 * Only the CPU load is used in this status component.
 * 
 * @author Jannes
 *
 */
public class SimpleLoadSheddingAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {
	
	private final String NAME = "simple";
	
	@Override
	public void measureStatus() {
		//Nothing to measure
	}
	
	@Override
	public void runLoadShedding() {
		if(!isSheddingPossible()) {
			return;
		}
		
		int queryID = getRandomPossibleQueryID();
		//It was not possible to find a queryID for load shedding.
		if (queryID < 0) {
			return;
		}
		
		int sheddingFactor;
		int maxSheddingFactor = allowedQueries.get(queryID);
		
		//The load shedding of this query is already active 
		if (activeQueries.containsKey(queryID)) {
			sheddingFactor = activeQueries.get(queryID) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			
			//The maximal shedding factor is reached.
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(queryID);
			}
			
			activeQueries.replace(queryID, sheddingFactor);
		} else {
			sheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			
			//The maximal shedding factor is reached.
			if (sheddingFactor >= maxSheddingFactor) {
				sheddingFactor = maxSheddingFactor;
				maxSheddingQueries.add(queryID);
			}
			
			activeQueries.put(queryID, sheddingFactor);
		}
		
		AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, superUser);
	}
	
	@Override
	public void rollbackLoadShedding() {
		int queryID = getRandomActiveQueryID();
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
		
		List<Integer> list = new ArrayList<Integer>(allowedQueries.keySet());
		
		// remove queries which already have their maximal shedding-factor
		for (int queryID : list) {
			if (maxSheddingQueries.contains(queryID)) {
				list.remove(Integer.valueOf(queryID));
			}
		}
		if (list.isEmpty()){
			return -1;
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
		List<Integer> list = new ArrayList<>(activeQueries.keySet());
		Collections.shuffle(list);
		return list.get(0);
	}

	@Override
	public String getComponentName() {
		return NAME;
	}
}
