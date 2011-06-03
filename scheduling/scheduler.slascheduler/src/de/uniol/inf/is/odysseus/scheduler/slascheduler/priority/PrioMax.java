package de.uniol.inf.is.odysseus.scheduler.slascheduler.priority;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.IPriorityFunction;

public class PrioMax implements IPriorityFunction {

	@Override
	public int calcPriority(int... cost) {
		int max = 0;
		for (int c : cost) {
			if (c > max)
				max = c;
		}
		return max;
	}

}
