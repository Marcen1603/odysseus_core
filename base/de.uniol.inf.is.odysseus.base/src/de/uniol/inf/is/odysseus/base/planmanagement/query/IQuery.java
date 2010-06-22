package de.uniol.inf.is.odysseus.base.planmanagement.query;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.base.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
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
	 * Returns the logical plan of this query.
	 * 
	 * TODO: getSealedLogicalPlan -> getLogicalPlan: problem getLogicalPlan is
	 * used by {@link IQuery}.
	 * 
	 * @return The logical plan of this query.
	 */
	public ILogicalOperator getSealedLogicalPlan();

	/**
	 * Returns the physical plan of this query.
	 * 
	 * TODO: getSealedRoot -> getRoot: problem getRoot is used by
	 * {@link IQuery}.
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
	 * Initializes the physical plan of this query. Should be used if a new plan
	 * is set. This method also opens the physical plan and sets the owner
	 * relationship between the query and the operators.
	 * 
	 * The new physical plan is stored as the initial physical plan and is set
	 * as the current active physical root.
	 * 
	 * @param physicalChilds
	 *            The new physical plan of this Query.
	 * @throws OpenFailedException
	 *             An {@link Exception} occurs during opening an opertaor.
	 */
	public void initializePhysicalPlan(IPhysicalOperator physicalChilds)
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
	public IPhysicalOperator setRoot(IPhysicalOperator root)
			throws OpenFailedException;

	/**
	 * Returns the physical plan of this query.
	 * 
	 * @return The physical plan of this query.
	 */
	public IPhysicalOperator getRoot();

	/**
	 * Sets the physical children of this query. These children are the physical
	 * operators which are necessary for the execution of this query. It also
	 * sets the owner relationship between the query and the operators.
	 * 
	 * This method should be only used if special optimizations are processed.
	 * For initial setting the physical plan use
	 * {@link #initializePhysicalPlan(IPhysicalOperator)}.
	 * 
	 * @param physicalChilds
	 *            Physical operators which are necessary for the execution of
	 *            this query.
	 */
	public void setPhysicalChilds(ArrayList<IPhysicalOperator> physicalChilds);

	/**
	 * Returns the direct physical children which are necessary for the
	 * execution of this query.
	 * 
	 * TODO: rename to getPhysicalChilds()?
	 * 
	 * @return The direct physical children which are necessary for the
	 *         execution of this query.
	 */
	public ArrayList<IPhysicalOperator> getIntialPhysicalPlan();

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
