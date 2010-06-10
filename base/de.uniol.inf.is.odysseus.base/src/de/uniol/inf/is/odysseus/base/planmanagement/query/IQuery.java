package de.uniol.inf.is.odysseus.base.planmanagement.query;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.base.usermanagement.User;

/**
 * Describes an object which represents a basic query in odyessus. This
 * interface is used as an extern view on a query. Editing these queries should
 * delimited.
 * 
 * It has a unique ID and consists of a logical plan, a physical plan, a
 * priority and a execution state.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IQuery extends
		IReoptimizeRequester<AbstractQueryReoptimizeRule>,
		IReoptimizeHandler<IQueryReoptimizeListener> {
	/**
	 * ID of this query. Should be unique.
	 * 
	 * @return ID of this query. Should be unique.
	 */
	public int getID();

	/**
	 * Indicates if this query will be scheduled or not.
	 * 
	 * @return TRUE: This query will be scheduled. FALSE: else
	 */
	public boolean isRunning();

	/**
	 * Returns the logical plan of this query.
	 * 
	 * TODO: getSealedLogicalPlan -> getLogicalPlan: problem getLogicalPlan is
	 * used by {@link IEditableQuery}.
	 * 
	 * @return The logical plan of this query.
	 */
	public ILogicalOperator getSealedLogicalPlan();

	/**
	 * Returns the physical plan of this query.
	 * 
	 * TODO: getSealedRoot -> getRoot: problem getRoot is used by
	 * {@link IEditableQuery}.
	 * 
	 * @return The physical plan of this query.
	 */
	public IPhysicalOperator getSealedRoot();

	/**
	 * Returns the priority with which this query should be scheduled.
	 * 
	 * @return The priority with which this query should be scheduled.
	 */
	public int getPriority();

	/**
	 * Sets the priority with which this query should be scheduled.
	 * 
	 * @param priority
	 *            The new priority with which this query should be scheduled.
	 */
	public void setPriority(int priority);
	
	public String getParserId();
	
	public String getQueryText();
	public void setQueryText(String queryText);
	
	public User getUser();
	public void setUser(User user);
}
