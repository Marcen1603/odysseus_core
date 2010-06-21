package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;


public class StaticPriorityScheduling extends AbstractPriorityPlanScheduling {

	public StaticPriorityScheduling(){}
	
	public StaticPriorityScheduling(
			StaticPriorityScheduling staticPriorityScheduling) {
		super(staticPriorityScheduling);
	}

	@Override
	public StaticPriorityScheduling clone() {
		return new StaticPriorityScheduling(this);
	}

	@Override
	protected void updatePriorities(IScheduling current) {
		// Nothing to do, this scheduling is static
	}

	


}
