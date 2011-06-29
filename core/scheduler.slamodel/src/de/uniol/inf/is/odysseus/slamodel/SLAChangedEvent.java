package de.uniol.inf.is.odysseus.slamodel;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

/**
 * Event marking the change of a sla and related data. currently only adding and
 * removing of sla-related partial plans are possible
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAChangedEvent {
	/**
	 * The type of the event
	 */
	private SLAChangedEventType type;
	/**
	 * the sla effected by the event
	 */
	private SLA sla;
	/**
	 * the partial plan effected by the event
	 */
	private IPartialPlan plan;

	/**
	 * creates a new event
	 * @param type the vent type
	 * @param sla the sla effected by this event
	 * @param plan the partial plan effected by the event
	 */
	public SLAChangedEvent(SLAChangedEventType type, SLA sla, IPartialPlan plan) {
		super();
		this.type = type;
		this.sla = sla;
		this.plan = plan;
	}

	/**
	 * @return the type of the event
	 */
	public SLAChangedEventType getType() {
		return type;
	}

	/**
	 * @return the sla effected by the event
	 */
	public SLA getSla() {
		return sla;
	}

	/**
	 * @return the plan effected by the event 
	 */
	public IPartialPlan getPlan() {
		return plan;
	}

}
