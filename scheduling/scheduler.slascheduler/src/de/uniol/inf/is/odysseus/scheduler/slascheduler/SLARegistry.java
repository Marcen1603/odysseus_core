package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * central management of scheduling data
 * 
 * @author Thomas Vogelgesang
 *
 */
public class SLARegistry implements IPlanModificationListener {
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
	public SLARegistry(SLAPartialPlanScheduling scheduler) {
		super();
		this.scheduler = scheduler;
		this.schedData = new HashMap<IQuery, SLARegistryInfo>();
		/*
		 * the SLARegistry won't be registered as eventlistener to the executor
		 * because the executor is now available here. instead the 
		 * schedulermanager implements the listener interface and registers add
		 * the abstract executor as listener. if the schedulermanager receives
		 * an event and the scheduler implmenetes the listener interface it will
		 * pass the event. all other classes between schedulermanager and 
		 * slaregistry pass the event in the same way if they are listeners.
		 * before passing the event it must be checked if teh receiver is a 
		 * listener because only certain classes implement this interface. Due
		 * to this only certain combination of schedulermanager/scheduler/
		 * partialplanscheduling etc. could be used! otherwise the sla regsitry
		 * won't be able to react on changing plans.
		 */
//		IExecutor executor = ExecutorHandler.getExecutor();
//		if (executor != null) {
//			executor.addPlanModificationListener(this);
//		} else {
//			throw new RuntimeException(
//					"Cannot register plan modification listener: executor not found");
//		}
		
//		final SLARegistry reg = this; 
//		
//		Thread t = new Thread(new Runnable() {
//			IExecutor executor;
//			@Override
//			public void run() {
//				while((executor = ExecutorHandler.getExecutor()) == null) {
//				}
//				System.err.println("GOT EXECUTOR");
//				executor.addPlanModificationListener(reg);
//			}
//		});
//		t.start();
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

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType)eventArgs.getEventType(); 
		switch (eventType) {
		case QUERY_ADDED: {
			IQuery query = ((QueryPlanModificationEvent)eventArgs).getValue();
			
			SLARegistryInfo data = new SLARegistryInfo();
			ISLAConformance conformance = new SLAConformanceFactory().
					createSLAConformance(query.getSLA(), this.scheduler, query);
			data.setConformance(conformance);
			
			ICostFunction costFunction = new CostFunctionFactory().createCostFunction(this.scheduler.getCostFunctionName(), query.getSLA());
			data.setCostFunction(costFunction);
			
			// starvation freedom and conformance placment need still partial plan
			IPartialPlan plan = this.scheduler.getPartialPlan(query);
			
			IStarvationFreedom starvationFreedom = new StarvationFreedomFactory().
					buildStarvationFreedom(this.scheduler.getStarvationFreedom(),
							data, query);
			data.setStarvationFreedom(starvationFreedom);
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement(query.getSLA());
			data.setConnectionPoint(placement.placeSLAConformance(query, conformance));
			
			this.addSchedData(query, data);
			
			break;
		}
		case QUERY_REMOVE: {
			IQuery query = ((QueryPlanModificationEvent)eventArgs).getValue();
			
			SLARegistryInfo data = this.removeSchedData(query);
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement(query.getSLA());
			placement.removeSLAConformance(data.getConnectionPoint(), 
					data.getConformance());
			break;
		}
		}
	}
	
}
