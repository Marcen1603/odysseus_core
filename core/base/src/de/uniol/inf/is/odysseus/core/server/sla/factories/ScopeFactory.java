package de.uniol.inf.is.odysseus.core.server.sla.factories;

import de.uniol.inf.is.odysseus.core.server.sla.Scope;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Average;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Number;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Rate;
import de.uniol.inf.is.odysseus.core.server.sla.scope.Single;

public class ScopeFactory {
	
	public static final String SCOPE_AVERAGE = "average";
	public static final String SCOPE_NUMBER = "number";
	public static final String SCOPE_SINGLE = "single";
	public static final String SCOPE_RATE = "rate";
	
	public Scope buildScope(String scopeID) {
		if (SCOPE_AVERAGE.equals(scopeID)) {
			return new Average();
		} else if (SCOPE_NUMBER.equals(scopeID)) {
			return new Number();
		} else if (SCOPE_SINGLE.equals(scopeID)) {
			return new Single();
		} else if (SCOPE_RATE.equals(scopeID)) {
			return new Rate();
		} else {
			throw new RuntimeException("unknown scope: " + scopeID);
		}
	}

}
