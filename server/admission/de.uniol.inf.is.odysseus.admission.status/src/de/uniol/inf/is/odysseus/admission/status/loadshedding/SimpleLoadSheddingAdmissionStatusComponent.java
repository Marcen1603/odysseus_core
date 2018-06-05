package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides the status for simple load shedding.
 * 
 * Only the CPU load is used in this status component.
 */
public class SimpleLoadSheddingAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {
	
	/**
	 * The name of this status component.
	 */
	private final String NAME = "simple";
	
	//This attribute is used in the equally strategy.
	private volatile int actSheddingFactor = 0;
	
	//This attribute is used in the separately strategy.
	private volatile int actSheddingQuery = -1;

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
		//Nothing to measure.
	}
	
	@Override
	public void runLoadShedding() {
		if(!isSheddingPossible()) {
			return;
		}
		int queryID = getSheddingQueryID();
		if (queryID < 0) {
			return;
		}
		increaseFactor(queryID);
		
	}
	
	@Override
	public void rollbackLoadShedding() {
		if (activeQueries.isEmpty()) {
			actSheddingQuery = -1;
			return;
		}
		
		int queryID = getQueryIDToRollback();
		if (queryID < 0) {
			return;
		}
		
		decreaseFactor(queryID);
	}
	
	/**
	 * Returns a query, which should increase its shedding factor, depending on the selected load shedding strategy.
	 * @return queryID
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
	
	/**
	 * Returns a query, which should decrease its shedding factor, depending on the selected load shedding strategy.
	 * @return queryID
	 */
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
	 * Estimates a possible random queryID with the separately strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDSeparately() {
		if (actSheddingQuery > -1 && !maxSheddingQueries.contains(actSheddingQuery)) {
			return actSheddingQuery;
		} else {
			actSheddingQuery = getSheddingQueryIDDefault();
			return actSheddingQuery;
		}
	}
	
	/**
	 * Estimates a possible random queryID with the default strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDDefault() {
		
		List<Integer> list = new ArrayList<Integer>();
		
		//Remove queries with maximal shedding factor.
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
	 * Estimates a possible random queryID with the equally strategy.
	 * @return queryID
	 */
	private int getSheddingQueryIDEqually() {
		
		if (actSheddingFactor <= 0) {
			actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		List<Integer> list = new ArrayList<Integer>();
		
		//Remove queries which already have their maximal shedding-factor
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
	 * Returns a queryID with active load shedding from the separately strategy.
	 * @return queryID
	 */
	private int getRandomActiveQueryIDSeparately() {
		if(activeQueries.containsKey(actSheddingQuery)) {
			return actSheddingQuery;
		}
		
		actSheddingQuery = getRandomActiveQueryIDDefault();
		return actSheddingQuery;
	}
	
	/**
	 * Returns a queryID with active load shedding from the default strategy.
	 * @return queryID
	 */
	private int getRandomActiveQueryIDDefault() {
		List<Integer> list = new ArrayList<>(activeQueries.keySet());
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns a queryID with active load shedding from the equally strategy.
	 * @return queryID
	 */
	private int getRandomActiveQueryIDEqually() {
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
