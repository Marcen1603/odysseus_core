package de.uniol.inf.is.odysseus.core.planmanagement.query;

import de.uniol.inf.is.odysseus.core.sla.SLA;

public interface IProvidesSLA {
	/**
	 * returns the query's service level agreement
	 * @return
	 */
	public SLA getSLA();
	
	/**
	 * sets the service level agreement that is defined for the query
	 * @param sla the serivce level agreement to set for the query
	 */
	public void setSLA(SLA sla);
}
