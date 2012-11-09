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
package de.uniol.inf.is.odysseus.mining.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Dennis Geesen
 *
 */
@LogicalOperator(name = "CLASSIFY", minInputPorts = 2, maxInputPorts = 2)
public class ClassificationAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1231999597473176237L;

	public ClassificationAO() {

	}

	public ClassificationAO(ClassificationAO classificationAO) {
		
	}

	protected SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<>();
		for(SDFAttribute oldAttribute : this.getInputSchema(0).getAttributes()){
			attributes.add(new SDFAttribute(null, oldAttribute.getAttributeName(), oldAttribute.getDatatype()));
		}
		
		SDFAttribute attributeId = new SDFAttribute(null, "clazz", SDFDatatype.STRING);
		attributes.add(attributeId);
		
		SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
		return outSchema;

	}

	@Override
	public ClassificationAO clone() {
		return new ClassificationAO(this);
	}
}
