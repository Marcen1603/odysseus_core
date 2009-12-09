package de.uniol.inf.is.odysseus.cep.epa.eventgeneration.relational;

import de.uniol.inf.is.odysseus.cep.epa.MatchingTrace;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.AbstractComplexEventFactory;

public class RelationalCreator<R> extends AbstractComplexEventFactory<R,RelationalTuple<?>> {

	/**
	 * Erzeugt einen neuen Creator für relationale Tupel vom Typ
	 * {@link RelationalTuple}.
	 */
	public RelationalCreator() {
		super();
	}

	@Override
	public RelationalTuple<?> createComplexEvent(OutputScheme outputscheme,
			MatchingTrace<R> matchingTrace, SymbolTable symTab) {
		
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("RelationalCreator ");
		System.out.println("MatchingTrace "+ matchingTrace);
		System.out.println("SymbolTable "+ symTab);
		
		Object[] attributes = new Object[outputscheme.getEntries().size()];
		for (int i = 0; i < outputscheme.getEntries().size(); i++) {
			/*
			 * Ist der geparste Ausdruck nur ein Variablenname, so kann mittels
			 * getValueAsObject jedes beliebige Objekt über einen JEp-Ausdruck
			 * weitergegeben werden (siehe JEPTest.java)
			 */
			attributes[i] = outputscheme.getEntries().get(i).getValueAsObject();
		}
		// TODO: Metadaten setzen, nur wie?
		return new RelationalTuple(attributes);
	}

}
