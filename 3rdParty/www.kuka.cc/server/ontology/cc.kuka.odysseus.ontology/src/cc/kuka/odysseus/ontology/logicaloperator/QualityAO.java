/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.logicaloperator;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cc.kuka.odysseus.ontology.Activator;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.utils.SDFUtils;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc = "Append quality information to the incoming stream object.", name = "Quality", url = "https://kuka.cc/software/odysseus/ontology", category = { LogicalOperatorCategory.ONTOLOGY })
public class QualityAO extends UnaryLogicalOp {

    /**
     *
     */
    private static final long serialVersionUID = 7153504084002972374L;
    private List<SDFAttribute> attributes = new ArrayList<>();
    private List<String> properties = new ArrayList<>();
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
        this.attributes = new ArrayList<>(clone.attributes);
        this.properties = new ArrayList<>(clone.properties);
        if (clone.expressions != null) {
            this.expressions = new SDFExpression[clone.expressions.length];
            for (int i = 0; i < this.expressions.length; i++) {
                this.expressions[i] = clone.expressions[i].clone();
            }
        }
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = false, isList = true, doc = "The list of attributes")
    public void setAttributes(final List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        return this.attributes;
    }

    @Parameter(type = StringParameter.class, name = "PROPERTIES", optional = false, isList = true, doc = "The list of measurement property names")
    public void setProperties(final List<String> properties) {
        this.properties = properties;
    }

    @GetParameter(name = "PROPERTIES")
    public List<String> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
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
    }

    @Override
    public boolean isValid() {
        this.addError(Activator.getSensorOntologyService() == null, "Sensor ontology service not ready");
        this.addError(this.attributes.isEmpty(), "No attributes selected");
        this.addError(this.properties.isEmpty(), "No properties selected");
        return !this.hasErrors();
    }

    private void calcOutputSchema() {
        final List<SDFAttribute> outputSchemaAttributes = new ArrayList<>();

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

        this.setOutputSchema(SDFSchemaFactory.createNewSchema(this.getInputSchema().getURI(), this.getInputSchema().getType(), outputSchemaAttributes));
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
        final List<SDFAttribute> attrs = this.getAttributes();
        final List<String> measurementPropertyNames = this.getProperties();

        final SDFExpression[] exprs = new SDFExpression[(attrs.size() * measurementPropertyNames.size()) + this.getInputSchema().size()];

        int i = 0;
        for (final SDFAttribute attribute : this.getInputSchema()) {
            exprs[i] = new SDFExpression(attribute.getAttributeName(), MEP.getInstance());
            i++;
        }

        for (final SDFAttribute attribute : attrs) {
            final List<SensingDevice> sensingDevices = QualityAO.getSensingDevices(attribute);
            if (!sensingDevices.isEmpty()) {
                // FIXME What happens if there are more than one sensing device?
                final SensingDevice sensingDevice = sensingDevices.get(0);

                final List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(attribute.getAttributeName());
                final Map<String, Map<StringBuilder, StringBuilder>> attributeMeasurementPropertyExpression = new HashMap<>();

                for (final MeasurementCapability measurementCapability : measurementCapabilities) {

                    final Map<String, StringBuilder> measurementPropertyExpression = QualityAO.getMeasurementPropertyExpressions(measurementCapability);
                    final StringBuilder conditionExpression = QualityAO.getConditionExpression(measurementCapability);

                    if (conditionExpression.length() > 0) {
                        for (final String measurementPropertyName : measurementPropertyExpression.keySet()) {
                            if (!attributeMeasurementPropertyExpression.containsKey(measurementPropertyName)) {
                                attributeMeasurementPropertyExpression.put(measurementPropertyName, new HashMap<StringBuilder, StringBuilder>());
                            }
                            final Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression.get(measurementPropertyName);

                            conditionMeasurementPropertyMapping.put(conditionExpression, measurementPropertyExpression.get(measurementPropertyName));
                        }
                    }

                }

                for (final String measurementPropertyName : measurementPropertyNames) {
                    if (attributeMeasurementPropertyExpression.containsKey(measurementPropertyName)) {
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
                        exprs[i] = new SDFExpression(expression.toString(), MEP.getInstance());
                    }
                    else {
                        exprs[i] = new SDFExpression("-1.0", MEP.getInstance());
                    }
                    i++;
                }
            }
            else {
                exprs[i] = new SDFExpression("-1.0", MEP.getInstance());
            }
        }
        return exprs;
    }

    /**
     * Get all sensing devices providing the given attribute
     *
     * @param attribute
     *            The attribute
     * @return The list of sensing devices
     */
    private static List<SensingDevice> getSensingDevices(final SDFAttribute attribute) {
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
    private static StringBuilder getConditionExpression(final MeasurementCapability measurementCapability) {
        final List<Condition> conditions = measurementCapability.getInConditions();

        final StringBuilder conditionExpression = new StringBuilder();

        for (final Condition condition : conditions) {
            final List<String> observerAttributeUris = Activator.getSensorOntologyService().getAttributes(condition);

            if (!observerAttributeUris.isEmpty()) {
                if (conditionExpression.length() != 0) {
                    // Concatenate each condition with an AND
                    conditionExpression.append(" AND ");
                }

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
    private static Map<String, StringBuilder> getMeasurementPropertyExpressions(final MeasurementCapability measurementCapability) {
        final Map<String, StringBuilder> measurementPropertyExpressions = new HashMap<>();
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
