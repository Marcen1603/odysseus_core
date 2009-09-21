package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class IntervalPriority extends TimeInterval implements IPriority {

	private static final long serialVersionUID = -313473603482217362L;
	private byte priority = 0;

	public IntervalPriority() {
	}
	
	@Override
	public byte getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(byte priority) {
		this.priority = priority;
	}

}
