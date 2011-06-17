package de.uniol.inf.is.odysseus.scheduler.slascheduler.conformance;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slamodel.ServiceLevel;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAConformance;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ISLAViolationEventDistributor;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.SLAViolationEvent;

public abstract class AbstractSLaConformance<T> extends AbstractSink<T> implements ISLAConformance {
	
	private ISLAViolationEventDistributor distributor;
	
	private SLA sla;
	// required for violationEvent generation
	private IPartialPlan plan;

	private long windowEnd;
	
	public AbstractSLaConformance(ISLAViolationEventDistributor dist, SLA sla, IPartialPlan plan, long windowEnd) {
		this.distributor = dist;
		this.sla = sla;
		this.plan = plan;
		this.windowEnd = windowEnd;
	}
	
	protected ISLAViolationEventDistributor getDistributor() {
		return this.distributor;
	}
	
	protected SLA getSLA() {
		return this.sla;
	}
	
	protected IPartialPlan getPlan() {
		return this.plan;
	}
	
	protected long getWindowEnd() {
		return this.windowEnd;
	}
	
	/**
	 * creates a SLAViolationEvent and puts it to the event queue of the scheduler
	 * @param cost
	 */
	private void violation(int cost) {
		SLAViolationEvent event = new SLAViolationEvent(this.plan, this.sla, cost);
		this.distributor.queueSLAViolationEvent(event);
	}
	
	/**
	 * checks if a window has reached its end. If a violation is detected an
	 * event will be fired and queued in the event queue of the scheduler. if
	 * a window's end has been reached a new window will be set and all results
	 * of conformance are reset.
	 */
	protected void checkViolation() {
		if (System.currentTimeMillis() >= this.windowEnd) {
			for (ServiceLevel<?> sl : this.getSLA().getServiceLevel()) {
				if (this.getSLA().getMetric().valueIsMin()) {
					if ((Integer) sl.getThreshold() < this.getConformance()) {
						this.violation(sl.getPenalty().getCost());
						break;
					}
				} else {
					if ((Integer) sl.getThreshold() > this.getConformance()) {
						this.violation(sl.getPenalty().getCost());
						break;
					}
				}
			}
			this.windowEnd += this.getSLA().getWindow().lengthToMilliseconds();
			this.reset();
		}
	}
}
