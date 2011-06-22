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
			SLARegistryInfo data = new SLARegistryInfo();
			ISLAConformance conformance = new SLAConformanceFactory().
					createSLAConformance(event.getSla(), this.scheduler, event.getPlan());
			data.setConformance(conformance);
			
			ICostFunction costFunction = new CostFunctionFactory().createCostFunction(this.scheduler.getCostFunctionName(), event.getSla());
			data.setCostFunction(costFunction);
			
			IStarvationFreedom starvationFreedom = new StarvationFreedomFactory().
					buildStarvationFreedom(this.scheduler.getStarvationFreedom(),
							data, event.getPlan());
			data.setStarvationFreedom(starvationFreedom);
			
			data.setSla(event.getSla());
			
			this.addSchedData(event.getPlan(), data);
			
			new SLAConformancePlacementFactory().buildSLAConformancePlacement(
					event.getSla()).placeSLAConformance(event.getPlan(), conformance);
			
			break;
		}
		case remove: {
			this.removeSchedData(event.getPlan());
			// TODO remove sla conformance operator from plan?			
			break;
		}
		default: throw new RuntimeException("Unknown event type: " +  event.getType());
		}
		
	}
	
}
