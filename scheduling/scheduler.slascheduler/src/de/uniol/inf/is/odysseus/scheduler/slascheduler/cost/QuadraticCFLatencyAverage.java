package de.uniol.inf.is.odysseus.scheduler.slascheduler.cost;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

/**
 * this is a quadratic cost function for metric latency and scope average
 * 
 * @author Thomas Vogelgesang
 *
 */
public class QuadraticCFLatencyAverage extends QuadraticCFLatency {

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
	
}
