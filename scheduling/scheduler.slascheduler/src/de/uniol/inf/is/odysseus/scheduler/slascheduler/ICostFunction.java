package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.core.server.sla.SLA;

/**
 * Interface for priority functions
 * @author Thomas Vogelgesang
 *
 */
public interface ICostFunction {
	/**
	 * calculates the opportunity costs for a partial plan
	 * @param conformance the current sla conformance of the partial plan
	 * @param sla the sla defined for the partial plan
	 * @return the opportunity costs for the partial plan
	 */
	public double oc(double conformance, SLA sla);
	
	/**
	 * calculates the marginal gain for a partial plan
	 * @param conformance the current sla conformance of the partial plan
	 * @param sla the sla defined for the partial plan
	 * @return the marginal gain for the partial plan
	 */
	public double mg(double conformance, SLA sla);

}
