package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
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
		// nothing to do
	}

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>)object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			this.aggregate.addAggValue((double)latency.getLatency());
		}
	}

	@Override
	public AbstractSink<T> clone() {
		return new LatencyAverageConformance<T>(this);
	}

}
