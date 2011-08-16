package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.sla.SLA;

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
	private double maxLatency;

	/**
	 * creates a new sla conformance for metric latency and scope single
	 * 
	 * @param dist
	 *            distributor to send events to event listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 */
	public LatencySingleConformance(ISLAViolationEventDistributor dist,
			SLA sla, IQuery query) {
		super(dist, sla, query);
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
		super.reset();
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
		super.process_next(object, port, isReadOnly);
		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			if (latency.getLatency() > this.maxLatency) {
				this.maxLatency = nanoToMilli(latency.getLatency());
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
