package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.slamodel.SLA;

/**
 * sla Conformance for metric latency and scope single
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencySingleConformance<T> extends AbstractSLaConformance<T> {
	/**
	 * the highest measured latency
	 */
	private long maxLatency;

	/**
	 * creates a new sla conformance for metric latency and scope single
	 * 
	 * @param dist
	 *            distributor to send events to event listeners
	 * @param sla
	 *            the related sla
	 * @param plan
	 *            the related partial plan
	 */
	public LatencySingleConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPartialPlan plan) {
		super(dist, sla, plan);
		this.maxLatency = 0;
	}

	/**
	 * copy constructor, required for clone method
	 * @param conformance object to copy
	 */
	private LatencySingleConformance(LatencySingleConformance<T> conformance) {
		super(conformance);
		this.maxLatency = conformance.maxLatency;
	}

	/**
	 * returns the conformancy as the highest measured latency
	 */
	@Override
	public double getConformance() {
		return this.maxLatency;
	}

	/**
	 * resets the measured maximum value for latency
	 */
	@Override
	public void reset() {
		this.maxLatency = 0;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// nothing to do
	}

	/**
	 * measures the latency and saves it, if it exceeds the current maximum
	 */
	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		// first check for sla violation and create event in case of violation
		this.checkViolation();

		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			if (latency.getLatency() > this.maxLatency) {
				this.maxLatency = latency.getLatency();
			}
		}
	}

	/**
	 * copies the object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencySingleConformance<T>(this);
	}

}
