/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;

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
	private Map<IPhysicalQuery, SLARegistryInfo> schedData;
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
		this.schedData = new HashMap<IPhysicalQuery, SLARegistryInfo>();
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
	public SLARegistryInfo getData(IPhysicalQuery query) {
		return this.schedData.get(query);
	}
	
	/**
	 * removes the scheduling data for the given partial plan
	 * @param plan the partial plan, whichs data should be removed from registry
	 * @return the removed scheduling data or null if no scheduling data could 
	 * be found for the given partial plan.
	 */
	private SLARegistryInfo removeSchedData(IPhysicalQuery query) {
		return this.schedData.remove(query);
	}
	
	/**
	 * adds the given scheduling data for the given partial plan to the registry
	 * @param plan the partial plan
	 * @param data the scheduling data
	 */
	private void addSchedData(IPhysicalQuery query, SLARegistryInfo data) {
		this.schedData.put(query, data);
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType)eventArgs.getEventType(); 
		switch (eventType) {
		case QUERY_ADDED: {
			IPhysicalQuery query = ((QueryPlanModificationEvent)eventArgs).getValue();
			SLARegistryInfo data = this.initRegistryInfo(query);
			this.addSchedData(query, data);
			
			this.initSLAConformacePrediction(data.getBuffers(), data.getConformance());
			
			break;
		}
		case QUERY_REMOVE: {
			IPhysicalQuery query = ((QueryPlanModificationEvent)eventArgs).getValue();
			
			SLARegistryInfo data = this.removeSchedData(query);
			
			ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement((SLA) query.getParameter(SLA.class.getName()));
			placement.removeSLAConformance(data.getConnectionPoint(), 
					data.getConformance());
			break;
		}
		default:
            break;
		}
	}
	
	private SLARegistryInfo initRegistryInfo(IPhysicalQuery query) {
		SLARegistryInfo data = new SLARegistryInfo();
		ISLAConformance conformance = new SLAConformanceFactory().
				createSLAConformance((SLA) query.getParameter(SLA.class.getName()), this.scheduler, query);
		data.setConformance(conformance);
		
		ICostFunction costFunction = new CostFunctionFactory().createCostFunction(this.scheduler.getCostFunctionName(), (SLA) query.getParameter(SLA.class.getName()));
		data.setCostFunction(costFunction);
		
		IStarvationFreedom starvationFreedom = new StarvationFreedomFactory().
				buildStarvationFreedom(this.scheduler.getStarvationFreedom(),
						data, query);
		data.setStarvationFreedom(starvationFreedom);
		
		ISLAConformancePlacement placement = new SLAConformancePlacementFactory().buildSLAConformancePlacement((SLA) query.getParameter(SLA.class.getName()));
		data.setConnectionPoint(placement.placeSLAConformance(query, conformance));
		
		List<IBuffer<?>> buffers = findBuffers(query);
		data.setBuffers(buffers);
		conformance.setBuffers(buffers);
		
		return data;
	}

	/**
	 * Returns a list of all buffers owned by the given query
	 * @param query the query
	 * @return a list of all buffers owned by the query. could be empty, if
	 * 		the query ownes no buffers 
	 */
	private static List<IBuffer<?>> findBuffers(IPhysicalQuery query) {
		List<IBuffer<?>> buffers = new ArrayList<IBuffer<?>>();
		for (IPhysicalOperator po : query.getAllOperators()) {
			if (po instanceof IBuffer<?>) {
				IBuffer<?> buffer = (IBuffer<?>) po;
				buffers.add(buffer);
			}
		}
		return buffers;
	}
	
	/**
	 * initializes the SLAConformance prediction. Has side-effects on the parameter conformance: sets the map containing all paths for buffers
	 * @param list
	 * @param conformance
	 */
	private void initSLAConformacePrediction(List<IBuffer<?>> list, ISLAConformance conformance) {
		Map<IBuffer<?>, List<List<IPhysicalOperator>>> pathMap = new HashMap<>();
		
		for (IBuffer<?> buffer : list) {
			List<List<IPhysicalOperator>> paths = findPaths(buffer);
			pathMap.put(buffer, paths);
		}
		
		conformance.setPathMap(pathMap);
	}
	
	private static List<List<IPhysicalOperator>> findPaths(IBuffer<?> buffer) {
		List<IPhysicalOperator> path = new ArrayList<>();
		
		return findPathsRecursive(path, buffer);
	}
	
	private static List<List<IPhysicalOperator>> findPathsRecursive(List<IPhysicalOperator> walkedPath, IPhysicalOperator currentOp) {
		List<List<IPhysicalOperator>> paths = null;
		
		if (currentOp.isSource()) {
			// follow outgoing paths 
			ISource<?> source = (ISource<?>) currentOp;
			Collection<?> subscriptions = source.getSubscriptions();
			
			walkedPath.add(currentOp);
			
			for (Object obj : subscriptions) {
				@SuppressWarnings("unchecked")
				PhysicalSubscription<ISink<?>> subscription = (PhysicalSubscription<ISink<?>>) obj;
				ISink<?> target = subscription.getTarget();
				
				List<IPhysicalOperator> newPath = null; 
				if (subscriptions.size() > 1) {
					// new lists in case of path split
					newPath = new ArrayList<>(walkedPath);
				} else {
					newPath = walkedPath;
				}
				
				List<List<IPhysicalOperator>> foundPaths = findPathsRecursive(newPath, target);
				
				if (paths == null) {
					paths = foundPaths;
				} else {
					paths.addAll(foundPaths);
				}
				
			}
		} else {
			// current op is leaf
			paths = new ArrayList<>();
			walkedPath.add(currentOp);
			paths.add(walkedPath);
		}
		
		return paths;
	}
	
}
