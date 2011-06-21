package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

public class SLARegistry implements ISLAChangedEventListener {
	
	private Map<IPartialPlan, SLARegistryInfo> schedData;
	
	private SLAPartialPlanScheduling scheduler;
	
	public SLARegistry() {
		super();
		this.schedData = new HashMap<IPartialPlan, SLARegistryInfo>();
	}

	public SLARegistryInfo getData(IPartialPlan plan) {
		return this.schedData.get(plan);
	}
	
	private void removeSchedData(IPartialPlan plan) {
		this.schedData.remove(plan);
	}
	
	private void addSchedData(IPartialPlan plan, SLARegistryInfo data) {
		this.schedData.put(plan, data);
	}
	
	@Override
	public void slaChanged(SLAChangedEvent event) {
		switch (event.getType()) {
		case add: {
			ISLAConformance conformance = new SLAConformanceFactory().
					createSLAConformance(event.getSla(), this.scheduler, event.getPlan());
			ICostFunction costFunction = new CostFunctionFactory().createCostFunction(this.scheduler.getCostFunctionName(), event.getSla());
			
			SLARegistryInfo data = new SLARegistryInfo(event.getSla(), conformance, costFunction, 0, new StarvationFreedomFactory().buildStarvationFreedom(this.scheduler.getStarvationFreedom()));
			this.addSchedData(event.getPlan(), data);
			
//			how to get last operator that allows connecting?
//			TODO: encapsulating operator placement in operator placement strategies
//			event.getPlan().getQueryRoots();
			
			break;
		}
		case remove: {
			this.removeSchedData(event.getPlan());
			break;
		}
		default: throw new RuntimeException("Unknown event type: " +  event.getType());
		}
		
	}

	
	
}
