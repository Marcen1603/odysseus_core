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
package de.uniol.inf.is.odysseus.cep.epa.metamodel.relational;

import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.MEPCondition;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalMEPCondition extends MEPCondition {

	public RelationalMEPCondition(String jepExpression){
		super(jepExpression);
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	public void setValue(CepVariable varName, Object newValue) {
		super.setValue(varName,((RelationalTuple)newValue).getAttribute(0));
	}

	
}
