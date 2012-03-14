package de.uniol.inf.is.odysseus.core.server.sla.scope;

import de.uniol.inf.is.odysseus.core.server.sla.Scope;

/**
 * A Scope for sla. This scope means that a given metric value (saved in the @link
 * Metric) could be exceeded by a certain rate of processed elements (that is
 * specified in the threshold of the sla), before the service level gets
 * violated.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Rate extends Scope {

	@Override
	public boolean thresholdIsMin() {
		return true;
	}

}
