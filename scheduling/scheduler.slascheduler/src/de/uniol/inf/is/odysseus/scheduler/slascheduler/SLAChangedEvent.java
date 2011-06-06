package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public class SLAChangedEvent {

	private SLAChangedEventType type;
	
	private SLA sla;
	
	private IPartialPlan plan;

	public SLAChangedEvent(SLAChangedEventType type, SLA sla, IPartialPlan plan) {
		super();
		this.type = type;
		this.sla = sla;
		this.plan = plan;
	}

	public SLAChangedEventType getType() {
		return type;
	}

	public SLA getSla() {
		return sla;
	}

	public IPartialPlan getPlan() {
		return plan;
	}
	
}
