package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

public class PrioSum implements IPriorityFunction {

	@Override
	public int calcPriority(int... cost) {
		int sum = 0;
		for (int c : cost) {
			sum += c;
		}
		return sum;
	}

}
