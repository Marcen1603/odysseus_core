package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

/**
 * sla-based partial plan scheduler. it chooses the next partial plan to
 * schedule by comparing the opportunity cost and marginal gain. The scheduler
 * provides starvation freedom to avoid buffer overflows.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAPartialPlanScheduling implements IPartialPlanScheduling,
		ISLAViolationEventDistributor {
	/**
	 * size of tuble trains
	 */

	private List<ISLAViolationEventListener> listeners;

	private List<IScheduling> plans;

	private SLARegistry registry;

	private String starvationFreedom;

	public String getStarvationFreedom() {
		return starvationFreedom;
	}

	public void setStarvationFreedom(String starvationFreedom) {
		this.starvationFreedom = starvationFreedom;
	}

	private String costFunctionName;

	private IPriorityFunction prioFunction;

	private LinkedList<SLAViolationEvent> eventQueue;

	private double decaySF;

	public SLAPartialPlanScheduling(String starvationFreedomFuncName,
			IPriorityFunction prio) {
		this.plans = new ArrayList<IScheduling>();
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		this.registry = new SLARegistry();
		this.starvationFreedom = starvationFreedomFuncName;
		this.prioFunction = prio;
		this.eventQueue = new LinkedList<SLAViolationEvent>();
	}

	@SuppressWarnings("unchecked")
	public SLAPartialPlanScheduling(SLAPartialPlanScheduling schedule) {
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
	}

	@Override
	public void clear() {
		this.plans.clear();
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		this.plans.add(scheduling);
	}

	@Override
	public int planCount() {
		return this.plans.size();
	}

	@Override
	public IScheduling nextPlan() {
		// check for sla violation and fire events
		while (!this.eventQueue.isEmpty()) {
			this.fireSLAViolationEvent(this.eventQueue.pop());
		}

		IScheduling next = null;
		double nextPrio = 0;

		for (IScheduling scheduling : this.plans) {
			// calculate sla conformance for all partial plans
			IPartialPlan plan = scheduling.getPlan();
			SLARegistryInfo data = this.registry.getData(plan);
			SLA sla = data.getSla();
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
			/*
			 * TODO: Generalize plan selection: select x plans with best
			 * priorities to reduce calculation overhead
			 * 
			 * such an optimization needs the constraint of selecting only a
			 * certain percentage of the existing partial plans. otherwise the
			 * this strategy would be the same as round robin with a certain
			 * ordering of the plans!
			 */
			if (prio > nextPrio) {
				next = scheduling;
				nextPrio = prio;
			}
		}

		// return selected plan(s)
		return next;
	}

	@Override
	public void removePlan(IScheduling plan) {
		this.plans.remove(plan);
	}

	@Override
	public IPartialPlanScheduling clone() {
		return new SLAPartialPlanScheduling(this);
	}

	public void addSLAViolationEventListener(ISLAViolationEventListener listener) {
		this.listeners.add(listener);
	}

	public boolean removeSLAViolationEventListener(
			ISLAViolationEventListener listener) {
		return this.listeners.remove(listener);
	}

	private void fireSLAViolationEvent(SLAViolationEvent event) {
		for (ISLAViolationEventListener listener : this.listeners) {
			listener.slaViolated(event);
		}
	}

	@Override
	public void queueSLAViolationEvent(SLAViolationEvent event) {
		this.eventQueue.add(event);
	}

	public void setDecaySF(double decaySF) {
		this.decaySF = decaySF;
	}

	public double getDecaySF() {
		return decaySF;
	}

	public void setCostFunctionName(String costFunctionName) {
		this.costFunctionName = costFunctionName;
	}

	public String getCostFunctionName() {
		return costFunctionName;
	}
}
