package de.uniol.inf.is.odysseus.core.server.sla.penalty;

import de.uniol.inf.is.odysseus.core.server.sla.Penalty;
import de.uniol.inf.is.odysseus.core.server.sla.ServiceLevel;

/**
 * abstract super class for all penalties.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class AbstractPenalty implements Penalty {
	/**
	 * reference to the service level that contains the penalty
	 */
	private ServiceLevel serviceLevel;

	/**
	 * creates a new Penalty object
	 */
	public AbstractPenalty() {
		super();
	}

	/**
	 * sets the containing service level of the penalty
	 * 
	 * @param serviceLevel
	 *            the containing service level
	 */
	public void setServiceLevel(ServiceLevel serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	/**
	 * 
	 * @return the containing service level of the penalty
	 */
	public ServiceLevel getServiceLevel() {
		return serviceLevel;
	}

}
