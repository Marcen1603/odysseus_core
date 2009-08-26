package de.uniol.inf.is.odysseus.interval_latency_priority;

import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;

public class IntervalLatencyPriorityFactory implements IMetadataFactory<IntervalLatencyPriority, Object>{

	@Override
	public IntervalLatencyPriority createMetadata(Object inElem) {
		return new IntervalLatencyPriority();
	}

	@Override
	public IntervalLatencyPriority createMetadata() {
		return new IntervalLatencyPriority();
	}

}
