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
package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.mep.ParseException;
import de.uniol.inf.is.odysseus.mep.Variable;

public class MEPOutputSchemeEntry extends AbstractOutputSchemeEntry {

	IExpression<?> expression;
	protected Map<CepVariable, Variable> symbolTable = new HashMap<CepVariable, Variable>();
	
	public MEPOutputSchemeEntry(String label) {
		super(label);
		try{
			expression = MepHelper.initMEPExpressionFromLabel(label, symbolTable);
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<CepVariable> getVarNames() {
		return symbolTable.keySet();
	}

	@Override
	public Object getValue() {
		return expression.getValue();
	}

	@Override
	public void setValue(CepVariable varName, Object newValue) {
		symbolTable.get(varName).bind(newValue);
	}

}
