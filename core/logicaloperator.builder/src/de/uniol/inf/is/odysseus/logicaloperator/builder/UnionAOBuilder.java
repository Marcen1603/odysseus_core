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

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnionAOBuilder extends AbstractOperatorBuilder {

	public UnionAOBuilder() {
		super(2, 999);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new UnionAO();
	}

	@Override
	protected boolean internalValidation() {
		int count = getInputOperatorCount();
		ILogicalOperator firstInput = getInputOperator(0);
		for (int i=1;i<count;i++){
			ILogicalOperator currentInput = getInputOperator(i);	
			validateSchemata(firstInput, currentInput);
		}
		return true;
	}

	protected void validateSchemata(ILogicalOperator firstInput, ILogicalOperator secondInput) {
		SDFAttributeList firstSchema = firstInput.getOutputSchema();
		SDFAttributeList secondSchema = secondInput.getOutputSchema();
		if (!firstSchema.compatibleTo(secondSchema)) {
			throw new IllegalArgumentException("incompatible schemas for union");
		}
	}
}
