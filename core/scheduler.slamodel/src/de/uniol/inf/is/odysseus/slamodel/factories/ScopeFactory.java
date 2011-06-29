package de.uniol.inf.is.odysseus.slamodel.factories;

import de.uniol.inf.is.odysseus.slamodel.Scope;
import de.uniol.inf.is.odysseus.slamodel.scope.Average;
import de.uniol.inf.is.odysseus.slamodel.scope.Number;
import de.uniol.inf.is.odysseus.slamodel.scope.Single;

public class ScopeFactory {
	
	public static final String SCOPE_AVERAGE = "average";
	public static final String SCOPE_NUMBER = "number";
	public static final String SCOPE_SINGLE = "single";
	
	public Scope buildScope(String scopeID) {
		if (SCOPE_AVERAGE.equals(scopeID)) {
			return new Average();
		} else if (SCOPE_NUMBER.equals(scopeID)) {
			return new Number();
		} else if (SCOPE_SINGLE.equals(scopeID)) {
			return new Single();
		} else {
			throw new RuntimeException("unknown scope: " + scopeID);
		}
	}

}
