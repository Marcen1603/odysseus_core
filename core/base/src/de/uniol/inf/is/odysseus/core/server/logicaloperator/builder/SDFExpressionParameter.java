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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

public class SDFExpressionParameter extends
		AbstractParameter<NamedExpressionItem> {

	private static final long serialVersionUID = -3129785072529123574L;

	@Override
	protected void internalAssignment() {
		String name;
		String expression;
		if (inputValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> in = ((List<String>) inputValue);
			if (in.size() == 2) {
				name = in.get(1);
				expression = in.get(0);
			} else {
				throw new RuntimeException(
						" Could not determine name/expression pair!");
			}
		} else {
			name = "";
			expression = (String) inputValue;
		}
		if (getAttributeResolver() != null) {
			setValue(new NamedExpressionItem(name, new SDFExpression("",
					expression, getAttributeResolver(), MEP.getInstance())));
		} else {
			throw new RuntimeException(
					"missing expression or attribute resolver");
		}

	}

	@Override
	protected String getPQLStringInternal() {
		return "'" + inputValue + "'";
	}
}
