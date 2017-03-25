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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ToKeyValue", doc = "Converts an input object a key-value/JSON object", category = { LogicalOperatorCategory.TRANSFORM })
public class ToKeyValueAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 3215936185841514847L;

	public ToKeyValueAO() {
	}

	public ToKeyValueAO(ToKeyValueAO tupleToKeyValue) {
		super(tupleToKeyValue);
	}

	@Override
	public ToKeyValueAO clone() {
		return new ToKeyValueAO(this);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(
				getInputSchema(0).getURI(),
				(Class<? extends IStreamObject<?>>) KeyValueObject.class, getInputSchema(0)
						.getAttributes(), getInputSchema());

		setOutputSchema(newOutputSchema);
		return newOutputSchema;
	}
}
