package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

public interface ISLAConformancePlacement {

	public void placeSLAConformance(IPartialPlan plan, ISLAConformance conformance);
	
}
