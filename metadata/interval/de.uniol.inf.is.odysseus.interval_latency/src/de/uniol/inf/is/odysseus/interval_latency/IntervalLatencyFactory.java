package de.uniol.inf.is.odysseus.interval_latency;

import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;

public class IntervalLatencyFactory implements IMetadataFactory<IntervalLatency, Object>{

	@Override
	public IntervalLatency createMetadata(Object inElem) {
		return createMetadata();
	}

	@Override
	public IntervalLatency createMetadata() {
		IntervalLatency il = new IntervalLatency();
		il.setLatencyStart(System.nanoTime());
		return il;
	}

}
