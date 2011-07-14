package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationEvent;
import de.uniol.inf.is.odysseus.sla.SLA;
import de.uniol.inf.is.odysseus.sla.ServiceLevel;

/**
 * abstract sla conformance super class. extends {@link AbstractSink} so the sla
 * conformance object could be added to a plan as a physical operator to
 * measure data inside the physical operator plan.
 * 
 * @author Thomas Vogelgesang
 * 
 * @param <T>
 */
public abstract class AbstractSLaConformance<T> extends AbstractSink<T>
		implements ISLAConformance {
	/**
	 * factor between nanoseconds and milliseconds
	 */
	private static final double NANO_TO_MILLI = 1000000.0;
	/**
	 * reference to the object, that distributes generated
	 * {@link SLAViolationEvent} to event listeners
	 */
	private ISLAViolationEventDistributor distributor;
	/**
	 * reference to the related sla
	 */
	private SLA sla;
	/**
	 * reference to the related query, required for violationEvent
	 * generation
	 */
	private IQuery query;
	/**
	 * timestamp marking the end of the evaluation window of the sla conformance
	 */
	private long windowEnd;

	/**
	 * default constructor
	 * 
	 * @param dist
	 *            event distributor, that sends generated
	 *            {@link SLAViolationEvent} to the listeners
	 * @param sla
	 *            the related sla
	 * @param query
	 *            the related query
	 */
	public AbstractSLaConformance(ISLAViolationEventDistributor dist, SLA sla,
			IQuery query) {
		this.distributor = dist;
		this.sla = sla;
		this.query = query;
		this.windowEnd = System.currentTimeMillis();
	}

	/**
	 * copy constructor, required for clone method
	 * 
	 * @param conformance
	 */
	protected AbstractSLaConformance(AbstractSLaConformance<T> conformance) {
		this.distributor = conformance.distributor;
		this.sla = conformance.sla;
		this.query = conformance.query;
		this.windowEnd = conformance.windowEnd;
	}

	/**
	 * @return the event distributor
	 */
	protected ISLAViolationEventDistributor getDistributor() {
		return this.distributor;
	}

	/**
	 * @return the related sla
	 */
	protected SLA getSLA() {
		return this.sla;
	}

	protected IQuery getQuery() {
		return query;
	}

	/**
	 * @return the end timestamp of the evaluation window
	 */
	protected long getWindowEnd() {
		return this.windowEnd;
	}

	/**
	 * creates a SLAViolationEvent and puts it to the event queue of the
	 * scheduler
	 * 
	 * @param cost
	 *            the cost caused by violating certain service levels
	 */
	private void violation(double cost) {
		SLAViolationEvent event = new SLAViolationEvent(this.query, cost);
		this.distributor.queueSLAViolationEvent(event);
	}

	/**
	 * checks if a window has reached its end. If a violation is detected an
	 * event will be fired and queued in the event queue of the scheduler. if a
	 * window's end has been reached a new window will be set and all results of
	 * conformance are reset.
	 */
	protected void checkViolation() {
		/*
		 * most valuable service level is first list entry! so iterate reverse
		 * over list to finde the less valuable violated service level first
		 */
		if (System.currentTimeMillis() >= this.windowEnd) {
			List<ServiceLevel> serviceLevels = this.getSLA()
					.getServiceLevel();
			for (int i = serviceLevels.size() - 1; i >= 0; i--) {
				if (this.getSLA().getMetric().valueIsMin()) {
					if (serviceLevels.get(i).getThreshold() < this
							.getConformance()) {
						this.violation(serviceLevels.get(i).getPenalty()
								.getCost());
						break;
					}
				} else {
					if (serviceLevels.get(i).getThreshold() > this
							.getConformance()) {
						this.violation(serviceLevels.get(i).getPenalty()
								.getCost());
						break;
					}
				}
			}
			this.windowEnd += this.getSLA().getWindow().lengthToMilliseconds();
			this.reset();
		}
	}
	
	protected double nanoToMilli(double value) {
		return value / NANO_TO_MILLI;
	}
}
