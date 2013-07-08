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
package de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
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
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

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

	public LinearRegressionAO(final LinearRegressionAO linearRegressionAO) {
		super(linearRegressionAO);
		this.dependentAttributes = new ArrayList<SDFAttribute>(linearRegressionAO.dependentAttributes);
		this.explanatoryAttributes = new ArrayList<SDFAttribute>(linearRegressionAO.explanatoryAttributes);

	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DEPENDENT", isList = true, optional = false)
	public final void setDependentAttributes(final List<SDFAttribute> dependentAttributes) {
		this.dependentAttributes = dependentAttributes;
	}

	@GetParameter(name = "DEPENDENT")
	public final List<SDFAttribute> getDependentAttributes() {
		if (this.dependentAttributes == null) {
			this.dependentAttributes = new ArrayList<SDFAttribute>();
		}
		return this.dependentAttributes;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "EXPLANATORY", isList = true, optional = false)
	public final void setExplanatoryAttributes(final List<SDFAttribute> explanatoryAttributes) {
		this.explanatoryAttributes = explanatoryAttributes;
	}

	@GetParameter(name = "EXPLANATORY")
	public final List<SDFAttribute> getExplanatoryAttributes() {
		if (this.explanatoryAttributes == null) {
			this.explanatoryAttributes = new ArrayList<SDFAttribute>();
		}
		return this.explanatoryAttributes;
	}

	public final int[] determineDependentList() {
		return SchemaUtils.getAttributePos(this.getInputSchema(), this.getDependentAttributes());
	}

	public final int[] determineExplanatoryList() {
		return SchemaUtils.getAttributePos(this.getInputSchema(), this.getExplanatoryAttributes());
	}

	@Override
	public final AbstractLogicalOperator clone() {
		return new LinearRegressionAO(this);
	}

	@Override
	public final void initialize() {
		final Collection<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
			if (this.getExplanatoryAttributes().contains(inAttr)) {
				attributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
			} else {
				attributes.add(inAttr);
			}
		}
		attributes.add(new SDFAttribute("", "$coefficients", SDFDatatype.MATRIX_DOUBLE));

		final SDFSchema outputSchema = new SDFSchema(this.getInputSchema().getURI(), attributes);
		this.setOutputSchema(outputSchema);
	}
}
