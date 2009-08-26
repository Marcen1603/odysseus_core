package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

public abstract class AbstractPlanModificationEvent<ValueType> extends
		AbstractEvent<IPlanManager, ValueType> {

	public AbstractPlanModificationEvent(IPlanManager sender, String id, ValueType value) {
		super(sender, id, value);
	}
}
