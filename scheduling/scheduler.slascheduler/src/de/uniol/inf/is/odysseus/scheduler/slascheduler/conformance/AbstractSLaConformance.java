package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationEvent;

public abstract class AbstractSLaConformance<T> extends AbstractSink<T> implements ISLAConformance {
	
	private ISLAViolationEventDistributor distributor;
	
	private SLA sla;
	// required for violationEvent generation
	private IPartialPlan plan;
	
	public AbstractSLaConformance(ISLAViolationEventDistributor dist, SLA sla, IPartialPlan plan) {
		this.distributor = dist;
		this.sla = sla;
		this.plan = plan;
	}
	
	protected ISLAViolationEventDistributor getDistributor() {
		return this.distributor;
	}
	
	protected SLA getSLA() {
		return this.sla;
	}
	
	protected IPartialPlan getPlan() {
		return this.plan;
	}
	
	protected void violation(int cost) {
		SLAViolationEvent event = new SLAViolationEvent(this.plan, this.sla, cost);
		this.distributor.queueSLAViolationEvent(event);
	}
}
