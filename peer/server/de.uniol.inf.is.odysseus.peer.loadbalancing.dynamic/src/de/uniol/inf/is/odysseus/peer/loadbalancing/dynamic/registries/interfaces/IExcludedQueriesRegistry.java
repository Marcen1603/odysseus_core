package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.IExcludedQueryRegistryListener;

/**
 * Registry for excluded Queries (Queries that should not be redistributed by load balancing)
 * @author Carsten Cordes
 *
 */
public interface IExcludedQueriesRegistry {

	/**
	 * Excludes Query from load balancing by adding it to registry.
	 * @param queryID Query ID from Query to add.
	 */
	public void excludeQueryIdFromLoadBalancing(int queryID);
	
	/**
	 * Removes Query id from Registry thus making it available to load balancing.
	 * @param queryID Query ID to remove.
	 */
	public void removeExcludedQueryID(int queryID);
	
	/**
	 * Checks if Query is excluded from Load Balancing (and in Registry).
	 * @param queryID Query ID to look for
	 * @return true if QueryID is excluded.
	 */
	public boolean isQueryIDExcludedFromLoadBalancing(int queryID);
	

	/**
	 * Adds listener 
	 * @param listener Listener to add.
	 */
	public void addListener(IExcludedQueryRegistryListener listener);
	/**
	 * Removes listener
	 * @param listener listener to remove.
	 */
	public void removeListener(IExcludedQueryRegistryListener listener);

}
