package de.uniol.inf.is.odysseus.base.planmanagement.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;

/**
 * Describes an object which represents a basic global plan in odyessus. This
 * interface is used as an intern view on a global plan. Editing this global
 * plan should not be delimited.
 * 
 * It consist of a set of queries and is the central storage in odysseus.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IEditablePlan extends IPlan {
	/**
	 * Adds a new query to the global plan.
	 * 
	 * @param query
	 *            The query which should be added.
	 * @return TRUE: The query is added. FALSE: else
	 */
	public boolean addQuery(IEditableQuery query);

	/**
	 * Returns a modifiable query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the searched modifiable query
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IEditableQuery getQuery(int queryID);

	/**
	 * Returns a query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the query to remove.
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IEditableQuery removeQuery(int queryID);

	/**
	 * Returns a list of all registered modifiable queries.
	 * 
	 * @return A list of all registered modifiable queries.
	 */
	public ArrayList<IEditableQuery> getEdittableQueries();
}
