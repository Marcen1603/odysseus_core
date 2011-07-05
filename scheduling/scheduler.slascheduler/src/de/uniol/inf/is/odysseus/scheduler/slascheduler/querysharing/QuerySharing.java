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
 * Expecting 1 partial plan == 1 Query !!!
 */
public class QuerySharing implements IQuerySharing {

	private Map<IQuery, Double> priorities;
	private Set<IScheduling> plans;

	private Map<Pair<IQuery, IQuery>, Double> effortMap;

	private IQuerySharingCostModel costModel;

	public QuerySharing(String costModelName) {
		this.priorities = new HashMap<IQuery, Double>();
		this.plans = new HashSet<IScheduling>();
		this.effortMap = new HashMap<Pair<IQuery, IQuery>, Double>();
		this.costModel = new QuerySharingCostModelFactory().buildCostModel(costModelName);
	}

	@Override
	public void setPriority(IScheduling plan, double priority) {
		this.priorities.put(plan.getPlan().getQueries().get(0), new Double(priority));
		this.plans.add(plan);
	}

	/**
	 * calculates the effort rates gained of shared queries and saves them in
	 * an internal map
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
					Set<IPhysicalOperator> sharedOps = query.getSharedOperators(otherQuery);
					
					// determine cost of each operator (shared and unshared!)
					Set<IPhysicalOperator> allOps = otherQuery.getAllOperators();
					
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
	
	@Override
	public IScheduling getNextPlan() {
		IScheduling nextPlan = null;
		double nextPrio = 0.0;

		for (IScheduling plan : this.plans) {
			PartialPlan pp = (PartialPlan)plan.getPlan();
			double tempPrio = this.calcPriority(pp.getQueries().get(0), 
					pp.getParticpatingQueries(), this.priorities.get(pp));
			if (tempPrio > nextPrio) {
				nextPrio = tempPrio;
				nextPlan = plan;
			}
		}

		return nextPlan;
	}

	private double calcPriority(IQuery query, Set<IQuery> participatingQueries, 
			double prio) {
		double newPrio = prio;
		// lookup all query sharing effort rates, multiply rates with prios
		// and add result to newPrio
		for (IQuery otherQuery : participatingQueries) {
			if (!query.equals(otherQuery)) {
				Pair<IQuery, IQuery> key = new Pair<IQuery, IQuery>(query, 
						otherQuery);
				newPrio += this.effortMap.get(key) * this.priorities.get(otherQuery);
			}
		}
		
		return newPrio;
	}

}
