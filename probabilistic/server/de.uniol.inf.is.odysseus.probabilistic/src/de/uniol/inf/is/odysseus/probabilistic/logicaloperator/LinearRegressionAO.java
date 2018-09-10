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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
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
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "LinearRegression", hidden = true, deprecation = true, doc = "This operator performs a linear regression on the given set of explanatory attributes to explain the given set of dependent attributes", category = { LogicalOperatorCategory.PROBABILISTIC })
public class LinearRegressionAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6621664432018792263L;
    /** The dependent attributes. */
    private List<SDFAttribute> dependentAttributes;
    /** The explanatory attributes. */
    private List<SDFAttribute> explanatoryAttributes;

    /**
     * Default constructor.
     */
    public LinearRegressionAO() {
        super();
    }

    /**
     * Clone constructor.
     * 
     * @param linearRegressionAO
     *            The object to copy from
     */
    public LinearRegressionAO(final LinearRegressionAO linearRegressionAO) {
        super(linearRegressionAO);
        this.dependentAttributes = new ArrayList<SDFAttribute>(linearRegressionAO.dependentAttributes);
        this.explanatoryAttributes = new ArrayList<SDFAttribute>(linearRegressionAO.explanatoryAttributes);

    }

    /**
     * Sets the value of the dependentAttributes property.
     * 
     * @param dependentAttributes
     *            The dependent attributes
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "DEPENDENT", isList = true, optional = false)
    public final void setDependentAttributes(final List<SDFAttribute> dependentAttributes) {
        Objects.requireNonNull(dependentAttributes);
        Preconditions.checkArgument(!dependentAttributes.isEmpty());
        this.dependentAttributes = dependentAttributes;
    }

    /**
     * Gets the value of the dependentAttributes property.
     * 
     * @return the dependent attributes
     */
    @GetParameter(name = "DEPENDENT")
    public final List<SDFAttribute> getDependentAttributes() {
        if (this.dependentAttributes == null) {
            this.dependentAttributes = new ArrayList<SDFAttribute>();
        }
        return this.dependentAttributes;
    }

    /**
     * Sets the value of the explanatoryAttributes property.
     * 
     * @param explanatoryAttributes
     *            The explanatory attributes
     */
    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "EXPLANATORY", isList = true, optional = false)
    public final void setExplanatoryAttributes(final List<SDFAttribute> explanatoryAttributes) {
        Objects.requireNonNull(explanatoryAttributes);
        Preconditions.checkArgument(!explanatoryAttributes.isEmpty());
        this.explanatoryAttributes = explanatoryAttributes;
    }

    /**
     * Gets the value of the explanatoryAttributes property.
     * 
     * @return the explanatory attributes
     */
    @GetParameter(name = "EXPLANATORY")
    public final List<SDFAttribute> getExplanatoryAttributes() {
        if (this.explanatoryAttributes == null) {
            this.explanatoryAttributes = new ArrayList<SDFAttribute>();
        }
        return this.explanatoryAttributes;
    }

    /**
     * 
     * @return The attribute positions of all dependent attributes
     */
    public final int[] determineDependentList() {
        return SchemaUtils.getAttributePos(this.getInputSchema(), this.getDependentAttributes());
    }

    /**
     * 
     * @return The attribute positions of all explanatory attributes
     */
    public final int[] determineExplanatoryList() {
        return SchemaUtils.getAttributePos(this.getInputSchema(), this.getExplanatoryAttributes());
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #clone()
     */
    @Override
    public final AbstractLogicalOperator clone() {
        return new LinearRegressionAO(this);
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
     * #initialize()
     */
    @Override
    public final void initialize() {
        super.initialize();
        Objects.requireNonNull(this.explanatoryAttributes);
        Objects.requireNonNull(this.dependentAttributes);
        Preconditions.checkArgument(!this.explanatoryAttributes.isEmpty());
        Preconditions.checkArgument(!this.dependentAttributes.isEmpty());

        final Collection<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute inAttr : this.getInputSchema().getAttributes()) {
            if (this.getExplanatoryAttributes().contains(inAttr)) {
                attributes.add(new SDFAttribute(inAttr.getSourceName(), inAttr.getAttributeName(), SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
            }
            else {
                attributes.add(inAttr);
            }
        }
        attributes.add(new SDFAttribute("", "$coefficients", SDFDatatype.MATRIX_DOUBLE, null, null, null));
        attributes.add(new SDFAttribute("", "$residual", SDFDatatype.MATRIX_DOUBLE, null, null, null));
        final SDFSchema outputSchema =  SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema());
        this.setOutputSchema(outputSchema);
    }
}
