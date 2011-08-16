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
 * Sla conformance for metric latency and scope rate
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencyRateConformance<T> extends AbstractSLaConformance<T> {
	/**
	 * counts the number of violating the specified latency
	 */
	private int numberOfViolations;
	/**
	 * counts the number of all processed elements
	 */
	private int totalNumber;
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
	public LatencyRateConformance(ISLAViolationEventDistributor dist,
			SLA sla, IQuery query, double latencyThreshold) {
		super(dist, sla, query);
		this.numberOfViolations = 0;
		this.totalNumber = 0;
		this.latencyThreshold = latencyThreshold;
	}

	/**
	 * copy constructor, required for clone
	 * 
	 * @param conformance
	 *            object to clone
	 */
	private LatencyRateConformance(LatencyRateConformance<T> conformance) {
		super(conformance);
		this.numberOfViolations = conformance.numberOfViolations;
		this.latencyThreshold = conformance.latencyThreshold;
		this.totalNumber = conformance.totalNumber;
	}

	/**
	 * returns the conformance as the number of violations
	 */
	@Override
	public double getConformance() {
		if (this.totalNumber != 0) {
			return (double)(this.totalNumber - this.numberOfViolations) / (double)this.totalNumber;
		} else {
			return 1.0;
		}
	}

	/**
	 * resets the counter for the number of violations
	 */
	@Override
	public void reset() {
		super.reset();
		this.numberOfViolations = 0;
		this.totalNumber = 0;
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
			this.totalNumber++;
		}
	}

	/**
	 * returns a copy of the object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencyRateConformance<T>(this);
	}

}
