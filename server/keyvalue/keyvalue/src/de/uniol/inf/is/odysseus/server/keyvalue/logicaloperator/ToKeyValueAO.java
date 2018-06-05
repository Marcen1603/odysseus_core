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
package de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ToKeyValue", doc = "Converts an input object a key-value/JSON object", category = {
		LogicalOperatorCategory.TRANSFORM })
public class ToKeyValueAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 3215936185841514847L;

	private String template;

	public ToKeyValueAO() {
		this.template = "";
	}

	public ToKeyValueAO(ToKeyValueAO tupleToKeyValue) {
		super(tupleToKeyValue);
		this.template = tupleToKeyValue.getTemplate();
	}

	public String getTemplate() {
		return template;
	}

	@Parameter(type = StringParameter.class, optional = true, doc = "Template for the JSON object. Variables have to be in <brackets> and their names have to match the tuples attribute names.")
	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public ToKeyValueAO clone() {
		return new ToKeyValueAO(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// We need to remove the attribute list. KeyValueObjects do not have an
		// attribute list.
		Collection<SDFAttribute> emptyAttributes = new ArrayList<>();
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(getInputSchema(pos).getURI(),
				(Class<? extends IStreamObject<?>>) KeyValueObject.class, emptyAttributes, getInputSchema());

		setOutputSchema(newOutputSchema);
		return newOutputSchema;
	}
}
