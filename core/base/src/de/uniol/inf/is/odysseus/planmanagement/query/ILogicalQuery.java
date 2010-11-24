package de.uniol.inf.is.odysseus.planmanagement.query;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface ILogicalQuery extends
		IReoptimizeHandler<IQueryReoptimizeListener>,
		IReoptimizeRequester<AbstractQueryReoptimizeRule>, IOperatorOwner {

	/**
	 * ID of this query. Should be unique.
	 * 
	 * @return ID of this query. Should be unique.
	 */
	@Override
	public int getID();

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

	public void setParserId(String parserId);

	public String getQueryText();

	public void setQueryText(String queryText);

	public User getUser();

	public void setUser(User user);

	/**
	 * Set the logical plan of this query.
	 * 
	 * @param logicalPlan
	 *            The new logical plan of this query
	 * @setOwner: Sets all connected operators in the logical plan as owned by
	 * this query. Attention: If there are operators that are connected but not part
	 * of this query, owner need to be set manually! 
	 * @throws IllegalArgumentException if setOwner is false and no owners are set
	 */
	public void setLogicalPlan(ILogicalOperator logicalPlan, boolean setOwner);

	/**
	 * Returns the logical plan of this query.
	 * 
	 * @return The logical plan of this query.
	 */
	public ILogicalOperator getLogicalPlan();

	/**
	 * Removes the ownership of this query and the registered child operators.
	 * After this method this query has no relationship to any operator.
	 */
	public void removeOwnerschip();
	
	/**
	 * Returns the {@link QueryBuildConfiguration} of this query.
	 * 
	 * @return The {@link QueryBuildConfiguration} of this query.
	 */
	public QueryBuildConfiguration getBuildParameter();

	public void setBuildParameter(QueryBuildConfiguration parameter);

}
