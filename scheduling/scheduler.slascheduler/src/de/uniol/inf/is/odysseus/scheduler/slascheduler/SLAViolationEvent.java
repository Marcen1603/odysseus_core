package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

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
	 * index of the hold service level
	 */
	private int serviceLevel;
	/**
	 * measured conformnace to sla
	 */
	private double conformance;

	/**
	 * creates a new sla violation event
	 * 
	 * @param query
	 *            the query, the violated sla was defined for
	 * @param cost
	 *            the costs caused by the violation
	 * @param serviceLevel
	 *            index of the hold service level
	 * @param conformance
	 *            measured conformnace to sla
	 */
	public SLAViolationEvent(IQuery query, double cost, int serviceLevel,
			double conformance) {
		this.query = query;
		this.cost = cost;
		this.serviceLevel = serviceLevel;
		this.conformance = conformance;
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
	 * @return the hold service level of the sla
	 */
	public int getServiceLevel() {
		return serviceLevel;
	}

	/**
	 * @return the measured conformance to sla
	 */
	public double getConformance() {
		return conformance;
	}

}
