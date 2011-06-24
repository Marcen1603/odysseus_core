package de.uniol.inf.is.odysseus.scheduler.slascheduler.cost;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;

/**
 * this is a quadratic cost function for metric latency and scope average
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class QuadraticCFLatency implements ICostFunction {

	/**
	 * returns the index of the current held service level of a partial plan its
	 * given conformance and the related sla
	 * 
	 * @param conformance
	 *            the conformance
	 * @param sla
	 *            the sla
	 * @return the index of the currenly held service level
	 */
	protected int getCurrentServiceLevelIndex(double conformance, SLA sla) {
		for (int i = 0; i < sla.getServiceLevel().size(); i++) {
			if (conformance < (Integer) sla.getServiceLevel().get(i)
					.getThreshold()) {
				return i;
			}
		}
		return sla.getServiceLevel().size();
	}

	/**
	 * calculates delta (cost differences to next less valuable service level)
	 * for a given service level
	 * 
	 * @param slIndex
	 *            the index of the current service level
	 * @param sla
	 *            the sla containing service level definitions
	 * @return the cost difference to the next worse service level of the given
	 *         service level index
	 */
	protected double calcDelta(int slIndex, SLA sla) {
		if (slIndex != 0) {
			return sla.getServiceLevel().get(slIndex).getPenalty().getCost()
					- sla.getServiceLevel().get(slIndex - 1).getPenalty()
							.getCost();
		} else {
			/*
			 * if best service level is held, delta is defined as the costs for
			 * violating the held service level
			 */
			return sla.getServiceLevel().get(slIndex).getPenalty().getCost();
		}
	}

	/**
	 * returns the border value to the next higher service level
	 * 
	 * @param slIndex
	 *            the index of the service level whichs upper bound should be
	 *            calculated
	 * @param sla
	 *            the sla where the service levels are defined in
	 * @return the upper bound of the given service level
	 */
	protected double calcUpperbound(int slIndex, SLA sla) {
		if (slIndex == 0) {
			// upperbound is 0 if highest service level is held
			return 0.0;
		} else {
			return (Integer) sla.getServiceLevel().get(slIndex - 1)
					.getThreshold();
			// upperbound remains 0 if highest service level is held
		}
	}

	/**
	 * returns the border value to the next lower service level
	 * 
	 * @param slIndex
	 *            the index of the service level whichs upper bound should be
	 *            calculated
	 * @param sla
	 *            the sla where the service levels are defined in
	 * @return the lower bound of the given service level
	 */
	protected double calcLowerbound(int slIndex, SLA sla) {
		if (slIndex == sla.getServiceLevel().size()) {
			/*
			 * if less valuable service level is held the lowerbound is infinite
			 * (max possible latency)
			 */
			return Double.MAX_VALUE;
		} else {
			return (Integer) sla.getServiceLevel().get(slIndex).getThreshold();
		}
	}

}
