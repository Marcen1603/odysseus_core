package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

/**
 * Sla conformance for metric latency and scope number
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencyNumberConformance<T> extends AbstractSLaConformance<T> {
	/**
	 * counts the number of violating the specified latency
	 */
	private int numberOfViolations;
	/**
	 * threshold of the latency that should not be exceeded
	 */
	private double latencyThreshold;

	/**
	 * creates a new sla conformance for metric latency and scope number
	 * 
	 * @param dist
	 *            event distributor to send generated events to event listener
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 * @param latencyThreshold
	 *            latency threshold that should not be exceeded
	 */
	public LatencyNumberConformance(ISLAViolationEventDistributor dist,
			SLA sla, IPhysicalQuery query, double latencyThreshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.latencyThreshold = latencyThreshold;
	}

	/**
	 * copy constructor, required for clone
	 * 
	 * @param conformance
	 *            object to clone
	 */
	private LatencyNumberConformance(LatencyNumberConformance<T> conformance) {
		super(conformance);
		this.numberOfViolations = conformance.numberOfViolations;
	}

	/**
	 * returns the conformance as the number of violations
	 */
	@Override
	public double getConformance() {
		return numberOfViolations;
	}

	/**
	 * resets the counter for the number of violations
	 */
	@Override
	public void reset() {
		super.reset();
		this.numberOfViolations = 0;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// nothing to do
	}

	/**
	 * measures the latency and counts how often it exceeds the threshold
	 */
	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		super.process_next(object, port, isReadOnly);
		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			if (this.nanoToMilli(latency.getLatency()) > latencyThreshold) {
				this.numberOfViolations++;
			}
		}
	}

	/**
	 * returns a copy of the object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencyNumberConformance<T>(this);
	}

	@Override
	public double predictConformance() {
		return this.numberOfViolations + this.getNumberOfViolationsPredictedLatency();
	}

}
