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
package de.uniol.inf.is.odysseus.cep.epa.symboltable.relational;

import java.util.HashMap;

import java.util.Map;

import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.ISymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalAvgSum;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalCount;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMinMax;
@SuppressWarnings({"rawtypes","unchecked"})
public class RelationalSymbolTableOperationFactory implements
		ISymbolTableOperationFactory {
	Map<String, IAggregateFunction> cache = new HashMap<String, IAggregateFunction>();

	@Override
	public IAggregateFunction getOperation(String name) {
		IAggregateFunction func = cache.get(name);
		if (func == null){
			func = createNewFunction(name);;
			cache.put(name, func);
		}
		return func;
	}

	private IAggregateFunction createNewFunction(String name) {
		IAggregateFunction func = null;
		if ("COUNT".equalsIgnoreCase(name)){
			func = RelationalCount.getInstance();
		}else if ("SUM".equalsIgnoreCase(name)){
			func = RelationalAvgSum.getInstance(0, false);
		}else if ("AVG".equalsIgnoreCase(name)){
			func = RelationalAvgSum.getInstance(0, true);
		}else if ("MAX".equalsIgnoreCase(name)){
			func = RelationalMinMax.getInstance(0, true);
		}else if ("MIN".equalsIgnoreCase(name)){
			func = RelationalMinMax.getInstance(0, false);
		}else if ("WRITE".equalsIgnoreCase(name)){
			func = Write.getInstance();
		}
		return func;
	}

}
