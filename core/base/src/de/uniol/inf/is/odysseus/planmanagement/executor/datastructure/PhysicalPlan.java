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
package de.uniol.inf.is.odysseus.planmanagement.executor.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPhysicalPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

/**
 * Plan represents a map of all registered queries. 
 * 
 * @author Wolf Bauer, Marco Grawunder
 *
 */
public class PhysicalPlan implements IPhysicalPlan {

	/**
	 * Map of all registered queries.
	 */
	private Map<Integer, IPhysicalQuery> queries;

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private List<IPlanReoptimizeListener> reoptimizeListener = Collections.synchronizedList(new ArrayList<IPlanReoptimizeListener>());

	/**
	 * List of objects which respond to reoptimize requests.
	 */
	private List<AbstractPlanReoptimizeRule> reoptimizeRule = Collections.synchronizedList(new ArrayList<AbstractPlanReoptimizeRule>());

	/**
	 * Creates a new Plan.
	 */
	public PhysicalPlan() {
		queries = Collections.synchronizedMap(new HashMap<Integer, IPhysicalQuery>());
	}


	@Override
	public synchronized boolean addQuery(IPhysicalQuery query) {
		this.queries.put(query.getID(), query);

		return true;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.plan.IPlan#removeQuery(int)
	 */
	@Override
	public synchronized IPhysicalQuery removeQuery(int queryID) {
		return this.queries.remove(queryID);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.plan.IPlan#getQuery(int)
	 */
	@Override
	public synchronized IPhysicalQuery getQuery(int queryID) {
		return this.queries.get(queryID);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.plan.IPlan#getQueries()
	 */
	@Override
	public synchronized Collection<IPhysicalQuery> getQueries() {
		return Collections.unmodifiableCollection(this.queries.values());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester#reoptimize()
	 */
	@Override
	public void reoptimize() {
		for (IPlanReoptimizeListener reoptimizationListener : this.reoptimizeListener) {
			reoptimizationListener.reoptimizeRequest(this);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler#addReoptimizeListener(java.lang.Object)
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeHandler#removeReoptimizeListener(java.lang.Object)
	 */
	@Override
	public void removeReoptimizeListener(
			IPlanReoptimizeListener reoptimizationListener) {
		synchronized (this.reoptimizeListener) {
			this.reoptimizeListener.remove(reoptimizationListener);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester#addReoptimzeRule(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule)
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester#removeReoptimzeRule(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule)
	 */
	@Override
	public void removeReoptimzeRule(AbstractPlanReoptimizeRule reoptimizeRule) {
		synchronized (this.reoptimizeRule) {
			this.reoptimizeRule.remove(reoptimizeRule);
			reoptimizeRule.deinitialize();
		}
	}


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.plan.IPlan#getRoots()
	 */
	@Override
	public List<IPhysicalOperator> getRoots() {
		List<IPhysicalOperator> roots = new ArrayList<IPhysicalOperator>();

		for (IPhysicalQuery query : getQueries()) {
			for(IPhysicalOperator curRoot: query.getRoots()){
				if (!roots.contains(curRoot)) {
					roots.add(curRoot);
				}
			}
		}

		return roots;
	}
}
