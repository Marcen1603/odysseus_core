package de.uniol.inf.is.odysseus.planmanagement.query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public interface IPhysicalQuery extends IMonitoringDataProvider, IReoptimizeHandler<IQueryReoptimizeListener>,
IReoptimizeRequester<AbstractQueryReoptimizeRule>, IOperatorOwner, IProvidesSLA {

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

	/**
	 * Removes the ownership of this query and the registered child operators.
	 * After this method this query has no relationship to any operator.
	 */
	public void removeOwnerschip();
	
	public boolean isOpened();
	
	public Set<IPhysicalOperator> getSharedOperators(IPhysicalQuery otherQuery);
	
	public Set<IPhysicalOperator> getAllOperators();

	void open() throws OpenFailedException;

	void close();

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

	ISession getUser();
	void setUser(ISession user);

	public QueryBuildConfiguration getBuildParameter();

	void addChild(IPhysicalOperator child);

	public void replaceOperator(IPhysicalOperator op1, IPhysicalOperator op2);
	public void replaceRoot(IPhysicalOperator op1, IPhysicalOperator op2);

	public int getPriority();

	public boolean containsCycles();

	public ILogicalQuery getLogicalQuery();	
}
