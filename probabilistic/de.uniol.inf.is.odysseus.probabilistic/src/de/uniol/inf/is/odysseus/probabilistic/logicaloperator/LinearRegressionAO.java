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
package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.List;

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
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "LinearRegression")
public class LinearRegressionAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6621664432018792263L;
	private List<SDFAttribute> dependentAttributes;
	private List<SDFAttribute> explanatoryAttributes;

	public LinearRegressionAO() {
		super();
	}

	public LinearRegressionAO(LinearRegressionAO linearRegressionAO) {
		super(linearRegressionAO);
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DEPENDENT", isList = true, optional = false)
	public void setDependentAttributes(
			final List<SDFAttribute> dependentAttributes) {
		this.dependentAttributes = dependentAttributes;
	}

	@GetParameter(name = "DEPENDENT")
	public List<SDFAttribute> getDependentAttributes() {
		return this.dependentAttributes;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "EXPLANATORY", isList = true, optional = false)
	public void setExplanatoryAttributes(
			final List<SDFAttribute> explanatoryAttributes) {
		this.explanatoryAttributes = explanatoryAttributes;
	}

	@GetParameter(name = "EXPLANATORY")
	public List<SDFAttribute> getExplanatoryAttributes() {
		return this.explanatoryAttributes;
	}

	public int[] determineDependentList() {
		return calcAttributeList(getInputSchema(), dependentAttributes);
	}

	public int[] determineExplanatoryList() {
		return calcAttributeList(getInputSchema(), explanatoryAttributes);
	}

	public static int[] calcAttributeList(SDFSchema in,
			List<SDFAttribute> attributes) {
		int[] ret = new int[attributes.size()];
		int i = 0;
		for (SDFAttribute attr : attributes) {
			if (!in.contains(attr)) {
				throw new IllegalArgumentException("no such attribute: " + attr);
			} else {
				ret[i] = in.indexOf(attr);
				i++;
			}
		}
		return ret;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LinearRegressionAO(this);
	}

	@Override
	public void initialize() {
		SDFSchema inputSchema = this.getInputSchema();
		this.setOutputSchema(SDFSchema.union(inputSchema, new SDFSchema(
				getInputSchema().getURI(), new SDFAttribute("", "residual",
						SDFDatatype.MATRIX_DOUBLE), new SDFAttribute("",
						"regressionCoefficients", SDFDatatype.MATRIX_DOUBLE))));
	}
}
