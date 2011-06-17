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
		// determine index of current service level
		int slIndex = this.getCurrentServiceLevelIndex(conformance, sla);
		
		/* 
		 * calculate oc if current service level is not the less valuable 
		 * service level, return 0 otherwise
		 */
		if (slIndex < sla.getServiceLevel().size()) {
			// calculate lower bound
			double lowerbound = this.calcLowerbound(slIndex, sla);
			
			// calculate upper bound
			double upperbound = this.calcUpperbound(slIndex, sla);
			
			// calculate delta to lower service level
			double delta = this.calcDelta(slIndex, sla);
			
			// calculate relative position in service level:
			double temp = (lowerbound - conformance) / (lowerbound - upperbound);
			// square relative position 
			temp = temp * temp;
			// multiply cost with relative position
			return (int)(temp * delta);
		} else {
			return 0;
		}
		
	}

	@Override
	public int mg(int conformance, SLA sla) {
		// determine index of current service level
		int slIndex = this.getCurrentServiceLevelIndex(conformance, sla);
		
		/* 
		 * calculate mg if current service level is not the most valuable 
		 * service level, return 0 otherwise
		 */
		if (slIndex != 0) {
			// calculate lower bound
			double lowerbound = this.calcLowerbound(slIndex, sla);
			
			// calculate upper bound
			double upperbound = this.calcUpperbound(slIndex, sla);
			
			// calculate delta to higher service level
			double delta = this.calcDelta(slIndex - 1, sla);
			
			// calculate relative position in service level
			double temp = (conformance - lowerbound) / (upperbound - lowerbound);
			// square relative position
			temp = temp * temp;
			// multiply with delta
			return (int)(temp * delta);
		} else {
			return 0;
		}
	}
	
	/**
	 * returns the index of the current held service level
	 * @param conformance
	 * @param sla
	 * @return
	 */
	private int getCurrentServiceLevelIndex(int conformance, SLA sla) {
		for (int i = 0; i < sla.getServiceLevel().size(); i++) {
			if (conformance < (Integer)sla.getServiceLevel().get(i).getThreshold()) {
				return i;
			}
		}
		return sla.getServiceLevel().size();
	}
	
	/**
	 * calculates delta (cost differences to next less valuable service level) 
	 * for a given service level
	 * @param slIndex the index of the current service level
	 * @param sla the sla containing service level definitions
	 * @return
	 */
	private double calcDelta(int slIndex, SLA sla) {
		if (slIndex != 0) {
			return sla.getServiceLevel().get(slIndex).getPenalty().getCost() 
				- sla.getServiceLevel().get(slIndex - 1).getPenalty().getCost();
		} else {
			/*
			 * if best service level is held, delta is defined as the
			 * costs for violating the held service level
			 */
			return sla.getServiceLevel().get(slIndex).getPenalty().getCost();
		}
	}
	
	private double calcUpperbound(int slIndex, SLA sla) {
		if (slIndex == 0) {
			// upperbound is 0 if highest service level is held
			return 0.0;
		} else {
			return (Integer)sla.getServiceLevel().get(slIndex-1).getThreshold();
			// upperbound remains 0 if highest service level is held
		}
	}
	
	private double calcLowerbound(int slIndex, SLA sla) {
		if (slIndex == sla.getServiceLevel().size()) {
			/* 
			 * if less valuable service level is held the lowerbound
			 * is infinite (max possible latency)
			 */
			return Double.MAX_VALUE;
		} else {
			return (Integer)sla.getServiceLevel().get(slIndex).getThreshold();
		}
	}

}
