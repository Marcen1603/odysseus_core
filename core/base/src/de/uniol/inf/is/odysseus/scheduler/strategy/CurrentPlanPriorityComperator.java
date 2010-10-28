package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.Comparator;

public class CurrentPlanPriorityComperator implements Comparator<IScheduling> {

	@Override
	public int compare(IScheduling p1, IScheduling p2) {
		long p1Priority = p1.getPlan().getCurrentPriority();
		long p2priority = p2.getPlan().getCurrentPriority();
		if (p1Priority < p2priority){
			return 1;
		}
		if (p1Priority > p2priority){
			return -1;
		}
		return 0;
	}

}
