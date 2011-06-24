package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

/**
 * average based priority function
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class PrioAvg implements IPriorityFunction {

	/**
	 * calculates the priority of a partial plan as the average of the given
	 * costs
	 */
	@Override
	public double calcPriority(double... cost) {
		int count = 0;
		double sum = 0;
		for (double c : cost) {
			sum += c;
			count++;
		}
		return sum / count;
	}

}
