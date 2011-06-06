package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public class SLARegistry implements ISLAChangedEventListener {
	
	private Map<IPartialPlan, SLA> slas;
	
	private Map<IPartialPlan, ISLAConformance> conformances;
	
	public SLARegistry() {
		super();
		this.slas = new HashMap<IPartialPlan, SLA>();
		this.conformances = new HashMap<IPartialPlan, ISLAConformance>();
	}

	public SLA getSLA(IPartialPlan plan) {
		return this.slas.get(plan);
	}
	
	public ISLAConformance getConformance(IPartialPlan plan) {
		return this.conformances.get(plan);
	}
	
	private void removeSLA(IPartialPlan plan) {
		this.slas.remove(plan);
	}
	
	private void removeConformance(IPartialPlan plan) {
		this.conformances.remove(plan);
	}
	
	private void addSLA(IPartialPlan plan, SLA sla) {
		this.slas.put(plan, sla);
	}
	
	private void addConformance(IPartialPlan plan, ISLAConformance conformance) {
		this.conformances.put(plan, conformance);
	}

	@Override
	public void slaChanged(SLAChangedEvent event) {
		switch (event.getType()) {
		case add: {
			ISLAConformance conformance = new SLAConformanceFactory().
					createSLAConformance(event.getSla());
			this.addConformance(event.getPlan(), conformance);
			this.addSLA(event.getPlan(), event.getSla());
			break;
		}
		case remove: {
			this.removeSLA(event.getPlan());
			this.removeConformance(event.getPlan());
			break;
		}
		default: throw new RuntimeException("Unknown event type: " +  event.getType());
		}
		
	}
	
}
