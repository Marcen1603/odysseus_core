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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * This class is used to keep information about queries and 
 * partial plans together
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
	 * List of all parts of this execution plan. Used for scheduling.
	 */
	final List<IPartialPlan> partialPlans;

	/**
	 * List of all parts of this execution plan. Used for scheduling.
	 */
	final List<IPartialPlan> partialPlansNotToSchedule;

	/**
	 * List of all leaf sources that need to be scheduled periodically.
	 */
	final List<IIterableSource<?>> leafSources;

	private Set<IPhysicalOperator> roots = null;

	/**
	 * Map of all registered queries.
	 */
	private Map<Integer, IPhysicalQuery> queries;

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
		partialPlans = new ArrayList<IPartialPlan>();
		partialPlansNotToSchedule = new ArrayList<IPartialPlan>();
		leafSources = new ArrayList<IIterableSource<?>>();
		queries = Collections
				.synchronizedMap(new HashMap<Integer, IPhysicalQuery>());
	}

	private ExecutionPlan(ExecutionPlan otherPlan) {
		this.open = otherPlan.open;
		this.leafSources = new ArrayList<IIterableSource<?>>(
				otherPlan.leafSources);
		this.partialPlans = new ArrayList<IPartialPlan>(otherPlan.partialPlans);
		this.partialPlansNotToSchedule = new ArrayList<IPartialPlan>(
				otherPlan.partialPlans);
		if (otherPlan.roots != null) {
			this.roots = new HashSet<IPhysicalOperator>(otherPlan.roots);
		}
		this.queries = Collections
				.synchronizedMap(new HashMap<Integer, IPhysicalQuery>(
						otherPlan.queries));
		this.reoptimizeListener.addAll(otherPlan.reoptimizeListener);
		this.reoptimizeRule.addAll(otherPlan.reoptimizeRule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan#
	 * getPartialPlans()
	 */
	@Override
	public List<IPartialPlan> getPartialPlans() {
		return Collections.unmodifiableList(this.partialPlans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan#getSources
	 * ()
	 */
	@Override
	public List<IIterableSource<?>> getLeafSources() {
		return Collections.unmodifiableList(this.leafSources);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan
	 * #setPartialPlans(java.util.List)
	 */
	@Override
	public void setPartialPlans(List<IPartialPlan> patialPlans) {
		this.open = false;
		this.partialPlans.clear();
		this.partialPlansNotToSchedule.clear();
		for (IPartialPlan plan : patialPlans) {
			if (plan.hasIteratableSources()) {
				this.partialPlans.add(plan);
			} else {
				this.partialPlansNotToSchedule.add(plan);
			}
		}
		updateRoots();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.plan.IExecutionPlan
	 * #setSources(java.util.List)
	 */
	@Override
	public void setLeafSources(List<IIterableSource<?>> leafSources) {
		this.open = false;
		this.leafSources.clear();
		this.leafSources.addAll(leafSources);
	}


	@Override
	public Set<IPhysicalOperator> getRoots() {
		if (roots == null) {
			updateRoots();
		}
		return Collections.unmodifiableSet(roots);
	}

	private void updateRoots() {
		roots = new HashSet<IPhysicalOperator>();
		for (IPartialPlan partialPlan : this.partialPlans) {
			roots.addAll(partialPlan.getQueryRoots());
		}
		for (IPartialPlan partialPlan : this.partialPlansNotToSchedule) {
			roots.addAll(partialPlan.getQueryRoots());
		}
	}

	@Override
	public IExecutionPlan clone() {
		return new ExecutionPlan(this);
	}

	@Override
	public synchronized boolean addQuery(IPhysicalQuery query) {
		this.queries.put(query.getID(), query);

		return true;
	}
	
	@Override
	public void addQueries(List<IPhysicalQuery> allQueries) {
		for (IPhysicalQuery q:allQueries){
			this.queries.put(q.getID(), q);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan#removeQuery(int)
	 */
	@Override
	public synchronized IPhysicalQuery removeQuery(int queryID) {
		return this.queries.remove(queryID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan#getQuery(int)
	 */
	@Override
	public synchronized IPhysicalQuery getQuery(int queryID) {
		return this.queries.get(queryID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlan#getQueries()
	 */
	@Override
	public synchronized Collection<IPhysicalQuery> getQueries() {
		return Collections.unmodifiableCollection(this.queries.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester#reoptimize()
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeHandler#
	 * addReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void addReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			if (!this.reoptimizeListener.contains(reoptimizationListener)) {
				this.reoptimizeListener.add(reoptimizationListener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeHandler#
	 * removeReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void removeReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			this.reoptimizeListener.remove(reoptimizationListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester#addReoptimzeRule
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.IReoptimizeRequester#
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
