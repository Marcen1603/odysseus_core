/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.planmanagement.plan;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler;
import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

/**
 * Describes an object which represents a basic global plan in odysseus. This
 * interface is used as an extern view on a global plan. Editing this plan
 * should delimited.
 * 
 * It consist of a set of queries and is the central storage in odysseus.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPhysicalPlan extends
		IReoptimizeRequester<AbstractPlanReoptimizeRule>,
		IReoptimizeHandler<IPlanReoptimizeListener> {

	/**
	 * Returns a list of all registered queries.
	 * 
	 * @return A list of all registered queries.
	 */
	public Collection<IPhysicalQuery> getQueries();

	/**
	 * Returns a list of all registered roots. The size can be different to the
	 * count of registered queries because a root could be used by more then one
	 * query.
	 * 
	 * @return A list of all registered roots.
	 */
	public List<IPhysicalOperator> getRoots();
	
	/**
	 * Adds a new query to the global plan.
	 * 
	 * @param query
	 *            The query which should be added.
	 * @return TRUE: The query is added. FALSE: else
	 */
	public boolean addQuery(IPhysicalQuery query);

	/**
	 * Returns a modifiable query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the searched modifiable query
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IPhysicalQuery getQuery(int queryID);

	/**
	 * Returns a query with the defined ID.
	 * 
	 * @param queryID
	 *            ID of the query to remove.
	 * @return The query with the defined ID or null if no query is found.
	 */
	public IPhysicalQuery removeQuery(int queryID);

}
