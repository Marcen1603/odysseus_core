package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.sla.SLA;

/**
 * Event, marking the violation of an sla
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class SLAViolationEvent {
	/**
	 * the query, the violated sla was defined for
	 */
	private IQuery query;
	/**
	 * the costs caused by the violation due to predefined penalties
	 */
	private double cost;
	/**
	 * the service level agreement that has been violated
	 */
	private SLA sla;

	/**
	 * creates a new sla violation event
	 * @param query the query, the violated sla was defined for
	 * @param sla the violated sla
	 * @param cost the costs caused by the violation
	 */
	public SLAViolationEvent(IQuery query, SLA sla, double cost) {
		this.query = query;
		this.sla = sla;
		this.cost = cost;
	}

	/**
	 * @return the query, the violated sla was defined for
	 */
	public IQuery getQuery() {
		return query;
	}
	
	/**
	 * @return the costs caused by the sla violation
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @return the violated sla
	 */
	public SLA getSla() {
		return sla;
	}

}
