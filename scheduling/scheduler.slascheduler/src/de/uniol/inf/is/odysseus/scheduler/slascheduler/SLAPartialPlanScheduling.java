package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SLAPartialPlanScheduling implements IPartialPlanScheduling, ISLAViolationEventDistributor {

	private int trainSize;

	private List<ISLAViolationEventListener> listeners;

	private List<IScheduling> plans;

	private SLARegistry registry;

	private IStarvationFreedom starvationFreedom;

	private IPriorityFunction prioFunction;

	private LinkedList<SLAViolationEvent> eventQueue;

	public SLAPartialPlanScheduling(IStarvationFreedom sf,
			IPriorityFunction prio) {
		this.plans = new ArrayList<IScheduling>();
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		this.registry = new SLARegistry();
		this.starvationFreedom = sf;
		this.prioFunction = prio;
		this.eventQueue = new LinkedList<SLAViolationEvent>();
	}

	@SuppressWarnings("unchecked")
	public SLAPartialPlanScheduling(SLAPartialPlanScheduling schedule) {
		this.trainSize = schedule.trainSize;
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
		IScheduling next = null;
		int nextPrio = 0;
		
		for (IScheduling scheduling : this.plans) {
			// calculate sla conformance for all partial plans
			IPartialPlan plan = scheduling.getPlan();
			SLA sla = this.registry.getSLA(plan);
			int conformance = this.registry.getConformance(plan).getConformance();
			// calculate priorities for all partial plans:
			// - calculate oc
			ICostFunction costFunc = this.registry.getCostFunction(plan);
			int oc = costFunc.oc(conformance, sla);
			
			// - calculate mg
			int mg = costFunc.oc(conformance, sla);

			// - calculate sf
			int sf = this.starvationFreedom.sf();
			
			// - calculate prio
			int prio = this.prioFunction.calcPriority(oc, mg, sf);
			
			// check for sla violation and fire event
			while (!this.eventQueue.isEmpty()) {
				this.fireSLAViolationEvent(this.eventQueue.pop());
			}

			// select plan with highest priority
			/* 
			 * TODO: Generalize plan selection: select x plans with best 
			 * priorities to reduce calculation overhead
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

	public void setTrainSize(int trainSize) {
		this.trainSize = trainSize;
	}

	public int getTrainSize() {
		return trainSize;
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

}
