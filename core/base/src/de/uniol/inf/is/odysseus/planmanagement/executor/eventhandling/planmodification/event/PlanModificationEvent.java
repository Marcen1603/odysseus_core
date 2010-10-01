package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;

/**
 * PlanModificationEvent is an event that occurs during modification of
 * the registered execution plan. It refers to an {@link IPlan}.
 * 
 * @author Wolf Bauer
 *
 */
public class PlanModificationEvent extends
		AbstractPlanModificationEvent<IPlan> {

	/**
	 * ID for an event after reoptimizing the execution plan.
	 */
	public static String PLAN_REOPTIMIZE = "PLAN_REOPTIMIZE";

	/**
	 * Constructor of PlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            {@link IPlan} to which this event refers.
	 */
	public PlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType,
			IPlan value) {
		super(sender, eventType, value);
	}
}
