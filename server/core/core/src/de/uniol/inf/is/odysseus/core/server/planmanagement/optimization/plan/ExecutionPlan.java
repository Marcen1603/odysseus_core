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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This class is used to keep information about queries and partial plans
 * together
 *
 * @author Wolf Bauer, Marco Grawunder
 *
 */
public class ExecutionPlan implements IExecutionPlan {

	static Logger _logger = null;

	static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ExecutionPlan.class);
		}
		return _logger;
	}

	/**
	 * Describes if the physical operators are opened.
	 */
	private boolean open = false;

	/**
	 * List of all leaf sources that need to be scheduled periodically.
	 */
	final List<IIterableSource<?>> leafSources;

	/**
	 * Map of all registered queries.
	 */
	final private Map<Integer, IPhysicalQuery> queries;
	final private Map<Resource, IPhysicalQuery> namedQueries;

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private List<IPlanReoptimizeListener> reoptimizeListener = Collections
			.synchronizedList(new ArrayList<IPlanReoptimizeListener>());

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private List<AbstractPlanReoptimizeRule> reoptimizeRule = Collections
			.synchronizedList(new ArrayList<AbstractPlanReoptimizeRule>());

	public ExecutionPlan() {
		leafSources = new ArrayList<IIterableSource<?>>();
		queries = Collections.synchronizedMap(new HashMap<Integer, IPhysicalQuery>());
		namedQueries = Collections.synchronizedMap(new HashMap<Resource, IPhysicalQuery>());
	}

	private ExecutionPlan(ExecutionPlan otherPlan) {
		this.open = otherPlan.open;
		this.leafSources = new ArrayList<IIterableSource<?>>(otherPlan.leafSources);
		this.queries = Collections.synchronizedMap(new HashMap<Integer, IPhysicalQuery>(otherPlan.queries));
		this.namedQueries = Collections.synchronizedMap(new HashMap<Resource, IPhysicalQuery>(otherPlan.namedQueries));
		this.reoptimizeListener.addAll(otherPlan.reoptimizeListener);
		this.reoptimizeRule.addAll(otherPlan.reoptimizeRule);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan
	 * #getSources ()
	 */
	@Override
	public List<IIterableSource<?>> getLeafSources(ISession session) {
		return Collections.unmodifiableList(this.leafSources);
	}

	@Override
	public boolean isEmpty() {
		return this.queries == null || this.queries.size() == 0;
	}

	private void updateLeafSources() {
		this.open = false;
		this.leafSources.clear();
		for (IPhysicalQuery query : this.queries.values()) {
			this.leafSources.addAll(query.getIteratableLeafSources());
		}
	}

	@Override
	public Set<IPhysicalOperator> getRoots(ISession session) {
		HashSet<IPhysicalOperator> roots = new HashSet<IPhysicalOperator>();
		for (IPhysicalQuery q : getQueries(session)) {
			roots.addAll(q.getRoots());
		}
		return Collections.unmodifiableSet(roots);
	}

	@Override
	public IExecutionPlan clone() {
		return new ExecutionPlan(this);
	}

	@Override
	public synchronized boolean addQuery(IPhysicalQuery query, ISession session) {
		return addQuery(query, session, true);
	}

	private synchronized boolean addQuery(IPhysicalQuery query, ISession session, boolean updateLeafSources) {
		if (this.queries.containsKey(query.getID())) {
			_logger.error("Query id {} already set!", query.getID());
		}
		this.queries.put(query.getID(), query);
		if (query.getName() != null) {
			this.namedQueries.put(query.getName(), query);
		}
		if (updateLeafSources) {
			updateLeafSources();
		}
		return true;
	}

	@Override
	public void addQueries(List<IPhysicalQuery> allQueries, ISession session) {
		for (IPhysicalQuery q : allQueries) {
			addQuery(q, session, false);
		}
		updateLeafSources();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan#
	 * removeQuery (int)
	 */
	@Override
	public synchronized IPhysicalQuery removeQuery(int queryID, ISession session) {
		IPhysicalQuery toRemove = this.queries.remove(queryID);
		if (toRemove == null){
			return null;
		}

		ExecutorPermission.validateUserRight(toRemove, session, ExecutorPermission.REMOVE_QUERY);

		if (toRemove != null) {
			namedQueries.remove(toRemove.getName());
		}
		updateLeafSources();
		return toRemove;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan#getQuery
	 * (int)
	 */
	@Override
	public synchronized IPhysicalQuery getQueryById(int queryID, ISession session) {
		IPhysicalQuery toReturn = this.queries.get(queryID);
		if (toReturn != null) {
			ExecutorPermission.validateUserRight(toReturn, session, ExecutorPermission.GET_QUERY);
		}
		return toReturn;
	}

	@Override
	public IPhysicalQuery getQueryByName(Resource name, ISession session) {
		IPhysicalQuery toReturn = this.namedQueries.get(name);
		if (toReturn != null) {
			ExecutorPermission.validateUserRight(toReturn, session, ExecutorPermission.GET_QUERY);
		}
		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan#getQueries
	 * ()
	 */
	@Override
	public synchronized Collection<IPhysicalQuery> getQueries(ISession session) {
		// User can have right to get all queries
		if (ExecutorPermission.hasUserRight(session, ExecutorPermission.GET_ALL_QUERIES)) {
			return Collections.unmodifiableCollection(this.queries.values());
		}
		// else return only queries, where user is owner or has access right
		List<IPhysicalQuery> queries = new ArrayList<>();
		for (IPhysicalQuery q : this.queries.values()) {
			if (ExecutorPermission.hasUserRight(session, ExecutorPermission.GET_QUERY)) {
				queries.add(q);
			}
		}
		return queries;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester
	 * #reoptimize()
	 */
	@Override
	public void reoptimize() {
		for (IPlanReoptimizeListener reoptimizationListener : this.reoptimizeListener) {
			reoptimizationListener.reoptimizeRequest(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeHandler#
	 * addReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void addReoptimizeListener(IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			if (!this.reoptimizeListener.contains(reoptimizationListener)) {
				this.reoptimizeListener.add(reoptimizationListener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeHandler#
	 * removeReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void removeReoptimizeListener(IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			this.reoptimizeListener.remove(reoptimizationListener);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester
	 * #addReoptimzeRule
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void addReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			if (!this.reoptimizeRule.contains(reoptimizeRule)) {
				this.reoptimizeRule.add(reoptimizeRule);
				reoptimizeRule.addReoptimieRequester(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester#
	 * removeReoptimzeRule
	 * (de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void removeReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			this.reoptimizeRule.remove(reoptimizeRule);
			reoptimizeRule.deinitialize();
		}
	}

}
