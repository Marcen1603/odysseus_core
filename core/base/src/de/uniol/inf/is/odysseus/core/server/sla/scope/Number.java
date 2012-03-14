package de.uniol.inf.is.odysseus.core.server.sla.scope;

import de.uniol.inf.is.odysseus.core.server.sla.Scope;

/**
 * A Scope for sla. This scope means that a given metric value (saved in the @link
 * Metric) could be exceeded a certain number of times (that is specified in the
 * threshold of the sla), before the service level gets violated.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Number extends Scope {

	@Override
	public boolean thresholdIsMin() {
		return true;
	}

}
