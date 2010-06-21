package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

public class CurrentPlanPriorityComperator implements Comparator<IPartialPlan> {

	@Override
	public int compare(IPartialPlan p1, IPartialPlan p2) {
		if (p1.getCurrentPriority() < p2.getCurrentPriority()){
			return -1;
		}
		if (p1.getCurrentPriority() > p2.getCurrentPriority()){
			return 1;
		}
		return 0;
	}

}
