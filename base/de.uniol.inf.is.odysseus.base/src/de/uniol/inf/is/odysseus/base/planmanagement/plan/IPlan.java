package de.uniol.inf.is.odysseus.base.planmanagement.plan;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;

/**
 * Describes an object which represents a basic global plan in odyessus. This
 * interface is used as an extern view on a global plan. Editing this plan
 * should delimited.
 * 
 * It consist of a set of queries and is the central storage in odysseus.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlan extends
		IReoptimizeRequester<AbstractPlanReoptimizeRule>,
		IReoptimizeHandler<IPlanReoptimizeListener> {
	/**
	 * Returns a query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the searched query
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IQuery getQuery(int queryID);

	/**
	 * Returns a list of all registered queries.
	 * 
	 * @return A list of all registered queries.
	 */
	public ArrayList<IQuery> getQueries();

	/**
	 * Returns a list of all registered roots. The size can be different to the
	 * count of registered queries because a root could be used by more then one
	 * query.
	 * 
	 * @return A list of all registered roots.
	 */
	public ArrayList<IPhysicalOperator> getRoots();
}
