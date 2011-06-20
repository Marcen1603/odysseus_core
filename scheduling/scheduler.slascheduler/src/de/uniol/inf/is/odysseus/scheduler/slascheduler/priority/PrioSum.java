package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

public class PrioSum implements IPriorityFunction {

	@Override
	public double calcPriority(double... cost) {
		double sum = 0;
		for (double c : cost) {
			sum += c;
		}
		return sum;
	}

}
