package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.slamodel.SLA;

/**
 * Event, marking the violation of an sla
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAViolationEvent {
	/**
	 * the plan, the violated sla was defined for
	 */
	private IPartialPlan plan;
	/**
	 * the costs caused by the violation due to predefined penalties
	 */
	private double cost;
	/**
	 * the service level agreement that has been violated
	 */
	private SLA sla;

	/**
	 * creates a new sla violation event
	 * @param plan the plan, the violated sla was defined for
	 * @param sla the violated sla
	 * @param cost the costs caused by the violation
	 */
	public SLAViolationEvent(IPartialPlan plan, SLA sla, double cost) {
		this.plan = plan;
		this.sla = sla;
		this.cost = cost;
	}

	/**
	 * @return the plan, the violated sla was defined for
	 */
	public IPartialPlan getPlan() {
		return plan;
	}

	/**
	 * @return the costs caused by the sla violation
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @return the violated sla
	 */
	public SLA getSla() {
		return sla;
	}

}
