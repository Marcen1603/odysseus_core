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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class MapAOBuilder extends AbstractOperatorBuilder {

	private static final String EXPRESSIONS = "EXPRESSIONS";

	private ListParameter<SDFExpression> expressions = new ListParameter<SDFExpression>(
			EXPRESSIONS, REQUIREMENT.MANDATORY, new SDFExpressionParameter(
					"map expression", REQUIREMENT.MANDATORY));

	public MapAOBuilder() {
		super(1, 1);
		setParameters(expressions);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		List<SDFExpression> expressionList = expressions.getValue();
		MapAO mapAO = new MapAO();
		mapAO.setExpressions(expressionList);

		return mapAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
