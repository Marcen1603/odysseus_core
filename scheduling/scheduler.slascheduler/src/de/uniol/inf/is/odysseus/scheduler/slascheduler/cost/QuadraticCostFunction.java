package de.uniol.inf.is.odysseus.scheduler.slascheduler.cost;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;

/**
 * this is a quadratic cost function for metric latency and scope average
 * 
 * @author Thomas Vogelgesang
 *
 */
public class QuadraticCostFunction implements ICostFunction {

	@Override
	public int oc(int conformance, SLA sla) {
		double upperbound = 0;
		double lowerbound = 0;
		double delta = 0.0;
		int cost = 0;
		
		// determine upper bound and lower bound of current service level
		for (int i = 0; i < sla.getServiceLevel().size(); i++) {
			if (conformance > (Integer)sla.getServiceLevel().get(i).getThreshold()) {
				if (i == sla.getServiceLevel().size() - 1) {
					/* 
					 * if the less valuable service level is violated, the
					 * opportunity costs are defined as 0
					 */
					return 0;
				}
				lowerbound = (Integer)sla.getServiceLevel().get(i).getThreshold();
				if (i != 0) {
					upperbound = (Integer)sla.getServiceLevel().get(i-1).getThreshold();
					// upperbound remains 0 if highest service level is held
					// calculate delta
					delta = sla.getServiceLevel().get(i).getPenalty().getCost() 
						- sla.getServiceLevel().get(i).getPenalty().getCost();
				} else {
					/*
					 * if best service level is held, delta is defined as the
					 * costs for violating the held service level
					 */
					delta = sla.getServiceLevel().get(i).getPenalty().getCost();
				}
			}
		}
		
		// calculate costs
		// calculate relative position in service level:
		double temp = (lowerbound - conformance) / (lowerbound - upperbound);
		// square relative position 
		temp = temp * temp;
		// multiply cost with relative position
		cost = (int)(temp * delta);
		
		return cost;
	}

	@Override
	public int mg(int conformance, SLA sla) {
		// TODO not implemented yet
		return 0;
	}

}
