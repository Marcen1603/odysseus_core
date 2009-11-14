package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling;

/**
 * PlanExecutionEvent is an event that occurs during changes of plan execution.
 * It refers to no value.
 * 
 * @author Wolf Bauer
 * 
 */
public class PlanExecutionEvent extends AbstractPlanExecutionEvent<String> {

	/**
	 * ID for an event after initializing the plan and before starting the execution.
	 */
	public static final String EXECUTION_PREPARED = "EXECUTION_PREPARED";

	/**
	 * ID for an event after stopping the execution.
	 */
	public static String EXECUTION_STOPPED = "EXECUTION_STOPPED";
	
	/**
	 * ID for an event after starting the execution.
	 */
	public static String EXECUTION_STARTED = "EXECUTION_STARTED";

	/**
	 * Constructor of PlanExecutionEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 */
	public PlanExecutionEvent(IPlanScheduling sender, String id) {
		super(sender, id, "");
	}
}
