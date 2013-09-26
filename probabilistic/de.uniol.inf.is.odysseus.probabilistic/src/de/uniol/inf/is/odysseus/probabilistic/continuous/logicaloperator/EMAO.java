/*
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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Logical operator for Expectation Maximization (EM) classifier.
 * 
 * @see de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.EMPO for an implementation
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "EM", category={LogicalOperatorCategory.PROBABILISTIC})
public class EMAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4183569304131228484L;
	/** The attributes to classify. */
	private List<SDFAttribute> attributes;
	/** The number of Gaussian mixtures. */
	private int mixtures;

	/**
	 * Crates a new EM logical operator.
	 */
	public EMAO() {
		super();
	}

	/**
	 * Clone Constructor.
	 * 
	 * @param emAO
	 *            The copy
	 */
	public EMAO(final EMAO emAO) {
		super(emAO);
		this.attributes = new ArrayList<SDFAttribute>(emAO.attributes);
		this.mixtures = emAO.mixtures;
	}

	/**
	 * Sets the attributes to classify.
	 * 
	 * @param attributes
	 *            The list of attributes
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false)
	public final void setAttributes(final List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the attributes to classify.
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
	 * Sets the number of Gaussian mixtures.
	 * 
	 * @param mixtures
	 *            The number of Gaussian mixtures.
	 */
	@Parameter(type = IntegerParameter.class, name = "MIXTURES", optional = false)
	public final void setMixtures(final int mixtures) {
		this.mixtures = mixtures;
	}

	/**
	 * Gets the number of Gaussian mixtures.
	 * 
	 * @return The number of Gaussian mixtures.
	 */
	@GetParameter(name = "MIXTURES")
	public final int getMixtures() {
		return this.mixtures;
	}

	/**
	 * Gets the positions of the attributes.
	 * 
	 * @return The positions of the attributes
	 */
	public final int[] determineAttributesList() {
		return SchemaUtils.getAttributePos(this.getInputSchema(), this.getAttributes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public final AbstractLogicalOperator clone() {
		return new EMAO(this);
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

		final SDFSchema outputSchema = new SDFSchema(this.getInputSchema().getURI(),this.getInputSchema().getType(), outputAttributes);
		this.setOutputSchema(outputSchema);
	}
}
