package de.uniol.inf.is.odysseus.cep.epa.eventgeneration;

import de.uniol.inf.is.odysseus.cep.epa.MatchingTrace;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;

public interface IComplexEventFactory<R, W> {
	public W createComplexEvent(OutputScheme outputscheme, MatchingTrace<R> matchingTrace, SymbolTable symTab);
}
