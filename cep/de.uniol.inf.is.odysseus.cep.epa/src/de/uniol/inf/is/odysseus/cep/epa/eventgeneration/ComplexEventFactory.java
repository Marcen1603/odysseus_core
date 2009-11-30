package de.uniol.inf.is.odysseus.cep.epa.eventgeneration;

import de.uniol.inf.is.odysseus.cep.epa.MatchingTrace;
import de.uniol.inf.is.odysseus.cep.epa.SymbolTable;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;

public abstract class ComplexEventFactory<R,W> {

	abstract public W createComplexEvent(OutputScheme outputscheme, MatchingTrace<R> matchingTrace, SymbolTable symTab);
	
}
