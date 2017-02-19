package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	private int actSheddingFactor = 0;
	
	private int actSheddingQuery = -1;

	@Override
	public boolean removeQuery(int queryID) {
		if (super.removeQuery(queryID)) {
			if(actSheddingQuery == queryID) {
				actSheddingQuery = -1;
			}
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
		
		int queryID = getSheddingQueryID();
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
		
		setSheddingFactor(queryID, sheddingFactor);
	}
	
	@Override
	public void rollbackLoadShedding() {
		int queryID = getQueryIDToRollback();
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
			} else {
				activeQueries.replace(queryID, sheddingFactor);
			}
			setSheddingFactor(queryID, sheddingFactor);
		}
	}
	
	/**
	 * 
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
	
	private int getQueryIDToRollback() {
		if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.DEFAULT) {
			return getRandomActiveQueryIDDefault();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			return getRandomActiveQueryIDEqually();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.SEPARATELY) {
			return getRandomActiveQueryIDSeparately();
		} else {
			return -1;
		}
	}
	
	/**
	 * Estimates a possible random queryID.
	 * 
	 * Only queryIDs are returned, which query has not already its maximal shedding-factor.
	 * @return
	 */
	private int getSheddingQueryIDSeparately() {
		if (actSheddingQuery > -1 && !maxSheddingQueries.contains(actSheddingQuery)) {
			return actSheddingQuery;
		}
		
		actSheddingQuery = getSheddingQueryIDDefault();
		return actSheddingQuery;
	}
	
	/**
	 * Estimates a possible random queryID.
	 * 
	 * Only queryIDs are returned, which query has not already its maximal shedding-factor.
	 * @return
	 */
	private int getSheddingQueryIDDefault() {
		
		List<Integer> list = new ArrayList<Integer>();
		
		// remove queries which already have their maximal shedding-factor
		for (int queryID : allowedQueries.keySet()) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (list.isEmpty()){
			return -1;
		}
		Collections.shuffle(list);
		return list.get(0);
	}
	
	/**
	 * Estimates a possible random queryID.
	 * 
	 * Only queryIDs are returned, which query has not already its maximal shedding-factor.
	 * @return
	 */
	private int getSheddingQueryIDEqually() {
		
		if (actSheddingFactor <= 0) {
			actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		List<Integer> list = new ArrayList<Integer>();
		
		// remove queries which already have their maximal shedding-factor
		for (int queryID : allowedQueries.keySet()) {
			if (!maxSheddingQueries.contains(queryID)) {
				if (activeQueries.containsKey(queryID)) {
					if (activeQueries.get(queryID) < actSheddingFactor) {
						list.add(Integer.valueOf(queryID));
					}
				} else {
					list.add(Integer.valueOf(queryID));
				}
			}
		}
		if (list.isEmpty()){
			if(actSheddingFactor < 100) {
				actSheddingFactor += LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if(actSheddingFactor > 100) {
					actSheddingFactor = 100;
				}
				return getSheddingQueryIDEqually();
			} else {
				return -1;
			}
		}
		Collections.shuffle(list);
		return list.get(0);
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated.
	 * @return
	 */
	private int getRandomActiveQueryIDSeparately() {
		if (activeQueries.isEmpty()) {
			actSheddingQuery = -1;
			return -1;
		}
		if(activeQueries.containsKey(actSheddingQuery)) {
			return actSheddingQuery;
		}
		
		actSheddingQuery = getRandomActiveQueryIDDefault();
		return actSheddingQuery;
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated.
	 * @return
	 */
	private int getRandomActiveQueryIDDefault() {
		if (activeQueries.isEmpty()) {
			return -1;
		}
		List<Integer> list = new ArrayList<>(activeQueries.keySet());
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated.
	 * @return
	 */
	private int getRandomActiveQueryIDEqually() {
		if (activeQueries.isEmpty()) {
			return -1;
		}
		List<Integer> list = new ArrayList<>();
		
		for (int queryID : activeQueries.keySet()) {
			if (activeQueries.get(queryID) >= actSheddingFactor) {
				list.add(queryID);
			}
		}
		
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			if (actSheddingFactor > 0) {
				actSheddingFactor -= LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
				if (actSheddingFactor < 0) {
					actSheddingFactor = 0;
				}
				return getRandomActiveQueryIDEqually();
			} else {
				return -1;
			}
		}
	}

	@Override
	public String getComponentName() {
		return NAME;
	}
}
