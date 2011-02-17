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
package de.uniol.inf.is.odysseus.parser.pql.benchmark;

import de.uniol.inf.is.odysseus.benchmarker.impl.PrioIdJoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PrioIdJoinAOBuilder extends AbstractOperatorBuilder {

	ResolvedSDFAttributeParameter leftAttribute = new ResolvedSDFAttributeParameter(
			"LEFT_ATTRIBUTE", REQUIREMENT.MANDATORY);
	ResolvedSDFAttributeParameter rightAttribute = new ResolvedSDFAttributeParameter(
			"RIGHT_ATTRIBUTE", REQUIREMENT.MANDATORY);

	public PrioIdJoinAOBuilder() {
		super(2, 2);
		setParameters(leftAttribute, rightAttribute);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		int leftPos = getInputOperator(0).getOutputSchema().indexOf(
				leftAttribute.getValue());
		int rightPos = getInputOperator(1).getOutputSchema().indexOf(
				rightAttribute.getValue());
		PrioIdJoinAO op = new PrioIdJoinAO();
		op.setLeftPos(leftPos);
		op.setRightPos(rightPos);
		
		return op;
	}

	@Override
	protected boolean internalValidation() {
		SDFAttributeList leftSchema = getInputOperator(0).getOutputSchema();
		SDFAttributeList rightSchema = getInputOperator(1).getOutputSchema();
		SDFAttribute leftAttributeValue = leftAttribute.getValue();
		SDFAttribute rightAttributeValue = rightAttribute.getValue();

		boolean isValid = true;
		if (!leftSchema.contains(leftAttributeValue)) {
			isValid = false;
			addError(new IllegalArgumentException(
					"no such attribute in first input: " + leftAttributeValue));
		}
		if (!rightSchema.contains(rightAttributeValue)) {
			isValid = false;
			addError(new IllegalArgumentException(
					"no such attribute in second input: " + rightAttributeValue));
		}

		return isValid;
	}

}
