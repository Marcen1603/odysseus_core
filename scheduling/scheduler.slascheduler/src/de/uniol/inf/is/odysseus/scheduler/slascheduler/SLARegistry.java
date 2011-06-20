package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;

public class SLARegistry implements ISLAChangedEventListener {
	
	private Map<IPartialPlan, SLARegistryInfo> schedData;
	
	private String costFunctionName;
	
	private SLAPartialPlanScheduling scheduler;
	
	public SLARegistry() {
		super();
		this.schedData = new HashMap<IPartialPlan, SLARegistryInfo>();
	}

	public SLA getSLA(IPartialPlan plan) {
		return this.schedData.get(plan).getSla();
	}
	
	public ISLAConformance getConformance(IPartialPlan plan) {
		return this.schedData.get(plan).getConformance();
	}
	
	public ICostFunction getCostFunction(IPartialPlan plan) {
		return this.schedData.get(plan).getCostFunction();
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
			ICostFunction costFunction = new CostFunctionFactory().createCostFunction(this.costFunctionName, event.getSla());
			
			SLARegistryInfo data = new SLARegistryInfo(event.getSla(), conformance, costFunction, 0);
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

	public void setCostFunctionName(String costFunctionName) {
		this.costFunctionName = costFunctionName;
	}

	public String getCostFunctionName() {
		return costFunctionName;
	}
	
}
