package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling;

/**
 * PlanExecutionEvent is an event that occurs during changes of plan execution.
 * It refers to no value.
 * 
 * @author Wolf Bauer
 * 
 */
public class PlanExecutionEvent extends AbstractPlanExecutionEvent<PlanExecutionEventType> {

	/**
	 * Constructor of PlanExecutionEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 */
	public PlanExecutionEvent(IPlanScheduling sender, PlanExecutionEventType eventType) {
		super(sender, eventType, eventType);
	}
}
