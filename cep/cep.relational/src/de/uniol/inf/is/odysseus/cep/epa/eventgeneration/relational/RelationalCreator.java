/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.cep.epa.eventgeneration.relational;

import de.uniol.inf.is.odysseus.cep.epa.MatchingTrace;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.AbstractComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTable;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

public class RelationalCreator<R extends IMetaAttributeContainer<?>> extends AbstractComplexEventFactory<R,Tuple<? extends ITimeInterval>> {

	/**
	 * Erzeugt einen neuen Creator für relationale Tupel vom Typ
	 * {@link RelationalTuple}.
	 */
	public RelationalCreator() {
		super();
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Tuple<? extends ITimeInterval> createComplexEvent(OutputScheme outputscheme,
			MatchingTrace<R> matchingTrace, SymbolTable symTab, R event) {
//		MatchedEvent<R> lastEvent = matchingTrace.getLastEvent();
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
			attributes[i] = outputscheme.getEntries().get(i).getValue();
		}

		Tuple ret = new Tuple(attributes, false);
		ret.setMetadata((IMetaAttribute) event.getMetadata().clone());
		//System.out.println("EVENT "+ret);
		return ret;
	}

}
