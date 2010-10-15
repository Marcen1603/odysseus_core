package de.uniol.inf.is.odysseus.scheduler.event;

import de.uniol.inf.is.odysseus.event.AbstractEvent;
import de.uniol.inf.is.odysseus.event.IEventType;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;

public class SchedulingEvent extends AbstractEvent<IScheduler, String> {
	
	public enum SchedulingEventType implements IEventType {
		SCHEDULING_STARTED, SCHEDULING_STOPPED
	}
	
	public SchedulingEvent(IScheduler sender, IEventType eventType,
			String value) {
		super(sender, eventType, value);
	}

}
