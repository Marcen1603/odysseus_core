package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.ISubscribable;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
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
			
			this.placeSLAConformance(event.getPlan(), conformance);
			
			break;
		}
		case remove: {
			this.removeSchedData(event.getPlan());
			break;
		}
		default: throw new RuntimeException("Unknown event type: " +  event.getType());
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void placeSLAConformance(IPartialPlan plan, ISLAConformance conformance) {
//		TODO: encapsulating operator placement in operator placement strategies?
//		event.getPlan().getQueryRoots();
		// it is expected that there is only one query per partial plan 
		IPhysicalOperator root = plan.getQueryRoots().get(0);
		if (root instanceof ISource) {
			ISubscribable subscribable = (ISubscribable)root;
			subscribable.connectSink(conformance, 0, 0, root.getOutputSchema());
		} else {
			throw new RuntimeException("Cannot connect SLA conformance operator to query root");
		}
	}
	
}
