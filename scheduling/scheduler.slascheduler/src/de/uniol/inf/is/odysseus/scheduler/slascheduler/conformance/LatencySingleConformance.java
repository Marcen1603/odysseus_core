package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

public class LatencySingleConformance<T> extends AbstractSLaConformance<T> {
	
	private int maxLatency;
	
	public LatencySingleConformance(ISLAViolationEventDistributor dist, SLA sla, IPartialPlan plan) {
		super(dist, sla, plan);
		this.maxLatency = 0;
	}
	
	public LatencySingleConformance(LatencySingleConformance<T> conformance) {
		super(conformance.getDistributor(), conformance.getSLA(), conformance.getPlan());
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
		// nothing to do
	}

	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>)object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			if (latency.getLatency() > this.maxLatency) {
				this.maxLatency = (int) latency.getLatency();
			}
		}
	}

	@Override
	public AbstractSink<T> clone() {
		return new LatencySingleConformance<T>(this);
	}

}
