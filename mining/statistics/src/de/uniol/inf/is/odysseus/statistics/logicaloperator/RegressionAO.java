/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.statistics.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * 
 * @author Dennis Geesen Created at: 29.03.2012
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "REGRESSION")
public class RegressionAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1953522305718887385L;

	private SDFAttribute attributeX;
	private SDFAttribute attributeY;

	private static final String URI_NAME = "Regression";
	private static final String SOURCE_NAME = null;

	public RegressionAO() {
		super();
	}

	public RegressionAO(RegressionAO ao) {
		super(ao);
		this.attributeX = ao.attributeX;
		this.attributeY = ao.attributeY;
	}

	@GetParameter(name = "X")
	public SDFAttribute getAttributeX() {
		return attributeX;
	}

	@Parameter(name = "X", type = ResolvedSDFAttributeParameter.class)
	public void setAttributeX(SDFAttribute attributeX) {
		this.attributeX = attributeX;
	}

	@GetParameter(name = "Y")
	public SDFAttribute getAttributeY() {
		return attributeY;
	}

	@Parameter(name = "Y", type = ResolvedSDFAttributeParameter.class)
	public void setAttributeY(SDFAttribute attributeY) {
		this.attributeY = attributeY;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		SDFAttribute x = new SDFAttribute(SOURCE_NAME, "slope",
				SDFDatatype.DOUBLE);
		SDFAttribute y = new SDFAttribute(SOURCE_NAME, "intercept",
				SDFDatatype.DOUBLE);
		setOutputSchema(new SDFSchema(URI_NAME, x, y));
		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RegressionAO(this);
	}

}
