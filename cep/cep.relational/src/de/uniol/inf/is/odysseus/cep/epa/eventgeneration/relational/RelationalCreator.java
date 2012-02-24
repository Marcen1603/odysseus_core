/** Copyright [2011] [The Odysseus Team]
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
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalCreator<R> extends AbstractComplexEventFactory<R,RelationalTuple<? extends ITimeInterval>> {

	/**
	 * Erzeugt einen neuen Creator für relationale Tupel vom Typ
	 * {@link RelationalTuple}.
	 */
	public RelationalCreator() {
		super();
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	public RelationalTuple<? extends ITimeInterval> createComplexEvent(OutputScheme outputscheme,
			MatchingTrace<R> matchingTrace, SymbolTable symTab, PointInTime timestamp) {
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

		RelationalTuple<TimeInterval> ret = new RelationalTuple<TimeInterval>(attributes);
		ret.setMetadata(new TimeInterval(timestamp));
		//System.out.println("EVENT "+ret);
		return ret;
	}

}
