package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SimpleDynamicPriorityPlanScheduling extends
		AbstractDynamicPriorityPlanScheduling {

	final int minPrio;

	public SimpleDynamicPriorityPlanScheduling(int minPrio) {
		this.minPrio = minPrio;

	}

	public SimpleDynamicPriorityPlanScheduling(
			SimpleDynamicPriorityPlanScheduling dynamicPriorityPlanScheduling) {
		super(dynamicPriorityPlanScheduling);
		minPrio = dynamicPriorityPlanScheduling.minPrio;
	}

	@Override
	public SimpleDynamicPriorityPlanScheduling clone() {
		return new SimpleDynamicPriorityPlanScheduling(this);
	}
	
	@Override
	protected void updatePriorities(IScheduling current) {
		int currentPriority = current.getPlan().getCurrentPriority();
		int newPrio = currentPriority - 1;
		if (newPrio < minPrio) {
			newPrio = current.getPlan().getBasePriority();
		}
		if (newPrio != currentPriority) {
			updatePriorityCurrent(current, newPrio);
		}
	}

}
