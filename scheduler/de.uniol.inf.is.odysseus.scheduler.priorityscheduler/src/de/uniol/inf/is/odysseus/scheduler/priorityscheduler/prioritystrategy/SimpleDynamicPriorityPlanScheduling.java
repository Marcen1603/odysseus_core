package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SimpleDynamicPriorityPlanScheduling extends
		AbstractPriorityPlanScheduling {
	
	final int minPrio;
	
	public SimpleDynamicPriorityPlanScheduling(int minPrio){
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
	/**
	 * This Strategy reduces plan priority if element got scheduled
	 */
	protected void updatePriorities(IScheduling current) {
		int newPrio = current.getPlan().getCurrentPriority()-1;
		if (newPrio < minPrio){
			newPrio = current.getPlan().getBasePriority();
		}
		updatePriorityCurrent(newPrio);
	}



}
