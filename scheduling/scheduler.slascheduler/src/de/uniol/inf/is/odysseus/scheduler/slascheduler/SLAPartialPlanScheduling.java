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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing.IQuerySharing;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing.QuerySharing;

/**
 * sla-based partial plan scheduler. it chooses the next partial plan to
 * schedule by comparing the opportunity cost and marginal gain. The scheduler
 * provides starvation freedom to avoid buffer overflows.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAPartialPlanScheduling implements IPartialPlanScheduling,
		ISLAViolationEventDistributor, IPlanModificationListener, ISchedulingEventListener {
	// ----------------------------------------------------------------------------------------
	// Logging
	// ----------------------------------------------------------------------------------------

	protected static Logger _logger = null;

	boolean loggedNull = false;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(SLAPartialPlanScheduling.class);
		}
		return _logger;
	}

	private static long lastLog = 0;

	protected static void debugSlow(int waitTime, String message) {
		long ts = System.currentTimeMillis();
		if (ts - lastLog > waitTime) {
			lastLog = ts;
			getLogger().debug(message);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Members
	// ----------------------------------------------------------------------------------------

	/**
	 * listeners connected to the scheduler for broadcasting
	 * {@link SLAViolationEvent}
	 */
	private List<ISLAViolationEventListener> listeners;
	/**
	 * List of scheduling objects representing schedulable partial plans
	 */
	private List<IScheduling> plans;
	/**
	 * registry for management of scheduling relevant information
	 */
	private SLARegistry registry;
	/**
	 * name of starvation freedom function
	 */
	private String starvationFreedom;
	/**
	 * name of the cost function that should be used
	 */
	private String costFunctionName;
	/**
	 * Priority function that should be used
	 */
	private IPriorityFunction prioFunction;
	/**
	 * queue for buffering {@link SLAViolationEvent} to avoid wasting processing
	 * time of a partial plan by handling events.
	 */
	private LinkedList<SLAViolationEvent> eventQueue;
	/**
	 * the decay that should be used in starvation freedom function
	 */
	private double decaySF;

	private IQuerySharing querySharing;

	private LinkedList<Pair<IScheduling, Double>> queue;
	@SuppressWarnings("unused")
	private int numberOfQueueScheds = 0;
	
	final private Set<IScheduling> pausedPlans;
	private ReentrantLock lock = new ReentrantLock();

	/**
	 * creates a new sla-based partial plan scheduler
	 * 
	 * @param starvationFreedomFuncName
	 *            name of the starvation freedom function
	 * @param prio
	 *            Priority function
	 * @param decaySF
	 *            decay for starvation freedom function
	 * @param querySharing
	 *            true iff the scheduling algorithm should consider the effort
	 *            of query sharing
	 * @param querySharingCostModelName
	 *            the name of the cost model that should be used to consider the
	 *            effort of query sharing
	 */
	public SLAPartialPlanScheduling(String starvationFreedomFuncName,
			IPriorityFunction prio, double decaySF, boolean querySharing,
			String querySharingCostModelName, String costFunctionName) {
		getLogger().debug("Creating SLA PartialPlanScheduling");
		this.plans = new ArrayList<IScheduling>();
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		this.registry = new SLARegistry(this);
		this.starvationFreedom = starvationFreedomFuncName;
		this.prioFunction = prio;
		this.eventQueue = new LinkedList<SLAViolationEvent>();
		this.decaySF = decaySF;
		this.costFunctionName = costFunctionName;
		if (querySharing) {
			this.querySharing = new QuerySharing(querySharingCostModelName);
		} else {
			this.querySharing = null;
		}
		this.addSLAViolationEventListener(new SLAViolationLogger());
		this.queue = new LinkedList<Pair<IScheduling, Double>>();
		
		pausedPlans = new HashSet<IScheduling>();
//		// init csv logger
//		SLATestLogger.initCSVLogger("scheduler0", 1000000, 0, "Query", "oc",
//				"mg", "sf", "prio", "conformance", "service level");
//		SLATestLogger.initCSVLogger("scheduler1", 1000000, 0, "Query", "oc",
//				"mg", "sf", "prio", "conformance", "service level");
//		SLATestLogger.initCSVLogger("scheduler2", 1000000, 0, "Query", "oc",
//				"mg", "sf", "prio", "conformance", "service level");

//		SLATestLogger.init();
		getLogger().debug("Creating SLA PartialPlanScheduling .Done");
	}

	/**
	 * compy constructor for clone method
	 * 
	 * @param schedule
	 *            the object to clone
	 */
	@SuppressWarnings("unchecked")
	private SLAPartialPlanScheduling(SLAPartialPlanScheduling schedule) {
		this.listeners = new ArrayList<ISLAViolationEventListener>(
				schedule.listeners);
		this.plans = new ArrayList<IScheduling>();
		this.plans.addAll(schedule.plans);
		this.registry = schedule.registry;
		this.starvationFreedom = schedule.starvationFreedom;
		this.eventQueue = (LinkedList<SLAViolationEvent>) schedule.eventQueue
				.clone();
		this.querySharing = schedule.querySharing;
		this.queue = schedule.queue;
		
		this.pausedPlans = new HashSet<IScheduling>(schedule.pausedPlans);
	}

	/**
	 * clears all plans
	 */
	@Override
	public synchronized void clear() {
		lock.lock();
		this.plans.clear();
		for (IScheduling plan : plans) {
			plan.removeSchedulingEventListener(this);
		}
		pausedPlans.clear();
		lock.unlock();
	}

	/**
	 * adds a plan to the scheduler
	 */
	@Override
	public synchronized void addPlan(IScheduling scheduling) {
		getLogger().debug(
				"Plan added to SLAPartialPlanScheduling: " + scheduling);
		this.plans.add(scheduling);
		getLogger().debug(this.plans.toString());
		this.refreshQuerySharing();
	}

	/**
	 * returns the number of plans that manged by the scheduler
	 */
	@Override
	public int planCount() {
		return this.plans.size();
	}

	/**
	 * returns the next partial plan to schedule represented by an
	 * {@link IScheduling}
	 */
	@Override
	public synchronized IScheduling nextPlan() {
		// check for sla violation and fire events
		while (!this.eventQueue.isEmpty()) {
			this.fireSLAViolationEvent(this.eventQueue.pop());
		}
		
		synchronized (pausedPlans) {
			while (this.pausedPlans.size() == this.plans.size()) {
				try {
					this.pausedPlans.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		IScheduling next = null;
		double nextPrio = -1;
		
		for (IScheduling scheduling : this.plans) {
			// calculate sla conformance for all queries
			// Attention: it is expected that 1 partial plan contains 1 query
			IPhysicalQuery query = scheduling.getPlan().getQueries().get(0);
			if (query.isOpened()) {
				SLARegistryInfo data = this.registry.getData(query);
				
				if (data != null) {
					// first check for sla violation and create event in case of
					// violation
					data.getConformance().checkViolation();
					if (this.hasNext(data.getBuffers())) {

						SLA sla = (SLA) query.getParameter(SLA.class.getName());
						double conformance = data.getConformance()
								.predictConformance();
						// calculate priorities for all partial plans:
						// - calculate oc
						ICostFunction costFunc = data.getCostFunction();
						double oc = costFunc.oc(conformance, sla);

						// - calculate mg
						double mg = costFunc.mg(conformance, sla);

						// - calculate sf
						double sf = data.getStarvationFreedom().sf(
								this.getDecaySF());

						// - calculate prio
						double prio = this.prioFunction
								.calcPriority(oc, mg, sf);

//						SLATestLogger.logCSV("scheduler" + query.getID(), query
//								.getID(), oc, mg, sf, prio, conformance,
//								((QuadraticCFLatency) costFunc)
//										.getCurrentServiceLevelIndex(
//												conformance, sla));

						// select plan with highest priority
						if (prio > nextPrio) {
							next = scheduling;
							nextPrio = prio;
						}

						if (this.querySharing != null) {
							this.querySharing.setPriority(scheduling, prio);
						}
					}
				}else{
					getLogger().warn("Not entry found for query {}",query);
				}
			}

		}

		if (this.querySharing != null) {
			// optional: consider effort of query sharing
			IScheduling tmpNext = this.querySharing.getNextPlan();
			if (tmpNext != null) {
				next = tmpNext;
			}
		}

		if (next != null){
//			getLogger().debug(
//					"[" + SLATestLogger.formatNanoTime(System.nanoTime())
//							+ "] Scheduling query "
//							+ next.getPlan().getQueries().get(0).getID());
		}
		// set tmestamp of last execution
		if (next != null) {
			SLARegistryInfo data = this.registry.getData(next.getPlan()
					.getQueries().get(0));
			data.setLastExecTimeStamp(System.currentTimeMillis());
		}

		
		
		return next;
	}

	/*
	 * SCHEDULING METHOD WITH QUEUE OPTIMIZATION!
	 */

	// /**
	// * returns the next partial plan to schedule represented by an
	// * {@link IScheduling}
	// */
	// @Override
	// public synchronized IScheduling nextPlan() {
	// OVERHEAD.start();
	// // check for sla violation and fire events
	// while (!this.eventQueue.isEmpty()) {
	// this.fireSLAViolationEvent(this.eventQueue.pop());
	// }
	// IScheduling next = null;
	//
	// if (this.numberOfQueueScheds <= 0) {
	// this.queue.clear();
	// for (IScheduling scheduling : this.plans) {
	// // calculate sla conformance for all queries
	// // Attention: it is expected that 1 partial plan contains 1 query
	// IPhysicalQuery query = scheduling.getPlan().getQueries().get(0);
	// SLARegistryInfo data = this.registry.getData(query);
	// if (data != null) {
	// // first check for sla violation and create event in case of violation
	// data.getConformance().checkViolation();
	// if (SLAPartialPlanScheduling.hasNext(data.getBuffers())) {
	//
	// SLA sla = (SLA) query.getParameter(SLA.class.getName());
	// double conformance = data.getConformance().predictConformance();
	// // calculate priorities for all partial plans:
	// // - calculate oc
	// ICostFunction costFunc = data.getCostFunction();
	// double oc = costFunc.oc(conformance, sla);
	//
	// // - calculate mg
	// double mg = costFunc.mg(conformance, sla);
	//
	// // - calculate sf
	// double sf = data.getStarvationFreedom().sf(this.getDecaySF());
	//
	// // - calculate prio
	// double prio = this.prioFunction.calcPriority(oc, mg, sf);
	//
	// // SLATestLogger.logCSV("scheduler"+query.getID(), query.getID(), oc, mg,
	// sf,
	// // prio, conformance, ((QuadraticCFLatency) costFunc)
	// // .getCurrentServiceLevelIndex(conformance, sla));
	//
	// // add plan to queue
	// this.queue.add(new Pair<IScheduling, Double>(scheduling, prio));
	//
	// if (this.querySharing != null) {
	// this.querySharing.setPriority(scheduling, prio);
	// }
	// }
	// }
	// }
	// Comparator<Pair<IScheduling, Double>> comp = new
	// Comparator<Pair<IScheduling,Double>>() {
	//
	// @Override
	// public int compare(Pair<IScheduling, Double> o1,
	// Pair<IScheduling, Double> o2) {
	// if (o1.getE2() > o2.getE2()) {
	// return -1;
	// } else if (o1.getE2() < o2.getE2()) {
	// return 1;
	// }
	//
	// return 0;
	// }
	// };
	// Collections.sort(this.queue, comp);
	// // for (Pair<IScheduling, Double> e: this.queue) {
	// // System.out.println(e.getE2());
	// // }
	// // System.out.println("*****************");
	// // for (int i = 0; i < this.queue.size() - 10; i++) {
	// // this.queue.remove();
	// // }
	// this.numberOfQueueScheds = this.queue.size() / 10;// schedule only 10% of
	// queries
	// if (this.numberOfQueueScheds == 0 && this.queue.size() > 0)
	// this.numberOfQueueScheds = 1;// schedule at least one query if
	// any/home/tommy/msc/evaluation/sim15/info
	// }
	// if (this.numberOfQueueScheds > 0) {
	// next = this.queue.remove().getE1();
	// this.numberOfQueueScheds--;
	// }
	//
	//
	// if (this.querySharing != null) {
	// // optional: consider effort of query sharing
	// IScheduling tmpNext = this.querySharing.getNextPlan();
	// if (tmpNext != null) {
	// next = tmpNext;
	// }
	// }
	//
	// // if (next != null)
	// // SLATestLogger.log("["
	// // + SLATestLogger.formatNanoTime(System.nanoTime())
	// // + "] Scheduling query "
	// // + next.getPlan().getQueries().get(0).getID());
	// // set tmestamp of last execution
	// if (next != null) {
	// SLARegistryInfo data =
	// this.registry.getData(next.getPlan().getQueries().get(0));
	// data.setLastExecTimeStamp(System.currentTimeMillis());
	// }
	//
	// OVERHEAD.stop();
	// return next;
	// }

	/**
	 * Checks if there are elements waiting for execution in the given buffers
	 * 
	 * @param buffers
	 *            list of buffers owned by one query
	 * @return true iff the given buffers have elements to process
	 */
	private boolean hasNext(List<IBuffer<?>> buffers) {
		for (IBuffer<?> buffer : buffers) {
			if (buffer.size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * removes a plan from the scheduler
	 */
	@Override
	public synchronized void removePlan(IScheduling plan) {
		this.plans.remove(plan);
		this.refreshQuerySharing();
		pausedPlans.remove(plan);
		plan.removeSchedulingEventListener(this);
	}

	/**
	 * returns a copy of the scheduler
	 */
	@Override
	public IPartialPlanScheduling clone() {
		return new SLAPartialPlanScheduling(this);
	}

	/**
	 * adds an event listener for {@link SLAViolationEvent}
	 * 
	 * @param listener
	 *            the event listener to add
	 */
	public void addSLAViolationEventListener(ISLAViolationEventListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * removes an event listener for {@link SLAViolationEvent}
	 * 
	 * @param listener
	 *            the event listener to remove
	 * @return
	 */
	public boolean removeSLAViolationEventListener(
			ISLAViolationEventListener listener) {
		return this.listeners.remove(listener);
	}

	/**
	 * broadcasts the given {@link SLAViolationEvent} to all registered event
	 * listeners
	 * 
	 * @param event
	 *            teh event to boradcast to the listeners
	 */
	private void fireSLAViolationEvent(SLAViolationEvent event) {
		for (ISLAViolationEventListener listener : this.listeners) {
			listener.slaViolated(event);
		}
	}

	/**
	 * adds the given event to the event buffer
	 * 
	 * @param event
	 *            the event to buffer
	 */
	@Override
	public void queueSLAViolationEvent(SLAViolationEvent event) {
		this.eventQueue.add(event);
	}

	/**
	 * sets the decay for starvatio freedom
	 * 
	 * @param decaySF
	 *            the decay
	 */
	public void setDecaySF(double decaySF) {
		this.decaySF = decaySF;
	}

	/**
	 * @return the decay for starvation freedom
	 */
	public double getDecaySF() {
		return decaySF;
	}

	/**
	 * sets the name of the cost function
	 * 
	 * @param costFunctionName
	 *            the new cost function name
	 */
	public void setCostFunctionName(String costFunctionName) {
		this.costFunctionName = costFunctionName;
	}

	/**
	 * @return the name of the cost function
	 */
	public String getCostFunctionName() {
		return costFunctionName;
	}

	/**
	 * @return the name of the starvation freedom function to use
	 */
	public String getStarvationFreedom() {
		return starvationFreedom;
	}

	/**
	 * sets the name of the starvation freedom function
	 * 
	 * @param starvationFreedom
	 *            the name of the starvation freedom function
	 */
	public void setStarvationFreedom(String starvationFreedom) {
		this.starvationFreedom = starvationFreedom;
	}

	/**
	 * returns the partial plan that represents the given query in scheduling.
	 * it is expected that each partial plan contains only one query. this
	 * method is required because some objects still need the partial plan (e.g.
	 * for finding buffers)
	 * 
	 * @param query
	 *            the given query
	 * @return the partial plan that represents the given query in scheduling or
	 *         null if no partial plan was found for the given query
	 */
	@Deprecated
	public IPartialPlan getPartialPlan(ILogicalQuery query) {
		for (IScheduling sched : this.plans) {
			if (sched.getPlan().getQueries().equals(query))
				return sched.getPlan();
		}
		return null;
	}

	/**
	 * updates the underlying data structures if the effort of query sharing
	 * should be considered
	 */
	private void refreshQuerySharing() {
		if (this.querySharing != null) {
			this.querySharing.refreshEffortTable(this.plans);
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		getLogger().debug("plan modified event");
		this.registry.planModificationEvent(eventArgs);
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		synchronized (pausedPlans) {
			pausedPlans.remove(sched);
			pausedPlans.notifyAll();
		}
	}

	@Override
	public void nothingToSchedule(IScheduling sched) {
		pausedPlans.add(sched);
	}
}
