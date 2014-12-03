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
package de.uniol.inf.is.odysseus.ontology.logicaloperator;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ontology.Activator;
import de.uniol.inf.is.odysseus.ontology.common.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.common.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.common.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.common.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.utils.SDFUtils;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, deprecation = true, doc = "Append quality information to the incoming stream object.", name = "Quality", category = { LogicalOperatorCategory.ONTOLOGY })
public class QualityAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7153504084002972374L;
    private List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
    private List<String> properties = new ArrayList<String>();
    private SDFExpression[] expressions;

    /**
     * Class constructor.
     * 
     */
    public QualityAO() {
        super();
    }

    /**
     * Clone constructor.
     * 
     * @param clone
     *            The instance to clone from
     */
    public QualityAO(final QualityAO clone) {
        super(clone);
        Objects.requireNonNull(clone);
        this.attributes = new ArrayList<SDFAttribute>(clone.attributes);
        this.properties = new ArrayList<String>(clone.properties);
        if (clone.expressions != null) {
            this.expressions = new SDFExpression[clone.expressions.length];
            for (int i = 0; i < this.expressions.length; i++) {
                this.expressions[i] = clone.expressions[i].clone();
            }
        }
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = false, isList = true)
    public void setAttributes(final List<SDFAttribute> attributes) {
        Objects.requireNonNull(attributes);
        Preconditions.checkArgument(!attributes.isEmpty());
        this.attributes = attributes;
    }

    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList<SDFAttribute>();
        }
        return this.attributes;
    }

    @Parameter(type = StringParameter.class, name = "PROPERTIES", optional = false, isList = true)
    public void setProperties(final List<String> properties) {
        Objects.requireNonNull(properties);
        Preconditions.checkArgument(!properties.isEmpty());
        this.properties = properties;
    }

    @GetParameter(name = "PROPERTIES")
    public List<String> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList<String>();
        }
        return this.properties;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public SDFSchema getOutputSchemaIntern(final int pos) {
        this.calcOutputSchema();
        return this.getOutputSchema();
    }

    public SDFExpression[] getExpressions() {
        return this.expressions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new QualityAO(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        Objects.requireNonNull(this.attributes);
        Objects.requireNonNull(this.properties);
        Preconditions.checkArgument(!this.attributes.isEmpty());
        Preconditions.checkArgument(!this.properties.isEmpty());
        Preconditions.checkState(Activator.getSensorOntologyService() != null);
    }

    private void calcOutputSchema() {
        final List<SDFAttribute> outputSchemaAttributes = new ArrayList<SDFAttribute>();

        for (final SDFAttribute attribute : this.getInputSchema()) {
            outputSchemaAttributes.add(attribute.clone());
        }
        for (final SDFAttribute attribute : this.getAttributes()) {
            for (final String property : this.properties) {
                final SDFAttribute propertyAttribute = new SDFAttribute(attribute.getURIWithoutQualName(), attribute.getAttributeName() + "_" + property, SDFDatatype.DOUBLE, attribute.getUnit(),
                        attribute.getDtConstraints());
                outputSchemaAttributes.add(propertyAttribute);
            }
        }
        
        this.setOutputSchema(new SDFSchema(this.getInputSchema(), outputSchemaAttributes));
        this.expressions = this.estimateExpressions();
    }

    /**
     * Constructs the expressions for the measurement properties.
     * 
     * The expression for each property is of the form:
     * 
     * sMin(eif((Condition1_1) AND (Condition1_2), sMax([Property1_1Expr1,
     * Property1_1Expr2]), DOUBLE.MAX), eif((Condition2_1) AND
     * (Condition2_2), sMax([Property2_1Expr1, Property2_2Expr2]),DOUBLE.MAX))
     * 
     * 
     * With ConditionX_Y being different conditions for one measurement
     * capability. And PropertyX_YExprZ being an expression for one property
     * under the
     * given condition
     * 
     * @return The expressions used during processing
     */
    private SDFExpression[] estimateExpressions() {
        final List<SDFAttribute> attributes = this.getAttributes();
        final List<String> measurementPropertyNames = this.getProperties();

        final SDFExpression[] expressions = new SDFExpression[(attributes.size() * measurementPropertyNames.size()) + this.getInputSchema().size()];

        int i = 0;
        for (final SDFAttribute attribute : this.getInputSchema()) {
            expressions[i] = new SDFExpression(attribute.getAttributeName(), MEP.getInstance());
            i++;
        }

        for (final SDFAttribute attribute : attributes) {
            final List<SensingDevice> sensingDevices = this.getSensingDevices(attribute);
            if (!sensingDevices.isEmpty()) {
                // FIXME What happens if there are more than one sensing device?
                final SensingDevice sensingDevice = sensingDevices.get(0);

                final List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(attribute.getAttributeName());
                final Map<String, Map<StringBuilder, StringBuilder>> attributeMeasurementPropertyExpression = new HashMap<String, Map<StringBuilder, StringBuilder>>();

                for (final MeasurementCapability measurementCapability : measurementCapabilities) {

                    final Map<String, StringBuilder> measurementPropertyExpression = this.getMeasurementPropertyExpressions(measurementCapability);
                    final StringBuilder conditionExpression = this.getConditionExpression(measurementCapability);

                    for (final String measurementPropertyName : measurementPropertyExpression.keySet()) {
                        if (!attributeMeasurementPropertyExpression.containsKey(measurementPropertyName)) {
                            attributeMeasurementPropertyExpression.put(measurementPropertyName, new HashMap<StringBuilder, StringBuilder>());
                        }
                        final Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression.get(measurementPropertyName);

                        conditionMeasurementPropertyMapping.put(conditionExpression, measurementPropertyExpression.get(measurementPropertyName));
                    }

                }

                for (final String measurementPropertyName : measurementPropertyNames) {
                    final Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression.get(measurementPropertyName);
                    final StringBuilder expression = new StringBuilder();

                    for (final StringBuilder conditionExpression : conditionMeasurementPropertyMapping.keySet()) {
                        if (expression.length() != 0) {
                            expression.append(", ");
                        }
                        expression.append("eif(");
                        expression.append(conditionExpression);
                        expression.append(",");
                        expression.append("sMax(");
                        expression.append(conditionMeasurementPropertyMapping.get(conditionExpression));
                        expression.append(")");
                        expression.append(",");
                        // expression.append(new
                        // BigDecimal(Double.MAX_VALUE).toPlainString());
                        expression.append("0.0");
                        expression.append(")");
                    }
                    expression.insert(0, "sMax([");
                    expression.append("])");
                    expressions[i] = new SDFExpression(expression.toString(), MEP.getInstance());
                    i++;
                }
            }
            else {
                expressions[i] = new SDFExpression("-1.0", MEP.getInstance());
            }
        }
        return expressions;
    }

    /**
     * Get all sensing devices providing the given attribute
     * 
     * @param attribute
     *            The attribute
     * @return The list of sensing devices
     */
    private List<SensingDevice> getSensingDevices(final SDFAttribute attribute) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(Activator.getSensorOntologyService());
        return Activator.getSensorOntologyService().getSensingDevices(SDFUtils.getFeatureOfInterestLabel(attribute), SDFUtils.getSensingDeviceLabel(attribute), attribute.getAttributeName());
    }

    /**
     * Constructs the condition expression for the given measurement capability
     * and inserts the attribute name observed condition property. The condition
     * looks like:
     * 
     * (con1) AND (con2) AND ...
     * 
     * @param measurementCapability
     *            The measurement capability
     * @return A {@link StringBuilder} object containing the condition predicate
     */
    private StringBuilder getConditionExpression(final MeasurementCapability measurementCapability) {
        final List<Condition> conditions = measurementCapability.getInConditions();

        final StringBuilder conditionExpression = new StringBuilder();

        for (final Condition condition : conditions) {
            if (conditionExpression.length() != 0) {
                // Concatenate each condition with an AND
                conditionExpression.append(" AND ");
            }

            final List<String> observerAttributeUris = Activator.getSensorOntologyService().getAttributes(condition);

            final List<SDFAttribute> observerAttributes = new ArrayList<>();
            for (final String uri : observerAttributeUris) {
                final String[] split = SDFElement.splitURI(uri);
                observerAttributes.add(new SDFAttribute(split[0], split[1], SDFDatatype.DOUBLE, null));
            }
            conditionExpression.append("(");
            // Inject the observed attribute name for evaluation during
            // processing
            conditionExpression.append(String.format(condition.toString(), observerAttributes.get(0).getAttributeName()));
            conditionExpression.append(")");
        }

        return conditionExpression;
    }

    /**
     * Construct the vectors for the measurement properties and insert the
     * attribute name of the measurement capability.
     * 
     * The vector consists of single expressions that are evaluated during
     * processing. The vector looks like:
     * 
     * [expr1, expr2, ...]
     * 
     * @param measurementCapability
     *            The measurement capability
     * @return A map including the name of the property and a vector of
     *         expressions to evaluate
     */
    private Map<String, StringBuilder> getMeasurementPropertyExpressions(final MeasurementCapability measurementCapability) {
        final Map<String, StringBuilder> measurementPropertyExpressions = new HashMap<String, StringBuilder>();
        final List<MeasurementProperty> measurementProperties = measurementCapability.getHasMeasurementProperties();

        for (final MeasurementProperty measurementProperty : measurementProperties) {
            if (measurementProperty != null) {
                StringBuilder expressionBuilder;
                if (!measurementPropertyExpressions.containsKey(URI.create(measurementProperty.getResource()).getFragment())) {
                    expressionBuilder = new StringBuilder();
                    measurementPropertyExpressions.put(URI.create(measurementProperty.getResource()).getFragment(), expressionBuilder);
                }
                else {
                    expressionBuilder = measurementPropertyExpressions.get(URI.create(measurementProperty.getResource()).getFragment());
                }
                if (expressionBuilder.length() != 0) {
                    expressionBuilder.append(", ");
                }
                if (measurementProperty.getExpression() != null) {
                    // Inject the observed attribute name for evaluation during
                    // processing
                    expressionBuilder.append(String.format(measurementProperty.getExpression(), measurementCapability.getName()));
                }
                else {
                    expressionBuilder.append(0.0);
                }
            }
        }
        for (final String measurementPropertyName : measurementPropertyExpressions.keySet()) {
            final StringBuilder expressionBuilder = measurementPropertyExpressions.get(measurementPropertyName);
            expressionBuilder.insert(0, "[");
            expressionBuilder.append("]");
        }
        return measurementPropertyExpressions;
    }
}
