package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
	
	private PriorityHandler priorityHandler = new PriorityHandler();
	
	private volatile int actIncreasingPriority = -1;
	private volatile int actDecreasingPriority = -1;
	
	private int actSheddingQuery = -1;
	
	private int actSheddingFactor = 0;
	
	@Override
	public boolean addQuery(int queryID) {
		if (super.addQuery(queryID)) {
			priorityHandler.addQuery(queryID);
			actIncreasingPriority = priorityHandler.getSmallestPriority();
			actDecreasingPriority = priorityHandler.getGreatestPriority();
			
			return true;
		}
		return false;
	}

	@Override
	public boolean removeQuery(int queryID) {
		if (super.removeQuery(queryID)) {
			priorityHandler.removeQuery(queryID);
			actIncreasingPriority = priorityHandler.getSmallestPriority();
			actDecreasingPriority = priorityHandler.getGreatestPriority();
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
		if (actDecreasingPriority < 0 || activeQueries.isEmpty()) {
			return;
		}
		
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
			return getIncreasingPriorityQueryIDDefault();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			return getIncreasingPriorityQueryIDEqually();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.SEPARATELY) {
			return getIncreasingPriorityQueryIDSeparately();
		} else {
			return -1;
		}
	}
	
	private int getQueryIDToRollback() {
		if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.DEFAULT) {
			return getDecreasingPriorityQueryIDDefault();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.EQUALLY) {
			return getDecreasingPriorityQueryIDEqually();
		} else if (LoadSheddingAdmissionStatusRegistry.getSelectionStrategy() == QuerySelectionStrategy.SEPARATELY) {
			return getDecreasingPriorityQueryIDSeparately();
		} else {
			return -1;
		}
	}
	
	/**
	 * Recursive method to find a query with minimal priority, which has not its maximal shedding factor. 
	 * @return
	 */
	private int getIncreasingPriorityQueryIDDefault() {
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityHandler.getQueriesByPriority(actIncreasingPriority)) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			int nextPrio = priorityHandler.getNextPriority(actIncreasingPriority);
			if (nextPrio < 0) {
				return -1;
			}
			actIncreasingPriority = nextPrio;
			if (actIncreasingPriority > actDecreasingPriority) {
				actDecreasingPriority = actIncreasingPriority;
			}
			
			return getIncreasingPriorityQueryIDDefault();
		}
	}
	
	private int getIncreasingPriorityQueryIDSeparately() {
		if (actSheddingQuery > -1 && !maxSheddingQueries.contains(actSheddingQuery)) {
			return actSheddingQuery;
		} else {
			actSheddingQuery = getIncreasingPriorityQueryIDDefault();
			return actSheddingQuery;
		}
	}
	
	/**
	 * Recursive method to find the active query with the maximal priority.
	 * @param actPriority
	 * @return
	 */
	private int getDecreasingPriorityQueryIDDefault() {
		List<Integer> list = new ArrayList<>();
		for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
			if (activeQueries.containsKey(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			int previousPrio = priorityHandler.getPreviousPriority(actDecreasingPriority);
			if (previousPrio < 0) {
				return -1;
			}
			actDecreasingPriority = previousPrio;
			if (actDecreasingPriority < actIncreasingPriority) {
				actIncreasingPriority = actDecreasingPriority;
			}
			return getDecreasingPriorityQueryIDDefault();
		}
	}
	
	private int getDecreasingPriorityQueryIDSeparately() {
		if (actSheddingQuery > -1 && activeQueries.containsKey(actSheddingQuery)) {
			return actSheddingQuery;
		} else {
			actSheddingQuery = getDecreasingPriorityQueryIDDefault();
			return actSheddingQuery;
		}
	}
	
	/**
	 * Recursive method to find a query with minimal priority, which has not its maximal shedding factor. 
	 * @return
	 */
	private int getIncreasingPriorityQueryIDEqually() {	
		if (actSheddingFactor <= 0) {
			actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		while (actSheddingFactor <= 100) {
			List<Integer> possibleList = new ArrayList<>();
			for (int queryID : priorityHandler.getQueriesByPriority(actIncreasingPriority)) {
				if (!maxSheddingQueries.contains(queryID)) {
					if (activeQueries.containsKey(queryID)) {
						if (activeQueries.get(queryID) < actSheddingFactor) {
							possibleList.add(Integer.valueOf(queryID));
						}
					} else {
						possibleList.add(Integer.valueOf(queryID));
					}
				}
			}
			if (!possibleList.isEmpty()) {
				Collections.shuffle(possibleList);
				return possibleList.get(0);
			} else {
				if (actSheddingFactor < 100) {
					actSheddingFactor += LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
					if (actSheddingFactor > 100) {
						actSheddingFactor = 100;
					}
				} else {
					break;
				}
			}
		}
		
		int nextPrio = priorityHandler.getNextPriority(actIncreasingPriority);
		if (nextPrio < 0) {
			return -1;
		}
		actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		actIncreasingPriority = nextPrio;
		if (actIncreasingPriority > actDecreasingPriority) {
			actDecreasingPriority = actIncreasingPriority;
		}
		
		return getIncreasingPriorityQueryIDEqually();
	}
	
	/**
	 * Recursive method to find the active query with the maximal priority.
	 * @param actPriority
	 * @return
	 */
	private int getDecreasingPriorityQueryIDEqually() {
		while (actSheddingFactor >= 0) {
			List<Integer> list = new ArrayList<>();
			for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
				if (activeQueries.containsKey(queryID) && activeQueries.get(queryID) >= actSheddingFactor) {
					list.add(Integer.valueOf(queryID));
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
				} else {
					break;
				}
			}
		}
		
		int previousPrio = priorityHandler.getPreviousPriority(actDecreasingPriority);
		if (previousPrio < 0) {
			return -1;
		}
		actSheddingFactor = 100;
		actDecreasingPriority = previousPrio;
		if (actDecreasingPriority < actIncreasingPriority) {
			actIncreasingPriority = actDecreasingPriority;
		}
		return getDecreasingPriorityQueryIDEqually();
	}

	@Override
	public String getComponentName() {
		return NAME;
	}

}
