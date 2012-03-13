package de.uniol.inf.is.odysseus.scheduler.slascheduler.cost;

import de.uniol.inf.is.odysseus.core.sla.SLA;

/**
 * this is a quadratic cost function for metric latency and scope number
 * 
 * @author Thomas Vogelgesang
 *
 */
public class QuadraticCFLatencyNumber extends QuadraticCFLatency {

	/**
	 * calculates opportunity costs for metric latency with scope number by
	 * quadratic cost function
	 */
	@Override
	public double oc(double conformance, SLA sla) {
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
			double temp = (upperbound - conformance) / (upperbound - lowerbound);
			// square relative position 
			temp = temp * temp;
			// multiply cost with relative position
			return temp * delta;
		}
        return sla.getMaxPenalty() * (conformance - this.calcUpperbound(slIndex, sla));
		
	}

	/**
	 * calculates marginal gain for metric latency with scope number by
	 * quadratic cost function
	 */
	@Override
	public double mg(double conformance, SLA sla) {
		/* 
		 * in scope number there is no chance to return to a better service
		 * level
		 */
		return 0;
	}

}
