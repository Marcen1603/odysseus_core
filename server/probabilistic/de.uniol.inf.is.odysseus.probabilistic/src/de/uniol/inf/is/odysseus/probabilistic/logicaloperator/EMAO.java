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

package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Logical operator for Expectation Maximization (EM) classifier.
 * 
 * @see de.uniol.inf.is.odysseus.probabilistic.physicaloperator.EMPO
 *      for an implementation
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, deprecation = true, name = "EM", category = { LogicalOperatorCategory.PROBABILISTIC }, doc = "Estimate the distribution of the given attributes using a Gaussian mixture model")
public class EMAO extends UnaryLogicalOp implements IHasPredicate {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4183569304131228484L;
    /** The attributes to classify. */
    private List<SDFAttribute> attributes;
    /** The number of Gaussian mixtures. */
    private int mixtures;
    /** The convergence threshold for fitting. */
    private double threshold = 1E-3;
    /** The maximum number of iterations allowed per fitting process. */
    private int iterations = 30;
    /** Incremental fitting. */
    private boolean incremental = false;
    
	private IPredicate<?> predicate;


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
        if (emAO.predicate != null){
        	this.predicate = emAO.predicate.clone();
        }
    }

    /**
     * Sets the attributes to classify.
     * 
     * @param attributes
     *            The list of attributes
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = false, doc = "The attributes to fit a distribution to")
    public final void setAttributes(final List<SDFAttribute> attributes) {
        Objects.requireNonNull(attributes);
        Preconditions.checkArgument(!attributes.isEmpty());
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
    @Parameter(type = IntegerParameter.class, name = "MIXTURES", optional = false, doc = "The number of mixture components.")
    public final void setMixtures(final int mixtures) {
        Preconditions.checkArgument(mixtures >= 2);
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
    @Parameter(type = IntegerParameter.class, name = "ITERATIONS", optional = true, doc = "The number of iterations (default: 1000).")
    public final void setIterations(final int iterations) {
        Preconditions.checkArgument(iterations > 0);
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
    @Parameter(type = DoubleParameter.class, name = "THRESHOLD", optional = true, doc = "The threshold for the loglikelyhood to terminate the fitting process (default: 10E-5).")
    public final void setThreshold(final double threshold) {
        Preconditions.checkArgument(threshold > 0);
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
    @Parameter(type = BooleanParameter.class, name = "INCREMENTAL", optional = true, doc = "Reuse the existing model in each fitting process.")
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
    @Parameter(type = PredicateParameter.class, name = "PREDICATE", optional = true, doc = "The predicate to run a new fitting process.")
    public final void setPredicate(@SuppressWarnings("rawtypes") final IPredicate predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }
    
    @Override
    public IPredicate<?> getPredicate() {
		return predicate;
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
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public final AbstractLogicalOperator clone() {
        return new EMAO(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #initialize()
     */
    @Override
    public final void initialize() {
        super.initialize();
        Objects.requireNonNull(this.attributes);
        Preconditions.checkArgument(!this.attributes.isEmpty());
        Preconditions.checkArgument(this.mixtures >= 2);
        Preconditions.checkArgument(this.threshold >= 0);
        Preconditions.checkArgument(this.iterations > 0);
        final Collection<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
            if (this.getAttributes().contains(inAttr)) {
                outputAttributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
            }
            else {
                outputAttributes.add(inAttr);
            }
        }

        final SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
        this.setOutputSchema(outputSchema);
    }
}
