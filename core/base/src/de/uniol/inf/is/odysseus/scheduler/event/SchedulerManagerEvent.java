package de.uniol.inf.is.odysseus.scheduler.event;

import de.uniol.inf.is.odysseus.event.AbstractEvent;
import de.uniol.inf.is.odysseus.event.IEventType;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;

public class SchedulerManagerEvent extends AbstractEvent<ISchedulerManager, IScheduler> {
	
	public enum SchedulerManagerEventType implements IEventType {
		SCHEDULER_REMOVED, SCHEDULER_SET, SCHEDULING_STRATEGY_ADDED, SCHEDULING_STRATEGY_REMOVED	
	}
	
	public SchedulerManagerEvent(ISchedulerManager sender, IEventType eventType,
			IScheduler value) {
		super(sender, eventType, value);
	}

}
