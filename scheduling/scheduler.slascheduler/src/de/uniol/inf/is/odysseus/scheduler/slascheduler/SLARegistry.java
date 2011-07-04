package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.sla.ISLAChangedEventListener;
import de.uniol.inf.is.odysseus.sla.SLAChangedEvent;
import de.uniol.inf.is.odysseus.sla.SLADictionary;

/**
 * central management of scheduling data
 * 
 * @author Thomas Vogelgesang
 *
 */
public class SLARegistry implements ISLAChangedEventListener {
	/**
	 * mapping partial plans to their relevant data
	 * TODO change mapping from pp to query
	 */
	private Map<IQuery, SLARegistryInfo> schedData;
	/**
	 * reference to the scheduler owning this registry
	 */
	private SLAPartialPlanScheduling scheduler;
	
	/**
	 * creates a new {@link SLARegistry} object
	 */
	public SLARegistry() {
		super();
		this.schedData = new HashMap<IQuery, SLARegistryInfo>();
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
	public SLARegistryInfo getData(IQuery query) {
		return this.schedData.get(query);
	}
	
	/**
	 * removes the scheduling data for the given partial plan
	 * @param plan the partial plan, whichs data should be removed from registry
	 * @return the removed scheduling data or null if no scheduling data could 
	 * be found for the given partial plan.
	 */
	private SLARegistryInfo removeSchedData(IQuery query) {
		return this.schedData.remove(query);
	}
	
	/**
	 * adds the given scheduling data for the given partial plan to the registry
	 * @param plan the partial plan
	 * @param data the scheduling data
	 */
	private void addSchedData(IQuery query, SLARegistryInfo data) {
		this.schedData.put(query, data);
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
					createSLAConformance(event.getSla(), this.scheduler, event.getQuery());
			data.setConformance(conformance);
			
			ICostFunction costFunction = new CostFunctionFactory().createCostFunction(this.scheduler.getCostFunctionName(), event.getSla());
			data.setCostFunction(costFunction);
			
			// starvation freedom and conformance placment need still partial plan
			IPartialPlan plan = this.scheduler.getPartialPlan(event.getQuery());
			
			IStarvationFreedom starvationFreedom = new StarvationFreedomFactory().
					buildStarvationFreedom(this.scheduler.getStarvationFreedom(),
							data, plan);
			data.setStarvationFreedom(starvationFreedom);
			
			data.setSla(event.getSla());
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement(event.getSla());
			data.setConnectionPoint(placement.placeSLAConformance(plan, conformance));
			
			this.addSchedData(event.getQuery(), data);
			
			break;
		}
		case remove: {
			SLARegistryInfo data = this.removeSchedData(event.getQuery());
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement(event.getSla());
			placement.removeSLAConformance(data.getConnectionPoint(), 
					data.getConformance());
			break;
		}
		default: throw new RuntimeException("Unknown event type: " +  event.getType());
		}
		
	}
	
}
