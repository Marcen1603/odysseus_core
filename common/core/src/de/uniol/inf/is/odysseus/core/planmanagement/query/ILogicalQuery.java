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
package de.uniol.inf.is.odysseus.core.planmanagement.query;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Describes an object which represents a basic query in odysseus.
 *
 * It has a unique ID and consists of a logical plan, a physical plan, a
 * priority and a execution state.
 *
 * @author Wolf Bauer, Marco Grawunder
 *
 */
public interface ILogicalQuery extends IOperatorOwner, Serializable,
		Comparable<IOperatorOwner>, IQuery {

	/**
	 * ID of this query. Should be unique.
	 *
	 * @return ID of this query. Should be unique.
	 */
	@Override
	int getID();

	/**
	 * A query can be named, retrieve the value
	 *
	 * @return
	 */
	Resource getName();

	/**
	 * A query can be named, set the value
	 *
	 * @return
	 */
	void setName(Resource resource);

	/**
	 * Returns the priority with which this query should be scheduled.
	 *
	 * @return The priority with which this query should be scheduled.
	 */
	int getPriority();

	/**
	 * Sets the priority with which this query should be scheduled.
	 *
	 * @param priority
	 *            The new priority with which this query should be scheduled.
	 */
	void setPriority(int priority);

	@Override
	String getParserId();

	void setParserId(String parserId);

	@Override
	String getQueryText();

	void setQueryText(String queryText);

	ISession getUser();

	void setUser(ISession user);

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
	void setLogicalPlan(ILogicalPlan logicalPlan, boolean setOwner);

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
	void setLogicalPlan(ILogicalOperator logicalPlan, boolean setOwner);

	/**
	 * Returns the logical plan of this query.
	 *
	 * @return The logical plan of this query.
	 */
	ILogicalPlan getLogicalPlan();

	/**
	 * Set a list of alternative logical plans for this query.
	 *
	 * @param altPlans
	 *            List of alternative logical plans for this query.
	 *
	 *            TODO: setOwner checken!
	 */
	void setAlternativeLogicalPlans(List<ILogicalPlan> altPlans);

	/**
	 * Returns the list of alternative logical plans for this query.
	 *
	 * @return The list of alternative logical plans for this query.
	 */
	List<ILogicalPlan> getAlternativeLogicalPlans();

	/**
	 * @return true if this plan contains cycles, typically the graph is cycle
	 *         free
	 */
	boolean containsCycles();

	/**
	 * Store key values pairs
	 *
	 * @param key
	 * @param value
	 */
	void setParameter(String key, Object value);

	/**
	 * Retrieve value for key
	 *
	 * @param key
	 * @return
	 */
	Object getParameter(String key);

	/**
	 * Parameter that are set by the user (e.g. with Odysseus script command QPARAM)
	 * @param key
	 * @param value
	 */
	void setUserParameter(String key, String value);

	/**
	 * Retrieve user defined parameters
	 * @param key
	 * @return
	 */
	String getUserParameter(String key);

	/**
	 * Store key values pairs
	 *
	 * @param key
	 * @param value
	 */
	void setServerParameter(String key, Object value);

	/**
	 * Retrieve value for key
	 *
	 * @param key
	 * @return
	 */
	Object getServerParameter(String key);

	/**
	 * Notice for additional information (e.g. stopped by runtime)
	 */
	void setNotice(String notice);

	String getNotice();

	ILogicalPlan getInitialLogicalPlan();

	void setInitialLogicalPlan(ILogicalPlan initialPlan);

}
