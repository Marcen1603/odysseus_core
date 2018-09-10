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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mep.MEP;

public class MepHelper {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static IMepExpression initMEPExpressionFromLabel(String label,
			Map<CepVariable, IMepVariable> symbolTable /* INOUT */,
			List<SDFSchema> schema) throws ParseException {

		String transformToMepVar = transformToMepVar(label);
		IMepExpression expression = MEP.getInstance().parse(transformToMepVar);

		Set<IMepVariable> v = expression.getVariables();
		if (schema != null) {
			// Try to find out, what are the datatypes of the variables
			// This is a hack and should be done in another way later!!
			List<SDFAttribute> tmpList = new ArrayList<SDFAttribute>();
			for (IMepVariable variable : v) {
				String varString = variable.toString();
				String attName = varString
						.substring(varString.lastIndexOf(".")+1);
				for (SDFSchema s : schema) {
					SDFAttribute sdfAttr = s.findAttribute(attName);
					if (sdfAttr != null) {
						SDFAttribute newAttr = new SDFAttribute("", varString,
								sdfAttr);
						tmpList.add(newAttr);
						break;
					}
				}
			}
			SDFSchema newSchema = SDFSchemaFactory.createNewWithAttributes(
					tmpList, schema.get(0));
			expression = MEP.getInstance().parse(transformToMepVar, newSchema);

			v = expression.getVariables();
		}

		for (IMepVariable s : v) {
			symbolTable.put(transformToOutVar(s.getIdentifier()), s);
		}
		return expression;
	}

	static public String transformToMepVar(String in) {
		String str = in.replace(
				CepVariable.getSeperator() + "-1" + CepVariable.getSeperator(),
				"a$0");
		str = str.replace(CepVariable.getSeperator(), "a$1");
		str = str.replace("[", "a$2");
		return str.replace("]", "a$3");
	}

	static public CepVariable transformToOutVar(String out) {
		String str = out.replace("a$0", CepVariable.getSeperator() + "-1"
				+ CepVariable.getSeperator());
		str = str.replace("a$1", CepVariable.getSeperator());
		str = str.replace("a$2", "[");
		str = str.replace("a$3", "]");
		return new CepVariable(str);
	}

}
