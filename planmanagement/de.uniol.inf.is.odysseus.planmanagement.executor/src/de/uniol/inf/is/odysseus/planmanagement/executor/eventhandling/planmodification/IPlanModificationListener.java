package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification;

import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;

public interface IPlanModificationListener {
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs);
}
