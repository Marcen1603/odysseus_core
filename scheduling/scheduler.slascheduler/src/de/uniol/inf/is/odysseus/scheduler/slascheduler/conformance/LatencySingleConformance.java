package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;

public class LatencySingleConformance<T> extends AbstractSLaConformance<T> {
	
	private int maxLatency;
	
	public LatencySingleConformance() {
		this.maxLatency = 0;
	}
	
	public LatencySingleConformance(LatencySingleConformance<T> conformance) {
		super();
		this.maxLatency = conformance.maxLatency;
	}

	@Override
	public int getConformance() {
		return this.maxLatency;
	}

	@Override
	public void reset() {
		this.maxLatency = 0;
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
		return new LatencySingleConformance<T>(this);
	}

}
