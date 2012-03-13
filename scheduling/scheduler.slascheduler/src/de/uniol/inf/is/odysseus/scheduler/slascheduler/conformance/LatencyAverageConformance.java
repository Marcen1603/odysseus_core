package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.AvgSumPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;

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
			SLA sla, IPhysicalQuery query) {
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

	@Override
	public double predictConformance() {
		double sumPredictedLatency = this.getSumPredictedLatency();
		double numberOfPredictedLatency = this.getNumberOfPredictedLatency();
		double conformance = (this.aggregate.getAggValue() + sumPredictedLatency)
				/ (this.aggregate.getCount() + numberOfPredictedLatency);
		if (Double.isNaN(conformance))
			return 0.0;
		else
		return conformance;
	}

	/**
	 * resets the aggregated average
	 */
	@Override
	public void reset() {
		super.reset();
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
		super.process_next(object, port, isReadOnly);
		MetaAttributeContainer<?> metaAttributeContainer = (MetaAttributeContainer<?>) object;
		IMetaAttribute metadata = metaAttributeContainer.getMetadata();
		if (metadata instanceof ILatency) {
			ILatency latency = (ILatency) metadata;
			this.aggregate.addAggValue(this.nanoToMilli(latency
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
