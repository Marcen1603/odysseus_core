package de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.collection.Pair;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.PartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

/**
 * Standard implementation of {@link IQuerySharing} Attention: It is expected,
 * that every partial plan contains only ONE query!
 */
public class QuerySharing implements IQuerySharing {
	/**
	 * map of priorities for each query calculated by the standard
	 * prioritization algorithm of the scheduler
	 */
	private Map<IQuery, Double> priorities;
	/**
	 * set of all scheduling units managed in this class
	 */
	private Set<IScheduling> plans;
	/**
	 * mapping of query pairs to its (statically calculated) effort in case of
	 * query sharing
	 */
	private Map<Pair<IQuery, IQuery>, Double> effortMap;
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
		this.priorities = new HashMap<IQuery, Double>();
		this.plans = new HashSet<IScheduling>();
		this.effortMap = new HashMap<Pair<IQuery, IQuery>, Double>();
		this.costModel = new QuerySharingCostModelFactory()
				.buildCostModel(costModelName);
	}

	@Override
	public void setPriority(IScheduling plan, double priority) {
		this.priorities.put(plan.getPlan().getQueries().get(0), new Double(
				priority));
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
			IQuery query = plan.getPlan().getQueries().get(0);
			for (IScheduling otherPlan : plans) {
				IQuery otherQuery = otherPlan.getPlan().getQueries().get(0);
				// check if plan and otherPlan share operators. iff true
				// calculate effort rate of the sharing between these queries
				// and add result to map
				Set<IQuery> participating = ((PartialPlan) plan)
						.getParticpatingQueries();

				if (plan != otherPlan && participating.contains(otherPlan)) {
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
					Pair<IQuery, IQuery> key = new Pair<IQuery, IQuery>(query,
							otherQuery);
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
			PartialPlan pp = (PartialPlan) plan.getPlan();
			double tempPrio = this.calcPriority(pp.getQueries().get(0),
					pp.getParticpatingQueries(), this.priorities.get(pp));
			if (tempPrio > nextPrio) {
				nextPrio = tempPrio;
				nextPlan = plan;
			}
		}

		return nextPlan;
	}

	/**
	 * calculates the priority of a given query considering the effort of query
	 * sharing
	 * 
	 * @param query
	 *            the query
	 * @param participatingQueries
	 *            set of queries, that share operators with the given query
	 * @param prio
	 *            the priority of the given query calculated by the standard
	 *            algorithm
	 * @return the priority under consideration of query sharing effort
	 */
	private double calcPriority(IQuery query, Set<IQuery> participatingQueries,
			double prio) {
		double newPrio = prio;
		// lookup all query sharing effort rates, multiply rates with prios
		// and add result to newPrio
		for (IQuery otherQuery : participatingQueries) {
			if (!query.equals(otherQuery)) {
				Pair<IQuery, IQuery> key = new Pair<IQuery, IQuery>(query,
						otherQuery);
				newPrio += this.effortMap.get(key)
						* this.priorities.get(otherQuery);
			}
		}

		return newPrio;
	}

}
