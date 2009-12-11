package de.uniol.inf.is.odysseus.cep.epa.eventgeneration.relational;

import de.uniol.inf.is.odysseus.cep.epa.MatchedEvent;
import de.uniol.inf.is.odysseus.cep.epa.MatchingTrace;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.AbstractComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

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
		MatchedEvent<R> lastEvent = matchingTrace.getLastEvent();
//		System.out.println("--------------------------------------------------------------------------");
//		System.out.println("RelationalCreator ");
//	
//		List<MatchedEvent<R>> eList = new LinkedList<MatchedEvent<R>>();
//		MatchedEvent<R> event = lastEvent;
//		eList.add(event);
//		while ((event = event.getPrevious()) != null){
//			eList.add(event);
//		}
//		Collections.reverse(eList);
//		System.out.println("Matched Events "+eList);		
//		System.out.println("--------------------------------------------------------------------------");
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
		RelationalTuple ret = new RelationalTuple(attributes);
		System.out.println("EVENT "+ret);
		return ret;
	}

}
