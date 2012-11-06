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
package de.uniol.inf.is.odysseus.cep.metamodel;

import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;

public class MepHelper {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static IExpression initMEPExpressionFromLabel(String label, Map<CepVariable, Variable> symbolTable /*INOUT*/) throws ParseException {
		IExpression expression = MEP.getInstance().parse(transformToMepVar(label),null);
		Set<Variable> v = expression.getVariables();
		for (Variable s : v) {
			symbolTable.put(transformToOutVar(s.getIdentifier()), s);
		}
		return expression;
	}

	static public String transformToMepVar(String in) {
		String str = in.replace(CepVariable.getSeperator()+"-1"+CepVariable.getSeperator(), "a$0");
		str = str.replace(CepVariable.getSeperator(), "a$1");
		str = str.replace("[", "a$2");
		return str.replace("]", "a$3");
	}

	static public CepVariable transformToOutVar(String out) {
		String str = out.replace("a$0",CepVariable.getSeperator()+"-1"+CepVariable.getSeperator());
		str = str.replace("a$1", CepVariable.getSeperator());
		str = str.replace("a$2", "[");
		str = str.replace("a$3", "]");
		return new CepVariable(str);
	}
	
}
