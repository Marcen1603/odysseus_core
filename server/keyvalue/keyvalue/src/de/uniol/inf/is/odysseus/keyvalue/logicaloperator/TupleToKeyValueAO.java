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
package de.uniol.inf.is.odysseus.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "TupleToKeyValue", doc = "Converts a tuple to a key-value/JSON object", category = { LogicalOperatorCategory.TRANSFORM })
public class TupleToKeyValueAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 3215936185841514847L;

	@SuppressWarnings("rawtypes")
	private Class<? extends IStreamObject> type = KeyValueObject.class;

	public TupleToKeyValueAO() {
	}

	public TupleToKeyValueAO(TupleToKeyValueAO tupleToKeyValue) {
		super(tupleToKeyValue);
	}

	@Override
	public TupleToKeyValueAO clone() {
		return new TupleToKeyValueAO(this);
	}

	@Parameter(type = StringParameter.class, name = "TYPE", optional = true, isList = false, doc = "type of key value object the tuples will be transformed to")
	public void setType(String type) {
		if (type.equalsIgnoreCase("KEYVALUEOBJECT")) {
			this.type = KeyValueObject.class;
		} else if (type.equalsIgnoreCase("NESTEDKEYVALUEOBJECT")) {
			this.type = NestedKeyValueObject.class;
		} else {
			this.type = null;
		}
	}

	// TODO: Shouldn't this method be renamed to getType?
	@GetParameter(name = "TYPE")
	public String getType() {
		return this.type.getSimpleName();
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends IStreamObject> getTypeClass() {
		return this.type;
	}	
	
	@Override
	public boolean isValid() {
		if (this.type != null
				&& KeyValueObject.class.isAssignableFrom(this.type)) {
			return true;
		}
		addError("TupleToKeyValue operator has a wrong type.");
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(
				getInputSchema(0).getURI(),
				(Class<? extends IStreamObject<?>>) type, getInputSchema(0)
						.getAttributes(), getInputSchema());

		setOutputSchema(newOutputSchema);
		return newOutputSchema;
	}
}
