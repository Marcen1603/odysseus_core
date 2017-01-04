package de.uniol.inf.is.odysseus.admission.status;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Interface for all admission monitors.
 * 
 * @author Jannes
 *
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
	 * Measures something depending on the admission monitor.
	 */
	public void updateMeasurements();
	
	/**
	 * Returns a list with all queries, which have an increasing tendency.
	 * @return
	 */
	public ArrayList<IPhysicalQuery> getQuerysWithIncreasingTendency();
}
