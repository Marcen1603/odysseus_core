package de.uniol.inf.is.odysseus.planmanagement.query;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * Describes an object which represents a basic query in odyessus.
 * 
 * It has a unique ID and consists of a logical plan, a physical plan, a
 * priority and a execution state.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public interface IQuery extends IReoptimizeHandler<IQueryReoptimizeListener>,
		IReoptimizeRequester<AbstractQueryReoptimizeRule>, IOperatorOwner,
		IMonitoringDataProvider {

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
	 *            this query. Attention: If there are operators that are
	 *            connected but not part of this query, owner need to be set
	 *            manually!
	 * @throws IllegalArgumentException
	 *             if setOwner is false and no owners are set
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

	/**
	 * Indicates if this query will be scheduled or not.
	 * 
	 * @return TRUE: This query will be scheduled. FALSE: else
	 */
	@Override
	public boolean isActive();

	/**
	 * The method must be called for each of the physical roots of a query.
	 * Usually there is only one, but sometimes like in object tracking, there
	 * maybe more than one root in a query.
	 * 
	 * Initializes the physical plan of this query. Should be used if a new plan
	 * is set.
	 * 
	 * The new physical plan is stored as the initial physical plan and is set
	 * as the current active physical root.
	 * 
	 * @param roots
	 *            The roots of this Query.
	 */
	public void initializePhysicalRoots(List<IPhysicalOperator> roots);

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
	 */
	public List<IPhysicalOperator> setRoots(List<IPhysicalOperator> root);

	/**
	 * Returns the physical plan of this query.
	 * 
	 * @return The physical plan of this query.
	 */
	public List<IPhysicalOperator> getRoots();

	/**
	 * Returns the direct physical children ( i.e. all physical operators of
	 * this query) which are necessary for the execution of this query.
	 * 
	 * @return The direct physical children which are necessary for the
	 *         execution of this query.
	 */
	public List<IPhysicalOperator> getPhysicalChilds();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.IOperatorControl#stop()
	 */
	@Override
	public void stop();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.planmanagement.IOperatorControl#start()
	 */
	@Override
	public void start();

	public boolean isOpened();

	/**
	 * @return true if this plan contains cycles, typically the graph is cycle
	 *         free
	 */
	public boolean containsCycles();

	/**
	 * Set Monitor for plans
	 * 
	 * @param planMonitor
	 */
	@SuppressWarnings("rawtypes")
	public void addPlanMonitor(String name, IPlanMonitor planMonitor);

	/**
	 * Get Monitor for plans
	 * 
	 * @param planMonitor
	 */
	@SuppressWarnings("rawtypes")
	public IPlanMonitor getPlanMonitor(String name);

	/**
	 * Get List of all plan monitors
	 */
	@SuppressWarnings("rawtypes")
	public Collection<IPlanMonitor> getPlanMonitors();

	public void addPenalty(double penalty);

	double getPenalty();
}
