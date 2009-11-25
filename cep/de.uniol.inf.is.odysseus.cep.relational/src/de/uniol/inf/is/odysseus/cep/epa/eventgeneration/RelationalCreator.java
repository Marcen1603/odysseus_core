package de.uniol.inf.is.odysseus.cep.epa.eventgeneration;

import de.uniol.inf.is.odysseus.cep.epa.MatchingTrace;
import de.uniol.inf.is.odysseus.cep.epa.SymbolTable;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalCreator extends ComplexEventFactory {

	/**
	 * Erzeugt einen neuen Creator für relationale Tupel vom Typ
	 * {@link RelationalTuple}.
	 */
	public RelationalCreator() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object createComplexEvent(OutputScheme outputscheme,
			MatchingTrace matchingTrace, SymbolTable symTab) {
		Object[] attributes = new Object[outputscheme.getEntries().size()];
		for (int i = 0; i < outputscheme.getEntries().size(); i++) {
			/*
			 * Ist der geparste Ausdruck nur ein Variablenname, so kann mittels
			 * getValueAsObject jedes beliebige Objekt über einen JEp-Ausdruck
			 * weitergegeben werden (siehe JEPTest.java)
			 */
			attributes[i] = outputscheme.getEntries().get(i).getExpression()
					.getValueAsObject();
		}
		return new RelationalTuple(attributes);
	}

}
