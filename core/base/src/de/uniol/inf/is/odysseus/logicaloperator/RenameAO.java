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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(name = "RENAME", minInputPorts = 1, maxInputPorts = 1)
public class RenameAO extends UnaryLogicalOp implements OutputSchemaSettable {

	private static final long serialVersionUID = 4218605858465342011L;
	protected SDFAttributeList outputSchema;

	public RenameAO() {
		super();
	}

	public RenameAO(AbstractLogicalOperator po) {
		super(po);
		outputSchema = po.getOutputSchema();
	}

	public RenameAO(RenameAO ao) {
		super(ao);
		outputSchema = new SDFAttributeList(ao.outputSchema);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}

	@Parameter(type = StringParameter.class, isList = true)
	public void setAliases(List<String> aliases) {
		SDFAttributeList inputSchema = getInputSchema();
		if (inputSchema.size() != aliases.size()) {
			throw new IllegalArgumentException(
					"number of aliases does not match number of input attributes for rename");
		}
		this.outputSchema = new SDFAttributeList();
		Iterator<SDFAttribute> it = inputSchema.iterator();
		for (String str : aliases) {
			// use clone, so we have a datatype etc.
			SDFAttribute attribute = it.next().clone();
			attribute.setAttributeName(str);
			attribute.setSourceName(null);
			this.outputSchema.add(attribute);
		}
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RenameAO(this);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if (port == 0) {
			setOutputSchema(outputSchema);
		} else {
			throw new IllegalArgumentException("no such port: " + port);
		}
	}

}
