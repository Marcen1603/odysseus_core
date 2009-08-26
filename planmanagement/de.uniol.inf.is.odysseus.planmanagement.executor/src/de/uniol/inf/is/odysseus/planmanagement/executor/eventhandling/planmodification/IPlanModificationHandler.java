package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification;

public interface IPlanModificationHandler {
	public void addPlanModificationListener(IPlanModificationListener listener);

	public void removePlanModificationListener(IPlanModificationListener listener);
}
