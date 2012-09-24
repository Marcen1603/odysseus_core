/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.plan;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public interface IExecutionPlan extends IClone, IReoptimizeRequester<AbstractPlanReoptimizeRule>,
IReoptimizeHandler<IPlanReoptimizeListener> {

	public void setLeafSources(List<IIterableSource<?>> leafSources);

	public List<IIterableSource<?>> getLeafSources();

	public void setPartialPlans(List<IPartialPlan> patialPlans);

	public List<IPartialPlan> getPartialPlans();

	/**
	 * Returns a set of all registered roots. The size can be different to the
	 * count of registered queries because a root could be used by more then one
	 * query.
	 * 
	 * @return A list of all registered roots.
	 */
	public Set<IPhysicalOperator> getRoots();

	@Override
	public IExecutionPlan clone();

	/**
	 * Returns a list of all registered queries.
	 * 
	 * @return A list of all registered queries.
	 */
	public Collection<IPhysicalQuery> getQueries();

	/**
	 * Adds a new query to the global plan.
	 * 
	 * @param query
	 *            The query which should be added.
	 * @return TRUE: The query is added. FALSE: else
	 */
	public boolean addQuery(IPhysicalQuery query);

	/**
	 * Add the queries to the global plan
	 * 
	 * @param allQueries
	 */
	void addQueries(List<IPhysicalQuery> allQueries);

	
	/**
	 * Returns a modifiable query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the searched modifiable query
	 * @return The query with the defined ID or null if no query is found.
	 *
	 * @deprecated use getQueryById
	 */
	@Deprecated
	public IPhysicalQuery getQuery(int queryID);
	
	
	/**
	 * Returns a modifiable query with the defined ID.
	 * 
	 * @param queryID  ID of the searched modifiable query
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IPhysicalQuery getQueryById(int queryID);
	

	/**
	 * Returns a query with the defined name.
	 * 
	 * @param name name of the searched query
	 *            
	 * @return The query with the name or null if no query is found.
	 */
	public IPhysicalQuery getQueryByName(String name);

	/**
	 * Returns a query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the query to remove.
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IPhysicalQuery removeQuery(int queryID);


}
