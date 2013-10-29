/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.planmanagement.query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IPhysicalQuery extends IMonitoringDataProvider, IReoptimizeHandler<IQueryReoptimizeListener>,
IReoptimizeRequester<AbstractQueryReoptimizeRule>, IOperatorOwner {

	String getName();
	
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
	 * Methods for scheduling 
	 */
	
	public List<IIterableSource<?>> getIterableSources();
	public boolean hasIteratableSources();
	public IIterableSource<?> getIterableSource(int id);
	public int getSourceId(IIterableSource<?> source);
	public List<IIterableSource<?>> getIteratableLeafSources();
	public List<IPhysicalOperator> getLeafSources();

	/**
	 * Removes the ownership of this query and the registered child operators.
	 * After this method this query has no relationship to any operator.
	 */
	public void removeOwnerschip();
	
	public boolean isOpened();
	
	public Set<IPhysicalOperator> getSharedOperators(IPhysicalQuery otherQuery);
	
	public Set<IPhysicalOperator> getAllOperators();

	void open(IQueryStarter queryListener) throws OpenFailedException;

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
	public IPlanMonitor<?> getPlanMonitor(String name);

	/**
	 * Get List of all plan monitors
	 */
	public Collection<IPlanMonitor<?>> getPlanMonitors();

	ISession getSession();
	void setSession(ISession user);
	
	boolean isOwner(ISession session);

	void addChild(IPhysicalOperator child);

	public void replaceOperator(IPhysicalOperator op1, IPhysicalOperator op2);
	public void replaceRoot(IPhysicalOperator op1, IPhysicalOperator op2);

	public int getPriority();
	
	public long getCurrentPriority();
	public void setCurrentPriority(long newPriority);
	public long getBasePriority();

	public boolean containsCycles();

	public ILogicalQuery getLogicalQuery();	
	
	/**
	 * Store key values pairs
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, Object value);

	/**
	 * Retrieve value for key
	 * @param name
	 * @return
	 */
	public Object getParameter(String name);
	
	public void setLogicalQuery(ILogicalQuery q);

	/**
	 * For whenever there might be a discrepancy between the logical query's ID and the one to expect from creating a new physical one.
	 * Mainly when creating the Physical Query based on a physical plan instead of a logical query
	 * (the latter takes the logical query's id automatically)
	 * @param q the logical query to be associated with this physical query
	 */
	public void setLogicalQueryAndAdoptItsID(ILogicalQuery q);

}
