package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

public class PrioAvg implements IPriorityFunction {

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
