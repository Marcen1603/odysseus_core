package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public class SLARegistry {
	
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
	
}
