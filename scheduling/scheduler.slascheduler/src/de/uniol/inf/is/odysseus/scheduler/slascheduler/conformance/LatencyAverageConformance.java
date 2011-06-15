package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AvgSumPartialAggregate;

public class LatencyAverageConformance<T> extends AbstractSLaConformance<T> {
	
	private AvgSumPartialAggregate<T> aggregate;
	
	public LatencyAverageConformance() {
		this.aggregate = new AvgSumPartialAggregate<T>(0.0, 0);
	}
	
	public LatencyAverageConformance(LatencyAverageConformance<T> conformance) {
		super();
		this.aggregate = conformance.aggregate.clone();
	}

	@Override
	public int getConformance() {
		return this.aggregate.getAggValue().intValue();
	}

	@Override
	public void reset() {
		this.aggregate.setAggValue(0.0, 0);
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
