package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution;

import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;

public interface IPlanExecutionListener {
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs);
}
