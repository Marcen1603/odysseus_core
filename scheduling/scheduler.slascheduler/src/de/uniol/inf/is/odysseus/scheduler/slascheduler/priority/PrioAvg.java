package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

public class PrioAvg implements IPriorityFunction {

	@Override
	public int calcPriority(int... cost) {
		int count = 0;
		int sum = 0;
		for (int c : cost) {
			sum += c;
			count++;
		}
		return sum / count;
	}

}
