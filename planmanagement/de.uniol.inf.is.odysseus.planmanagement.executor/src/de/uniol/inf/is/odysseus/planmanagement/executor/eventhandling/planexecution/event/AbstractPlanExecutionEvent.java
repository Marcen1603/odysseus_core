package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling;

public abstract class AbstractPlanExecutionEvent<ValueType> extends
		AbstractEvent<IPlanScheduling, ValueType> {

	public AbstractPlanExecutionEvent(IPlanScheduling sender, String id, ValueType value) {
		super(sender, id, value);
	}
}
