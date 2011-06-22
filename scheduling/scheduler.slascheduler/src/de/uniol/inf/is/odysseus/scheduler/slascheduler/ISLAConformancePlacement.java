package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

public interface ISLAConformancePlacement {

	public ISubscribable<?, ?> placeSLAConformance(IPartialPlan plan, ISLAConformance conformance);
	
	public void removeSLAConformance(ISubscribable<?, ?> connectionPoint, ISLAConformance conformance);
	
}
