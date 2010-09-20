package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.base.IEventType;

public enum PlanExecutionEventType implements IEventType {
	EXECUTION_PREPARED, EXECUTION_STOPPED, EXECUTION_STARTED
}
