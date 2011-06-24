package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

/**
 * sum based priority function
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class PrioSum implements IPriorityFunction {

	/**
	 * calculates the priority of a partial plan as the sum of the given costs
	 */
	@Override
	public double calcPriority(double... cost) {
		double sum = 0;
		for (double c : cost) {
			sum += c;
		}
		return sum;
	}

}
