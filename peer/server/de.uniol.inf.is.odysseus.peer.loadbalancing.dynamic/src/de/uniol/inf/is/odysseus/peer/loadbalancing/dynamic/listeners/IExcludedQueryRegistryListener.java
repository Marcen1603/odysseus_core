package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners;

import java.util.List;

/**
 * Interface to implement Listeners for Excluded Query Registry.
 * @author Carsten Cordes
 *
 */
public interface IExcludedQueryRegistryListener {

	/**
	 * Called when excluded Queries are added to or removed from Registry.
	 * @param queryIDs Queries that are currently in excluded Queries Registry. 
	 */
	public void notifyExcludedQueriesChanged(List<Integer> queryIDs);
}
