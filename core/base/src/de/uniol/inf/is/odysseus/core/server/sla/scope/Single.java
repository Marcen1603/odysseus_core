package de.uniol.inf.is.odysseus.core.server.sla.scope;

import de.uniol.inf.is.odysseus.core.sla.Scope;

/**
 * A scope for sla. This scope means that the service level gets violated if the
 * threshold is exceeded for a single time.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Single extends Scope {

	@Override
	public boolean thresholdIsMin() {
		return false;
	}

}
