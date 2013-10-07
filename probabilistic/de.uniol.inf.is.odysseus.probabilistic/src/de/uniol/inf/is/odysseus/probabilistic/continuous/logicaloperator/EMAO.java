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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
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
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "EM")
public class EMAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4183569304131228484L;
	/** The attributes to classify. */
	private List<SDFAttribute> attributes;
	/** The number of Gaussian mixtures. */
	private int mixtures;
	/** The convergence threshold for fitting. */
	private double threshold = 1E-5;
	/** The maximum number of iterations allowed per fitting process. */
	private int iterations = 1000;

	private boolean incremental = false;

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
		this.iterations = emAO.iterations;
		this.threshold = emAO.threshold;
		this.incremental = emAO.incremental;
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
	 * Sets the maximum number of iterations allowed per fitting process.
	 * 
	 * @param iterations
	 *            the iterations to set
	 */
	@Parameter(type = IntegerParameter.class, name = "ITERATIONS", optional = true)
	public final void setIterations(final int iterations) {
		this.iterations = iterations;
	}

	/**
	 * Gets the maximum number of iterations allowed per fitting process.
	 * 
	 * @return the iterations
	 */
	@GetParameter(name = "ITERATIONS")
	public final int getIterations() {
		return this.iterations;
	}

	/**
	 * Sets the convergence threshold for fitting.
	 * 
	 * @param threshold
	 *            the threshold to set
	 */
	@Parameter(type = DoubleParameter.class, name = "THRESHOLD", optional = true)
	public final void setThreshold(final double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Gets the convergence threshold for fitting.
	 * 
	 * @return the threshold
	 */
	@GetParameter(name = "THRESHOLD")
	public final double getThreshold() {
		return this.threshold;
	}

	/**
	 * Sets the value of incremental fitting.
	 * 
	 * @param incremental
	 *            The value of incremental fitting.
	 */
	@Parameter(type = BooleanParameter.class, name = "INCREMENTAL", optional = true)
	public final void setIncremental(final boolean incremental) {
		this.incremental = incremental;
	}

	/**
	 * Gets the value of incremental fitting.
	 * 
	 * @return The value of incremental fitting.
	 */
	@GetParameter(name = "INCREMENTAL")
	public final boolean isIncremental() {
		return this.incremental;
	}

	/**
	 * Sets the model fitting predicate.
	 * 
	 * @param predicate
	 *            The predicate for model fitting.
	 */
	@Override
	@Parameter(type = PredicateParameter.class, name = "PREDICATE", optional = true)
	public final void setPredicate(@SuppressWarnings("rawtypes") final IPredicate predicate) {
		super.setPredicate(predicate);
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

		final SDFSchema outputSchema = new SDFSchema(this.getInputSchema().getURI(), this.getInputSchema().getType(), outputAttributes);
		this.setOutputSchema(outputSchema);
	}
}
