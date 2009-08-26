package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution;

public interface IPlanExecutionHandler {
	public void addPlanExecutionListener(IPlanExecutionListener listener);

	public void removePlanExecutionListener(IPlanExecutionListener listener);
}
