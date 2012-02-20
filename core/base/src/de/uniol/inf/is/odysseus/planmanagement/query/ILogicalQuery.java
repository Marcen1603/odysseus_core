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
package de.uniol.inf.is.odysseus.planmanagement.query;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.serialize.ISerializable;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * Describes an object which represents a basic query in odysseus.
 * 
 * It has a unique ID and consists of a logical plan, a physical plan, a
 * priority and a execution state.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public interface ILogicalQuery extends IOperatorOwner, Serializable, ISerializable, Comparable<ILogicalQuery>, IProvidesSLA{

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

	public ISession getUser();

	public void setUser(ISession user);

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
	 * Returns the {@link QueryBuildConfiguration} of this query.
	 * 
	 * @return The {@link QueryBuildConfiguration} of this query.
	 */
	public QueryBuildConfiguration getBuildParameter();

	public void setBuildParameter(String name, QueryBuildConfiguration parameter);



	/**
	 * @return true if this plan contains cycles, typically the graph is cycle
	 *         free
	 */
	public boolean containsCycles();

	public void addPenalty(double penalty);

	double getPenalty();
	
	String getBuildConfigName();
	
}
