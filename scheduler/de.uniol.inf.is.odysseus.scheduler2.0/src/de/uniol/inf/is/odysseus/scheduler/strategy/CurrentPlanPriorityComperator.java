package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.Comparator;

public class CurrentPlanPriorityComperator implements Comparator<IScheduling> {

	@Override
	public int compare(IScheduling p1, IScheduling p2) {
		if (p1.getPlan().getCurrentPriority() < p2.getPlan().getCurrentPriority()){
			return -1;
		}
		if (p1.getPlan().getCurrentPriority() > p2.getPlan().getCurrentPriority()){
			return 1;
		}
		return 0;
	}

}
