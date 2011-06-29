package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.slamodel.ISLAChangedEventListener;
import de.uniol.inf.is.odysseus.slamodel.SLAChangedEvent;
import de.uniol.inf.is.odysseus.slamodel.SLADictionary;

/**
 * central management of scheduling data
 * 
 * @author Thomas Vogelgesang
 *
 */
public class SLARegistry implements ISLAChangedEventListener {
	/**
	 * mapping partial plans to their relevant data
	 */
	private Map<IPartialPlan, SLARegistryInfo> schedData;
	/**
	 * reference to the scheduler owning this registry
	 */
	private SLAPartialPlanScheduling scheduler;
	
	/**
	 * creates a new {@link SLARegistry} object
	 */
	public SLARegistry() {
		super();
		this.schedData = new HashMap<IPartialPlan, SLARegistryInfo>();
		/*
		 *  register add central sla dictionary to get notification about
		 *  changes of sla
		 */
		
		SLADictionary.getInstance().addSLAChangedEventListener(this);
	}

	/**
	 * looks up the scheduling data for a given partial plan
	 * 
	 * @param plan the partial plan
	 * 
	 * @return the scheduling data relevant for the given partial plan or null 
	 * if no data is stored for the given partial plan
	 */
	public SLARegistryInfo getData(IPartialPlan plan) {
		return this.schedData.get(plan);
	}
	
	/**
	 * removes the scheduling data for the given partial plan
	 * @param plan the partial plan, whichs data should be removed from registry
	 * @return the removed scheduling data or null if no scheduling data could 
	 * be found for the given partial plan.
	 */
	private SLARegistryInfo removeSchedData(IPartialPlan plan) {
		return this.schedData.remove(plan);
	}
	
	/**
	 * adds the given scheduling data for the given partial plan to the registry
	 * @param plan the partial plan
	 * @param data the scheduling data
	 */
	private void addSchedData(IPartialPlan plan, SLARegistryInfo data) {
		this.schedData.put(plan, data);
	}

	/**
	 * handles a {@link SLAChangedEvent} objects
	 * @param event the event to handle
	 */
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
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement(event.getSla());
			data.setConnectionPoint(placement.placeSLAConformance(event.getPlan(), conformance));
			
			this.addSchedData(event.getPlan(), data);
			
			break;
		}
		case remove: {
			SLARegistryInfo data = this.removeSchedData(event.getPlan());
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement(event.getSla());
			placement.removeSLAConformance(data.getConnectionPoint(), 
					data.getConformance());
			break;
		}
		default: throw new RuntimeException("Unknown event type: " +  event.getType());
		}
		
	}
	
}
