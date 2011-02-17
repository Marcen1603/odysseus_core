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

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RenameAOBuilder extends AbstractOperatorBuilder {

	private static final String ALIASES = "ALIASES";
	private ListParameter<String> aliases = new ListParameter<String>(ALIASES,
			REQUIREMENT.MANDATORY, new DirectParameter<String>("rename alias",
					REQUIREMENT.MANDATORY));

	public RenameAOBuilder() {
		super(1, 1);
		setParameters(aliases);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		List<String> names = aliases.getValue();
		ILogicalOperator inputOp = getInputOperator(0);
		SDFAttributeList inputSchema = inputOp.getOutputSchema();
		SDFAttributeList outputSchema = new SDFAttributeList();
		Iterator<SDFAttribute> it = inputSchema.iterator();
		for (String str : names) {
			// use clone, so we have a datatype etc.
			SDFAttribute attribute = it.next().clone();
			attribute.setAttributeName(str);
			attribute.setSourceName(null);
			outputSchema.add(attribute);
		}

		RenameAO renameAO = new RenameAO();
		renameAO.setOutputSchema(outputSchema);

		return renameAO;
	}

	@Override
	protected boolean internalValidation() {
		List<String> names = aliases.getValue();
		ILogicalOperator inputOp = getInputOperator(0);
		SDFAttributeList inputSchema = inputOp.getOutputSchema();
		if (inputSchema.size() != names.size()) {
			throw new IllegalArgumentException(
					"number of aliases does not match number of input attributes for rename");
		}
		return true;
	}

}
