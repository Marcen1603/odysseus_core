/**
 * Copyright 2013 The Odysseus Team
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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
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
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DISTRIBUTION", category = { "Probabilistic" }, doc = "Assign a distribution to the given attributes")
public class AssignDistributionAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5805291792113531438L;
	/** The variance. */
	private SDFAttribute variance;
	/** The attributes to build the distribution from. */
	private List<SDFAttribute> attributes;

	/**
	 * Creates a new Sample logical operator.
	 */
	public AssignDistributionAO() {

	}

	/**
	 * Clone Constructor.
	 * 
	 * @param distributionAO
	 *            The copy
	 */
	public AssignDistributionAO(final AssignDistributionAO distributionAO) {
		super(distributionAO);
		this.attributes = new ArrayList<SDFAttribute>(distributionAO.attributes);
		this.variance = distributionAO.variance;
	}

	/**
	 * Sets the attributes for the distribution.
	 * 
	 * @param attributes
	 *            The list of attributes
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false, doc = "The attributes holding the expected value.")
	public final void setAttributes(final List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the attributes for the distribution.
	 * 
	 * @return The list of attributes
	 */
	@GetParameter(name = "ATTRIBUTES")
	public final List<SDFAttribute> getAttributes() {
		if (this.attributes == null) {
			this.attributes = new ArrayList<SDFAttribute>();
		}
		return this.attributes;
	}

	/**
	 * Gets the variance.
	 * 
	 * @return The variance
	 */
	@GetParameter(name = "VARIANCE")
	public final SDFAttribute getVariance() {
		return this.variance;
	}

	/**
	 * Sets the variance.
	 * 
	 * @param variance
	 *            The variance
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "VARIANCE", isList = false, optional = false, doc = "The attribute holding the variance of the distribution.")
	public final void setVariance(final SDFAttribute variance) {
		this.variance = variance;
	}

	/**
	 * Gets the positions of the attributes.
	 * 
	 * @return The positions of the attributes
	 */
	public final int[] determineAttributesList() {
		return SchemaUtils.getAttributePos(this.getInputSchema(), this.getAttributes());
	}

	/**
	 * Gets the position of the variance matrix.
	 * 
	 * @return The position of the variance attribute
	 */
	public final int determineVariance() {
		return SchemaUtils.getAttributePos(getInputSchema(), getVariance());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public final AssignDistributionAO clone() {
		return new AssignDistributionAO(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#initialize()
	 */
	@Override
	public final void initialize() {
		final Collection<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
			if (this.getAttributes().contains(inAttr)) {
				outputAttributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE));
			} else {
				outputAttributes.add(inAttr);
			}
		}

		final SDFSchema outputSchema = new SDFSchema(this.getInputSchema().getURI(), this.getInputSchema().getType(), outputAttributes);
		this.setOutputSchema(outputSchema);
	}

}
