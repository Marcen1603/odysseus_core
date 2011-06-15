package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;

public class LatencyAverageConformance<T> extends AbstractSLaConformance<T> {
	
	public LatencyAverageConformance() {
		
	}
	
	public LatencyAverageConformance(LatencyAverageConformance<T> conformance) {
		super();
	}

	@Override
	public int getConformance() {
		// TODO not implemented yet
		return 0;
	}

	@Override
	public void reset() {
		// TODO not implemented yet
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO not implemented yet
	}

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		// TODO not implemented yet		
	}

	@Override
	public AbstractSink<T> clone() {
		return new LatencyAverageConformance<T>(this);
	}

}
