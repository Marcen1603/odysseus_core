package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

/**
 * maximum based priority function
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class PrioMax implements IPriorityFunction {

	/**
	 * calculates the priority of a partial plan as the maximum of the given
	 * costs
	 */
	@Override
	public double calcPriority(double... cost) {
		double max = 0;
		for (double c : cost) {
			if (c > max)
				max = c;
		}
		return max;
	}

}
