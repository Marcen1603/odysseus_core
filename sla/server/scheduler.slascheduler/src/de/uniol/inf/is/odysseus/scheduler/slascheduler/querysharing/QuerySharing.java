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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;

/**
 * Standard implementation of {@link IQuerySharing} Attention: It is expected,
 * that every partial plan contains only ONE query!
 */
public class QuerySharing implements IQuerySharing {
	/**
	 * map of priorities for each query calculated by the standard
	 * prioritization algorithm of the scheduler
	 */
	private Map<IPhysicalQuery, Double> priorities;
	/**
	 * set of all scheduling units managed in this class
	 */
	private Set<IScheduling> plans;
	/**
	 * mapping of query pairs to its (statically calculated) effort in case of
	 * query sharing
	 */
	private Map<Pair<IPhysicalQuery, IPhysicalQuery>, Double> effortMap;
	/**
	 * reference to the cost model used in calculation of effort
	 */
	private IQuerySharingCostModel costModel;

	/**
	 * creates a new {@link QuerySharing} object
	 * 
	 * @param costModelName
	 *            the name of the cost model that should be used in effort
	 *            calculation
	 */
	public QuerySharing(String costModelName) {
		this.priorities = new HashMap<IPhysicalQuery, Double>();
		this.plans = new HashSet<IScheduling>();
		this.effortMap = new HashMap<Pair<IPhysicalQuery, IPhysicalQuery>, Double>();
		this.costModel = new QuerySharingCostModelFactory()
				.buildCostModel(costModelName);
	}

	@Override
	public void setPriority(IScheduling plan, double priority) {
		this.priorities.put(plan.getPlan(), new Double(priority));
		this.plans.add(plan);
	}

	/**
	 * calculates the effort rates gained of shared queries and saves them in an
	 * internal map
	 * 
	 * @param plans
	 *            list of all scheduling units that managed by the scheduler
	 */
	@Override
	public void refreshEffortTable(List<IScheduling> plans) {
		this.effortMap.clear();
		for (IScheduling plan : plans) {
			IPhysicalQuery query = plan.getPlan();
			for (IScheduling otherPlan : plans) {
				IPhysicalQuery otherQuery = otherPlan.getPlan();
				// check if plan and otherPlan share operators. iff true
				// calculate effort rate of the sharing between these queries
				// and add result to map
				IPhysicalQuery participating = plan.getPlan();

				if (plan != otherPlan && participating == otherPlan) {
					// calc effort:
					// determine shared operators
					Set<IPhysicalOperator> sharedOps = query
							.getSharedOperators(otherQuery);

					// determine cost of each operator (shared and unshared!)
					Set<IPhysicalOperator> allOps = otherQuery
							.getAllOperators();

					double sharedCostSum = 0.0;
					double allCostSum = 0.0;

					for (IPhysicalOperator op : allOps) {
						double cost = this.costModel.getOperatorCost(op);
						allCostSum += cost;
						if (sharedOps.contains(op)) {
							sharedCostSum += cost;
						}
					}

					// calculate cost-based effort rate
					double effortRate = sharedCostSum / allCostSum;

					// save effort rate in map
					Pair<IPhysicalQuery, IPhysicalQuery> key = new Pair<IPhysicalQuery, IPhysicalQuery>(
							query, otherQuery);
					this.effortMap.put(key, effortRate);
				}
			}

		}
	}

	/**
	 * returns the best plan considering the effort of query sharing
	 */
	@Override
	public IScheduling getNextPlan() {
		IScheduling nextPlan = null;
		double nextPrio = 0.0;

		for (IScheduling plan : this.plans) {
			IPhysicalQuery pp = plan.getPlan();
			double tempPrio = this.priorities.get(pp);
			if (tempPrio > nextPrio) {
				nextPrio = tempPrio;
				nextPlan = plan;
			}
		}

		return nextPlan;
	}


}
