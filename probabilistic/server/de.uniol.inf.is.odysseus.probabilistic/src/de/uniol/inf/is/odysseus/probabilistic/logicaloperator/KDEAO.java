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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Logical operator for Kernel Density Estimator (KDE) classifier.
 * 
 * @see de.uniol.inf.is.odysseus.probabilistic.physicaloperator.KDEPO
 *      for an implementation
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "KDE", deprecation = true, category = { LogicalOperatorCategory.PROBABILISTIC }, doc = "Estimate the distribution of the given attributes using a Gaussian mixture model")
public class KDEAO extends UnaryLogicalOp {

    /**
     * 
     */
    private static final long serialVersionUID = 1541672562209669473L;
    /** The attributes to classify. */
    private List<SDFAttribute> attributes;

    /**
     * Crates a new KDE logical operator.
     */
    public KDEAO() {
        super();
    }

    /**
     * Clone Constructor.
     * 
     * @param emAO
     *            The copy
     */
    public KDEAO(final KDEAO kdeAO) {
        super(kdeAO);
        this.attributes = new ArrayList<SDFAttribute>(kdeAO.attributes);
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
        return new KDEAO(this);
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
