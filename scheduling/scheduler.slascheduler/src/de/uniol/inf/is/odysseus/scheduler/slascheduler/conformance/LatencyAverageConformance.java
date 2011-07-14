package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.sla.SLA;

/**
 * Sla conformance for metric latency and scope average
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public class LatencyAverageConformance<T> extends AbstractSLaConformance<T> {
	/**
	 * partial aggregate for calculating teh average
	 */
	private AvgSumPartialAggregate<T> aggregate;

	/**
	 * creates a new sla conformance obejct for metric latency and scope average
	 * 
	 * @param dist
	 *            reference to the event ditributor to pass generated events
	 * @param sla
	 *            teh related sla
	 * @param query
	 *            the related query
	 */
	public LatencyAverageConformance(ISLAViolationEventDistributor dist,
			SLA sla, IQuery query) {
		super(dist, sla, query);
		this.aggregate = new AvgSumPartialAggregate<T>(0.0, 0);

	}

	/**
	 * copy constructor for clone method
	 * 
	 * @param conformance
	 *            the object to clone
	 */
	private LatencyAverageConformance(LatencyAverageConformance<T> conformance) {
		super(conformance);
		this.aggregate = conformance.aggregate.clone();
	}

	/**
	 * returns the average latency of the elements processed by the related
	 * query
	 */
	@Override
	public double getConformance() {
		double avg = this.aggregate.getAggValue().doubleValue()
				/ this.aggregate.getCount();
		if (Double.isNaN(avg))
			return 0.0;
		else
			return avg;
	}

	/**
	 * resets the aggregated average
	 */
	@Override
	public void reset() {
		this.aggregate.setAggValue(0.0, 0);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// nothing to do
	}

	/**
	 * measures the average tuple latency
	 */
	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		// first check for sla violation and create event in case of violation
		this.checkViolation();

		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			this.aggregate.addAggValue(this.nanoToMilli((double) latency
					.getLatency()));
		} else {
			throw new RuntimeException("Latency missing");
		}
	}

	/**
	 * copy object
	 */
	@Override
	public AbstractSink<T> clone() {
		return new LatencyAverageConformance<T>(this);
	}

}
