package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

public class LatencyAverageConformance<T> extends AbstractSLaConformance<T> {

	private AvgSumPartialAggregate<T> aggregate;

	public LatencyAverageConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPartialPlan plan) {
		super(dist, sla, plan, System.currentTimeMillis());
		this.aggregate = new AvgSumPartialAggregate<T>(0.0, 0);
		
	}

	public LatencyAverageConformance(LatencyAverageConformance<T> conformance) {
		super(conformance.getDistributor(), conformance.getSLA(), conformance
				.getPlan(), conformance.getWindowEnd());
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
		// first check for sla violation and create event in case of violation
		this.checkViolation();

		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			this.aggregate.addAggValue((double) latency.getLatency());
		} else {
			throw new RuntimeException("Latency missing");
		}
	}

	@Override
	public AbstractSink<T> clone() {
		return new LatencyAverageConformance<T>(this);
	}

}
