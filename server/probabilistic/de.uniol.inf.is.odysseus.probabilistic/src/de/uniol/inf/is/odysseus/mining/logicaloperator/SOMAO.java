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
package de.uniol.inf.is.odysseus.mining.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
// @LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc =
// "A self organizing map", name = "SOMP", category = {
// LogicalOperatorCategory.PROBABILISTIC })
public class SOMAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4933957337698399607L;
    /** The attribute used as an input vector. */
    private SDFAttribute attribute;
    /** The number of iterations. */
    private long iterations;
    /** The learning rate. */
    private double learningRate;

    /**
     * Constructs a new SOM logical operator.
     */
    public SOMAO() {
        super();
    }

    /**
     * Clone constructor.
     * 
     * @param somAO
     *            The copy
     */
    public SOMAO(final SOMAO somAO) {
        super(somAO);
        this.attribute = somAO.attribute;
    }

    /**
     * Sets the attribute.
     * 
     * @param attribute
     *            The attribute
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTE", isList = false, optional = true)
    public final void setAttribute(final SDFAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Gets the attribute.
     * 
     * @return The attribute
     */
    public final SDFAttribute getAttribute() {
        return this.attribute;
    }

    /**
     * @return the iterations
     */
    public long getIterations() {
        return this.iterations;
    }

    /**
     * @param iterations
     *            the iterations to set
     */
    @Parameter(type = LongParameter.class, name = "ITERATIONS", isList = false, optional = true)
    public void setIterations(final long iterations) {
        this.iterations = iterations;
    }

    /**
     * @return the learningRate
     */
    public double getLearningRate() {
        return this.learningRate;
    }

    /**
     * @param learningRate
     *            the learningRate to set
     */
    @Parameter(type = DoubleParameter.class, name = "LEARNINGRATE", isList = false, optional = true)
    public void setLearningRate(final double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final AbstractLogicalOperator clone() {
        return new SOMAO(this);
    }

}
