package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

public class LatencyNumberConformance<T> extends AbstractSLaConformance<T> {
	
	private int numberOfViolations;
	
	private long latencyThreshold;
	
	public LatencyNumberConformance(ISLAViolationEventDistributor dist, SLA sla, IPartialPlan plan, long latencyThreshold) {
		super(dist, sla, plan);
		this.numberOfViolations = 0;
		this.latencyThreshold = latencyThreshold;
	}
	
	public LatencyNumberConformance(LatencyNumberConformance<T> conformance) {
		super(conformance.getDistributor(), conformance.getSLA(), conformance.getPlan());
		this.numberOfViolations = conformance.numberOfViolations;
	}

	@Override
	public int getConformance() {
		return numberOfViolations;
	}

	@Override
	public void reset() {
		this.numberOfViolations = 0;
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
			if (latency.getLatency() > latencyThreshold) {
				this.numberOfViolations++;
			}
		}
	}

	@Override
	public AbstractSink<T> clone() {
		return new LatencyNumberConformance<T>(this);
	}

}
