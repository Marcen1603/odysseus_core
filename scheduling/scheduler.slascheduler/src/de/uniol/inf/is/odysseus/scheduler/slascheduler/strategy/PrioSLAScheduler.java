package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy.SimpleDynamicPriorityPlanScheduling;

// This class is only for testing purposes to compare
// sla based scheduling with priority based scheduling
// do not use this class in real szenarios ;-)

public class PrioSLAScheduler extends SimpleDynamicPriorityPlanScheduling{

	public PrioSLAScheduler(int minPrio) {
		super(minPrio);
	}
	
	public PrioSLAScheduler(PrioSLAScheduler other){
		super(other);
	}

}
