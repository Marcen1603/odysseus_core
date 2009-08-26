package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling;

public class PlanExecutionEvent extends
	AbstractPlanExecutionEvent<String> {

	public static String EXECUTION_STOPPED = "EXECUTION_STOPPED";
	public static String EXECUTION_STARTED = "EXECUTION_STARTED";

	public PlanExecutionEvent(IPlanScheduling sender, String id) {
		super(sender, id, "");
	}
}
