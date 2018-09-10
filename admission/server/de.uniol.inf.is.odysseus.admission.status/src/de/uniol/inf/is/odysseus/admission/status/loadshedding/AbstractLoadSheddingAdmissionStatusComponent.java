package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Parent class of the different load shedding status components.
 */
public abstract class AbstractLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	/**
	 * The ISession superUser is used to get access to the execution plan of the queries.
	 */
	static private final ISession superUser = SessionManagement.instance.loginSuperUser(null);

	/**
	 * Contains all allowed queries with their maximal shedding factor.
	 */
	protected volatile Map<Integer, Integer> allowedQueries = new HashMap<Integer, Integer>();

	/**
	 * Contains all queries with active load shedding and their actual shedding
	 * factor.
	 */
	protected volatile Map<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();

	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	protected volatile List<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
	/**
	 * This list contains all queries which load shedding was randomly activated.
	 */
	protected volatile List<Integer> simpleActiveQueries = new ArrayList<>();

	/**
	 * The Default Constructor adds the object of this class to the LoadSheddingAdmissionStatusRegistry.
	 */
	public AbstractLoadSheddingAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}

	@Override
	public boolean addQuery(int queryID) {
		IPhysicalQuery physQuery = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser)
				.getQueryById(queryID, superUser);
		ILogicalQuery logQuery = physQuery.getLogicalQuery();
		
		//Check if load shedding is allowed.
		String o = logQuery.getUserParameter("LOADSHEDDINGENABLED");
		if (o != null) {
			boolean enabled = Boolean.parseBoolean(o);
			if (enabled) {
				
				//Check if a maximal shedding factor was defined.
				String factor = logQuery.getUserParameter("maxSheddingFactor");
				if (factor != null) {
					int maxSheddingFactor = Integer.parseInt(factor);
					
					if (maxSheddingFactor > 0) {
						//Load shedding is allowed and the maximal shedding factor is greater 0.
						allowedQueries.put(queryID, maxSheddingFactor);
						
						//This status component is active.
						LoadSheddingAdmissionStatusRegistry.setActive(true);
						return true;
					} else {
						//Load shedding is allowed, but the maximal shedding factor is 0.
						return false;
					}
				} else {
					// Load shedding is allowed but no maximal shedding factor was defined
					// so the default shedding factor is used.
					allowedQueries.put(queryID, LoadSheddingAdmissionStatusRegistry.getDefaultMaxSheddingFactor());
					
					//This status component is active.
					LoadSheddingAdmissionStatusRegistry.setActive(true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeQuery(int queryID) {
		//The given query is removed from all lists and maps.
		if (allowedQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(Integer.valueOf(queryID));
			}
			if (activeQueries.containsKey(queryID)) {
				activeQueries.remove(queryID);
			}
			if (simpleActiveQueries.contains(queryID)) {
				simpleActiveQueries.remove(Integer.valueOf(queryID));
			}
			allowedQueries.remove(queryID);
			
			if (allowedQueries.isEmpty()) {
				//If no query allows load shedding, this status component is inactive.
				LoadSheddingAdmissionStatusRegistry.setActive(false);
			}
			return true;

		}
		return false;
	}
	
	/**
	 * Increases the factor of the given query.
	 * @param queryID
	 */
	protected void increaseFactor(int queryID) {
		int sheddingFactor;
		int maxSheddingFactor = allowedQueries.get(queryID);
		
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
	
	/**
	 * Decreases the shedding factor of the given query.
	 * @param queryID
	 */
	protected void decreaseFactor(int queryID) {
		if (activeQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(Integer.valueOf(queryID));
			}
			
			//The new shedding factor.
			int sheddingFactor = activeQueries.get(queryID) - LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
			if (sheddingFactor <= 0) {
				//The load shedding will be deactivated.
				sheddingFactor = 0;
				activeQueries.remove(queryID);
				if (simpleActiveQueries.contains(queryID)) {
					simpleActiveQueries.remove(Integer.valueOf(queryID));
				}
			} else {
				activeQueries.replace(queryID, sheddingFactor);
			}
			
			setSheddingFactor(queryID, sheddingFactor);
		}
	}

	/**
	 * Returns true if a query can increase its shedding factor.
	 * @return boolean
	 */
	protected boolean isSheddingPossible() {
		if (allowedQueries.isEmpty()) {
			return false;
		}
		if (allowedQueries.keySet().size() <= maxSheddingQueries.size()) {
			return false;
		}
		return true;
	}

	/**
	 * Sets the shedding factor of the given query to the given factor.
	 * @param queryID
	 * @param factor
	 */
	protected void setSheddingFactor(int queryID, int factor) {
		if (AdmissionStatusPlugIn.getServerExecutor().getQueryState(queryID, superUser) != QueryState.INACTIVE) {
			AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, factor, superUser);
		}
		LoggerFactory.getLogger(getClass()).info("Query " + queryID + " has factor: " + factor);
	}
	
	/**
	 * Gets the physical query from the given queryID.
	 * @param queryID
	 * @return IPhysicalQuery
	 */
	protected IPhysicalQuery getQueryByID(int queryID) {
		return AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
	}
	
	/**
	 * Adds the given query to a list.
	 * @param queryID
	 */
	protected void addToSimpleQueries(int queryID) {
		if (!activeQueries.containsKey(queryID)) {
			simpleActiveQueries.add(Integer.valueOf(queryID));
		}
	}
	
	/**
	 * Removes the given query from a list.
	 * @param queryID
	 */
	protected void removeFromSimpleQueries(int queryID) {
		if (simpleActiveQueries.contains(queryID)) {
			simpleActiveQueries.remove(Integer.valueOf(queryID));
		}
	}
	
	/**
	 * All queries are removed from the given list, which have already their maximal shedding factor.
	 * @param List<IPhysicalQuery>
	 * @return New list without queries, which hav their maximal shedding factor.
	 */
	protected List<IPhysicalQuery> removeQueriesWithMaxSheddingFactor(List<IPhysicalQuery> list) {
		Iterator<IPhysicalQuery> iter = list.iterator();

		while (iter.hasNext()) {
			IPhysicalQuery query = iter.next();
			if (maxSheddingQueries.contains(query.getID())) {
				iter.remove();
			}
		}
		return list;
	}
	
	/**
	 * Returns the query with the greatest rank from the given map.
	 * @param queryRanks - Map<Integer, Integer>
	 * @return queryID
	 */
	protected int getQueryWithGreatestRank(Map<Integer, Integer> queryRanks) {
		if (queryRanks.isEmpty()) {
			return -1;
		}
		
		int partialQuery = -1;
		int rank = -1;
		for (int queryID : queryRanks.keySet()) {
			if(queryRanks.get(queryID) > rank) {
				partialQuery = queryID;
				rank = queryRanks.get(queryID);
			}
		}
		
		return partialQuery;
	}
}
