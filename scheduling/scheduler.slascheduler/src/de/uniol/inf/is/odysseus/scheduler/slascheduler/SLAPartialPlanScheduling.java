package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing.IQuerySharing;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.querysharing.QuerySharing;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.sla.SLA;

/**
 * sla-based partial plan scheduler. it chooses the next partial plan to
 * schedule by comparing the opportunity cost and marginal gain. The scheduler
 * provides starvation freedom to avoid buffer overflows.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAPartialPlanScheduling implements IPartialPlanScheduling,
		ISLAViolationEventDistributor, IPlanModificationListener {
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
			String querySharingCostModelName) {
		this.plans = new ArrayList<IScheduling>();
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		this.registry = new SLARegistry();
		this.starvationFreedom = starvationFreedomFuncName;
		this.prioFunction = prio;
		this.eventQueue = new LinkedList<SLAViolationEvent>();
		this.decaySF = decaySF;
		if (querySharing) {
			this.querySharing = new QuerySharing(querySharingCostModelName);
		} else {
			this.querySharing = null;
		}
	}

	/**
	 * compy constructor for clone method
	 * 
	 * @param schedule
	 *            the object to clone
	 */
	@SuppressWarnings("unchecked")
	private SLAPartialPlanScheduling(SLAPartialPlanScheduling schedule) {
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		for (ISLAViolationEventListener listener : schedule.listeners) {
			this.listeners.add(listener);
		}
		this.plans = new ArrayList<IScheduling>();
		for (IScheduling plan : schedule.plans) {
			this.plans.add(plan);
		}
		this.registry = schedule.registry;
		this.starvationFreedom = schedule.starvationFreedom;
		this.eventQueue = (LinkedList<SLAViolationEvent>) schedule.eventQueue
				.clone();
		this.querySharing = schedule.querySharing;
	}

	/**
	 * clears all plans
	 */
	@Override
	public void clear() {
		this.plans.clear();
	}

	/**
	 * adds a plan to the scheduler
	 */
	@Override
	public void addPlan(IScheduling scheduling) {
		this.plans.add(scheduling);
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
	public IScheduling nextPlan() {
		// check for sla violation and fire events
		while (!this.eventQueue.isEmpty()) {
			this.fireSLAViolationEvent(this.eventQueue.pop());
		}

		IScheduling next = null;
		double nextPrio = 0;

		for (IScheduling scheduling : this.plans) {
			// calculate sla conformance for all queries
			// Attention: it is expected that 1 partial plan contains 1 query
			IQuery query = scheduling.getPlan().getQueries().get(0);
			SLARegistryInfo data = this.registry.getData(query);
			SLA sla = query.getSLA();
			double conformance = data.getConformance().getConformance();
			// calculate priorities for all partial plans:
			// - calculate oc
			ICostFunction costFunc = data.getCostFunction();
			double oc = costFunc.oc(conformance, sla);

			// - calculate mg
			double mg = costFunc.oc(conformance, sla);

			// - calculate sf
			double sf = data.getStarvationFreedom().sf(this.getDecaySF());

			// - calculate prio
			double prio = this.prioFunction.calcPriority(oc, mg, sf);

			// select plan with highest priority
			if (prio > nextPrio) {
				next = scheduling;
				nextPrio = prio;
			}

			if (this.querySharing != null) {
				this.querySharing.setPriority(scheduling, prio);
			}
		}

		if (this.querySharing != null) {
			// optional: consider effort of query sharing
			next = this.querySharing.getNextPlan();
		}

		return next;
	}

	/**
	 * removes a plan from the scheduler
	 */
	@Override
	public void removePlan(IScheduling plan) {
		this.plans.remove(plan);
		this.refreshQuerySharing();
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
	public IPartialPlan getPartialPlan(IQuery query) {
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
		this.registry.planModificationEvent(eventArgs);
	}
}
