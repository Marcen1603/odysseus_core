package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

public class PrioMax implements IPriorityFunction {

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
