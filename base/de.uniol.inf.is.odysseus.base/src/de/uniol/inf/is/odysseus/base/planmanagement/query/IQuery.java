package de.uniol.inf.is.odysseus.base.planmanagement.query;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.base.usermanagement.User;

/**
 * Describes an object which represents a basic query in odyessus.
 * 
 * It has a unique ID and consists of a logical plan, a physical plan, a
 * priority and a execution state.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IQuery extends
		IReoptimizeRequester<AbstractQueryReoptimizeRule>,
		IReoptimizeHandler<IQueryReoptimizeListener>, IOperatorOwner{
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
	 * @param newLogicalAlgebra
	 *            The new logical plan of this query.
	 */
	public void setLogicalPlan(ILogicalOperator newLogicalAlgebra);

	/**
	 * Returns the logical plan of this query.
	 * 
	 * @return The logical plan of this query.
	 */
	public ILogicalOperator getLogicalPlan();

	/**
	 * The method must be called for each of the physical roots of a query.
	 * Usually there is only one, but sometimes like in object tracking,
	 * there maybe more than one root in a query.
	 * 
	 * Initializes the physical plan of this query. Should be used if a new plan
	 * is set. This method also opens the physical plan and sets the owner
	 * relationship between the query and the operators.
	 * 
	 * The new physical plan is stored as the initial physical plan and is set
	 * as the current active physical root.
	 * 
	 * @param roots
	 *            The roots of this Query.
	 * @throws OpenFailedException
	 *             An {@link Exception} occurs during opening an opertaor.
	 */
	public void initializePhysicalRoots(List<IPhysicalOperator> roots)
			throws OpenFailedException;

	/**
	 * Sets the current physical plan which will be executed. Owner relationship
	 * between the query and the operators are not affected. The physical plan
	 * could be modified by inner operations. If the physical plan should be
	 * used after setting it use the return value oder {@link #getRoot()}.
	 * 
	 * This method should be only used if special optimizations are processed.
	 * For initial setting the physical plan use
	 * {@link #initializePhysicalPlan(IPhysicalOperator)}.
	 * 
	 * @param root
	 *            The new root of the physical plan.
	 * @return The new physical plan of this query.
	 * @throws OpenFailedException
	 *             An {@link Exception} occurred during opening an opertaor.
	 */
	public List<IPhysicalOperator> setRoots(List<IPhysicalOperator> root)
			throws OpenFailedException;

	/**
	 * Returns the physical plan of this query.
	 * 
	 * @return The physical plan of this query.
	 */
	public List<IPhysicalOperator> getRoots();

	/**
	 * Returns the direct physical children which are necessary for the
	 * execution of this query.
	 * 
	 * TODO: rename to getPhysicalChilds()?
	 * 
	 * @return The direct physical children which are necessary for the
	 *         execution of this query.
	 */
	public List<IPhysicalOperator> getIntialPhysicalPlan();

	/**
	 * Removes the ownership of this query and the registered child operators.
	 * After this method this query has no relationship to any operator.
	 */
	public void removeOwnerschip();

	/**
	 * Returns the {@link QueryBuildParameter} of this query.
	 * 
	 * @return The {@link QueryBuildParameter} of this query.
	 */
	public QueryBuildParameter getBuildParameter();

	public void setBuildParameter(QueryBuildParameter parameter);
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl#stop()
	 */
	@Override
	public void stop();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl#start()
	 */
	@Override
	public void start();
	
}
