package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Interface for all admission monitors.
 */
public interface IAdmissionMonitor {
	
	/**
	 * Adds the query to the admission monitor for measurements.
	 * @param query
	 */
	public void addQuery(IPhysicalQuery query);
	
	/**
	 * Removes the query from the admission monitor.
	 * @param query
	 */
	public void removeQuery(IPhysicalQuery query);
	
	/**
	 * Returns a list with all queries, which have an increasing tendency.
	 * @return List<IPhysicalQuery>
	 */
	public List<IPhysicalQuery> getQueriesWithIncreasingTendency();
}
