package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.LoggerFactory;


/**
 * Provides the status for simple load shedding in consideration of priorities.
 * 
 * Only the CPU load is used in this status component.
 */
public class SimpleLoadSheddingWPAdmissionStatusComponent extends AbstractLoadSheddingAdmissionStatusComponent {

	/**
	 * The name of this status component.
	 */
	private final String NAME = "simpleWP";
	
	/**
	 * The priorityHandler is used for the correct handling of priorities.
	 */
	private volatile PriorityHandler priorityHandler = new PriorityHandler();
	
	//These variables save the actual priority.
	private volatile int actIncreasingPriority = -1;
	private volatile int actDecreasingPriority = -1;
	
	//This is used in the separately strategy.
	private volatile int actSheddingQuery = -1;
	
	//This is used in the equally strategy.
	private volatile int actSheddingFactor = 0;
	
	@Override
	public boolean addQuery(int queryID) {
		if (super.addQuery(queryID)) {
			//The query is added to the priority handling.
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
			//The query is removed from the priority handling.
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
		//Nothing to measure.
	}

	@Override
	public void runLoadShedding() {
		if(!isSheddingPossible()) {
			return;
		}
		int queryID = getSheddingQueryID();
		LoggerFactory.getLogger(getClass()).info("" + queryID);
		if (queryID < 0) {
			return;
		}
		increaseFactor(queryID);
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
		decreaseFactor(queryID);
	}
	
	/**
	 * Returns a query, which should increase its shedding factor, depending on the selected load shedding strategy.
	 * @return queryID
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
	
	/**
	 * Returns a query, which should decrease its shedding factor, depending on the selected load shedding strategy.
	 * @return queryID
	 */
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
	 * This method is used by the default strategy.
	 * @return queryID
	 */
	private int getIncreasingPriorityQueryIDDefault() {
		List<Integer> list = new ArrayList<>();
		//All queries with the actual priority are added to the list, if they have not reached
		//their maximal shedding factor.
		for (int queryID : priorityHandler.getQueriesByPriority(actIncreasingPriority)) {
			if (!maxSheddingQueries.contains(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			//The next greater priority has to be checked.
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
	
	/**
	 * Method to find a query with minimal priority, which has not its maximal shedding factor.
	 * This method is used by the separately strategy.
	 * @return queryID
	 */
	private int getIncreasingPriorityQueryIDSeparately() {
		if (actSheddingQuery > -1 && !maxSheddingQueries.contains(actSheddingQuery)) {
			return actSheddingQuery;
		} else {
			actSheddingQuery = getIncreasingPriorityQueryIDDefault();
			return actSheddingQuery;
		}
	}
	
	/**
	 * Recursive method to find a query with active load shedding and the maximal priority.
	 * This method is used by the default strategy.
	 * @return queryID
	 */
	private int getDecreasingPriorityQueryIDDefault() {
		List<Integer> list = new ArrayList<>();
		//All queries with the actual priority are added to the list, if they have active load shedding.
		for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
			if (activeQueries.containsKey(queryID)) {
				list.add(Integer.valueOf(queryID));
			}
		}
		if (!list.isEmpty()) {
			Collections.shuffle(list);
			return list.get(0);
		} else {
			//The next smaller priority has to checked.
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
	
	/**
	 * Method to find a query with active load shedding and the maximal priority.
	 * This method is used by the default strategy.
	 * @return queryID
	 */
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
	 * This method is used in the equally strategy.
	 * @return queryID
	 */
	private int getIncreasingPriorityQueryIDEqually() {	
		if (actSheddingFactor <= 0) {
			actSheddingFactor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		while (actSheddingFactor <= 100) {
			List<Integer> possibleList = new ArrayList<>();
			//All queries with the actual priority are added to the list, if they have not reached
			//their maximal shedding factor and their shedding factor is smaller than the actual shedding factor.
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
				//No query with this priority has a smaller shedding factor than the actual shedding factor.
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
		
		//No query in the actual priority can increase its shedding factor so the next priority has to be checked.
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
	 * Recursive method to find the query with active load shedding and the maximal priority.
	 * This method is used in the equally strategy.
	 * @return queryID
	 */ 
	private int getDecreasingPriorityQueryIDEqually() {
		while (actSheddingFactor >= 0) {
			List<Integer> list = new ArrayList<>();
			//All queries with the actual priority are added to the list, if they have active load shedding.
			for (int queryID : priorityHandler.getQueriesByPriority(actDecreasingPriority)) {
				if (activeQueries.containsKey(queryID) && activeQueries.get(queryID) >= actSheddingFactor) {
					list.add(Integer.valueOf(queryID));
				}
			}
			if (!list.isEmpty()) {
				Collections.shuffle(list);
				return list.get(0);
			} else {
				//No query with this priority has a greater shedding factor than the actual shedding factor.
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
		
		//The next smaller priority has to checked.
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
