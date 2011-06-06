package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public class SLAViolationEvent {

	private IPartialPlan plan;

	private int cost;

	private SLA sla;

	public SLAViolationEvent(IPartialPlan plan, SLA sla, int cost) {
		this.plan = plan;
		this.sla = sla;
		this.cost = cost;
	}

	public IPartialPlan getPlan() {
		return plan;
	}

	public int getCost() {
		return cost;
	}

	public SLA getSla() {
		return sla;
	}

}
