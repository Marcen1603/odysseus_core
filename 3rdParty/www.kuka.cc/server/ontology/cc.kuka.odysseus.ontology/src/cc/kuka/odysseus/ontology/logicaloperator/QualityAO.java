/*******************************************************************************
 * Copyright (C) 2014 Christian Kuka <christian@kuka.cc> This program is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either
 * version 2 of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.logicaloperator;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.ontology.Activator;
import cc.kuka.odysseus.ontology.common.SensorOntologyService;
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
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc = "Append quality information to the incoming stream object.", name = "Quality", url = "https://kuka.cc/software/odysseus/ontology", category = {
        LogicalOperatorCategory.ONTOLOGY })
public class QualityAO extends UnaryLogicalOp {

    /**
     *
     */
    private static final long serialVersionUID = 7153504084002972374L;
    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(QualityAO.class);

    /** MEP function symbols used for quality expressions. */
    private static final String TO_DOUBLE = "toDouble";
    private static final String EIF = "eif";
    private static final String MAX = "max";
    private static final String AND = "AND";

    private final List<SDFAttribute> attributes = new ArrayList<>();
    private final List<String> measurementProperties = new ArrayList<>();
    // @Nullable
    private SDFExpression[] expressions;

    /**
     * Class constructor.
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
        this.attributes.addAll(clone.attributes);
        this.measurementProperties.addAll(clone.measurementProperties);
        synchronized (clone.expressions) {
            final SDFExpression[] cloneExpressions = clone.expressions;
            if (cloneExpressions != null) {
                this.expressions = new SDFExpression[cloneExpressions.length];
                for (int i = 0; i < this.expressions.length; i++) {
                    // @Nullable
                    final SDFExpression cloneExpression = cloneExpressions[i];
                    if (cloneExpression != null) {
                        this.expressions[i] = cloneExpression.clone();
                    }
                }
            }
        }
    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = false, isList = true, doc = "The list of attributes")
    public void setAttributes(final List<SDFAttribute> attributes) {
        QualityAO.LOG.trace("attributes({})", attributes);
        this.attributes.clear();
        this.attributes.addAll(attributes);
    }

    @GetParameter(name = "ATTRIBUTES")
    public List<SDFAttribute> getAttributes() {
        QualityAO.LOG.trace("attributes()");

        return Collections.unmodifiableList(this.attributes);
    }

    @Parameter(type = StringParameter.class, name = "PROPERTIES", optional = false, isList = true, doc = "The list of measurement property names")
    public void setProperties(final List<String> properties) {
        QualityAO.LOG.trace("properties({})", properties);

        this.measurementProperties.clear();
        this.measurementProperties.addAll(properties);
    }

    @GetParameter(name = "PROPERTIES")
    public List<String> getProperties() {
        QualityAO.LOG.trace("properties()");

        return Collections.unmodifiableList(this.measurementProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SDFSchema getOutputSchemaIntern(final int pos) {
        this.calcOutputSchema();
        return this.getOutputSchema();
    }

    // @Nullable
    public SDFExpression[] expressions() {
        QualityAO.LOG.trace("expressions()");
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
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        QualityAO.LOG.trace("initialize()");

        super.initialize();
    }

    @Override
    public boolean isValid() {
        QualityAO.LOG.trace("isValid()");

        this.addError(Activator.getSensorOntologyService() == null, "Sensor ontology service not ready");
        this.addError(this.attributes.isEmpty(), "No attributes selected");
        this.addError(this.measurementProperties.isEmpty(), "No properties selected");

        for (final SDFAttribute attribute : this.attributes) {
            final List<SensingDevice> sensingDevices = QualityAO.sensingDevices(attribute);
            if (sensingDevices.isEmpty()) {
                this.addError(this.measurementProperties.isEmpty(), "No sensing device observes " + attribute.getURI());
            }
            else {
                for (final String measurementProperty : this.measurementProperties) {
                    boolean availableMeasurementProperty = false;
                    for (final SensingDevice sensingDevice : sensingDevices) {
                        final String attributeName = attribute.getAttributeName();
                        if (attributeName != null) {
                            final List<MeasurementCapability> measurementCapabilities = sensingDevice
                                    .hasMeasurementCapabilities(attributeName);
                            for (final MeasurementCapability measurementCapability : measurementCapabilities) {
                                final List<MeasurementProperty> measurementProperties = measurementCapability
                                        .hasMeasurementProperty(measurementProperty);
                                if (!measurementProperties.isEmpty()) {
                                    availableMeasurementProperty = true;
                                }
                            }
                        }
                    }
                    this.addError(!availableMeasurementProperty,
                            "No sensing device is capable to provide information about " + measurementProperty);
                }
            }
        }
        return !this.hasErrors();
    }

    private void calcOutputSchema() {
        QualityAO.LOG.trace("calcOutputSchema()");

        final List<SDFAttribute> outputSchemaAttributes = new ArrayList<>();

        for (final SDFAttribute attribute : this.getInputSchema()) {
            if (attribute != null) {
                outputSchemaAttributes.add(attribute.clone());
            }
        }
        for (final SDFAttribute attribute : this.getAttributes()) {
            for (final String property : this.measurementProperties) {
                final SDFAttribute propertyAttribute = new SDFAttribute(attribute.getURIWithoutQualName(),
                        attribute.getAttributeName() + "_" + property, SDFDatatype.DOUBLE, attribute.getUnit(),
                        attribute.getDtConstraints());
                outputSchemaAttributes.add(propertyAttribute);
            }
        }

        this.setOutputSchema(SDFSchemaFactory.createNewSchema(this.getInputSchema().getURI(),
                this.getInputSchema().getType(), outputSchemaAttributes));
        this.expressions = this.estimateExpressions();
    }

    /**
     * Constructs the expressions for the measurement properties. The expression
     * for each property is of the form: sMin(eif((Condition1_1) AND
     * (Condition1_2), max([Property1_1Expr1, Property1_1Expr2]), DOUBLE.MAX),
     * eif((Condition2_1) AND (Condition2_2), max([Property2_1Expr1,
     * Property2_2Expr2]),DOUBLE.MAX)) With ConditionX_Y being different
     * conditions for one measurement capability. And PropertyX_YExprZ being an
     * expression for one property under the given condition
     *
     * @return The expressions used during processing
     */
    private SDFExpression[] estimateExpressions() {
        QualityAO.LOG.trace("estimateExpression()");

        final List<SDFAttribute> attrs = this.getAttributes();
        final List<String> measurementPropertyNames = this.getProperties();

        final SDFExpression[] exprs = new SDFExpression[(attrs.size() * measurementPropertyNames.size())
                                                        + this.getInputSchema().size()];

        int expressionIndex = 0;
        for (final SDFAttribute attribute : this.getInputSchema()) {
            exprs[expressionIndex] = new SDFExpression(attribute.getAttributeName(), null, MEP.getInstance());
            expressionIndex++;
        }

        for (final SDFAttribute attribute : attrs) {
            final List<SensingDevice> sensingDevices = QualityAO.sensingDevices(attribute);
            if (!sensingDevices.isEmpty()) {
                // FIXME What happens if there are more than one sensing device?
                final SensingDevice sensingDevice = sensingDevices.get(0);

                final String attributeName = attribute.getAttributeName();
                if (attributeName != null) {
                    final List<MeasurementCapability> measurementCapabilities = sensingDevice
                            .hasMeasurementCapabilities(attributeName);
                    final Map<String, Map<StringBuilder, StringBuilder>> attributeMeasurementPropertyExpression = new HashMap<>();

                    for (final MeasurementCapability measurementCapability : measurementCapabilities) {

                        final Map<String, StringBuilder> measurementPropertyExpression = QualityAO
                                .measurementPropertyExpressions(measurementCapability);
                        final StringBuilder conditionExpression = QualityAO.conditionExpression(measurementCapability);

                        if (conditionExpression.length() > 0) {
                            for (final String measurementPropertyName : measurementPropertyExpression.keySet()) {
                                if (!attributeMeasurementPropertyExpression.containsKey(measurementPropertyName)) {
                                    attributeMeasurementPropertyExpression.put(measurementPropertyName,
                                            new HashMap<StringBuilder, StringBuilder>());
                                }
                                final Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression
                                        .get(measurementPropertyName);

                                conditionMeasurementPropertyMapping.put(conditionExpression,
                                        measurementPropertyExpression.get(measurementPropertyName));
                            }
                        }
                    }

                    for (final String measurementPropertyName : measurementPropertyNames) {
                        if (attributeMeasurementPropertyExpression.containsKey(measurementPropertyName)) {
                            final Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression
                                    .get(measurementPropertyName);
                            final StringBuilder expression = new StringBuilder();

                            for (final StringBuilder conditionExpression : conditionMeasurementPropertyMapping
                                    .keySet()) {
                                if (expression.length() != 0) {
                                    expression.append(", ");
                                }
                                expression.append(QualityAO.TO_DOUBLE).append("(").append(QualityAO.EIF).append("(");
                                expression.append(conditionExpression);
                                expression.append(",");
                                expression.append(QualityAO.TO_DOUBLE).append("(").append(QualityAO.MAX).append("(");
                                expression.append(conditionMeasurementPropertyMapping.get(conditionExpression));
                                expression.append(")").append(")");
                                expression.append(",");
                                expression.append(QualityAO.TO_DOUBLE).append("(0.0)");
                                expression.append(")").append(")");
                            }
                            expression.insert(0, QualityAO.TO_DOUBLE + "(" + QualityAO.MAX + "([");
                            expression.append("]))");
                            exprs[expressionIndex] = new SDFExpression(expression.toString(), null, MEP.getInstance());
                        }
                        else {

                            exprs[expressionIndex] = new SDFExpression(QualityAO.TO_DOUBLE + "(-1.0)", null,
                                    MEP.getInstance());
                        }
                        expressionIndex++;
                    }
                }
            }
            else {
                exprs[expressionIndex] = new SDFExpression(QualityAO.TO_DOUBLE + "(-1.0)", null, MEP.getInstance());
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

    private static List<SensingDevice> sensingDevices(final SDFAttribute attribute) {
        QualityAO.LOG.trace("sensingDevices({})", attribute);

        Objects.requireNonNull(attribute);
        Objects.requireNonNull(Activator.getSensorOntologyService());
        // @Nullable
        final SensorOntologyService ontologyService = Activator.getSensorOntologyService();
        if (ontologyService != null) {
            return Activator.getSensorOntologyService().sensingDevices(SDFUtils.getFeatureOfInterestLabel(attribute),
                    SDFUtils.getSensingDeviceLabel(attribute), attribute.getAttributeName());
        }
        return new ArrayList<>();
    }

    /**
     * Constructs the condition expression for the given measurement capability
     * and inserts the attribute name observed condition property. The condition
     * looks like: (con1) AND (con2) AND ...
     *
     * @param measurementCapability
     *            The measurement capability
     * @return A {@link StringBuilder} object containing the condition predicate
     */
    private static StringBuilder conditionExpression(final MeasurementCapability measurementCapability) {
        QualityAO.LOG.trace("conditionExpression({})", measurementCapability);

        final List<Condition> conditions = measurementCapability.inConditions();

        final StringBuilder conditionExpression = new StringBuilder();

        for (final Condition condition : conditions) {
            final SensorOntologyService ontologyService = Activator.getSensorOntologyService();
            if (ontologyService != null) {
                final List<String> observerAttributeUris = ontologyService.attributes(condition);

                if (!observerAttributeUris.isEmpty()) {
                    if (conditionExpression.length() != 0) {
                        // Concatenate each condition with an AND
                        conditionExpression.append(" ").append(QualityAO.AND).append(" ");
                    }

                    final List<SDFAttribute> observerAttributes = new ArrayList<>();
                    for (final String uri : observerAttributeUris) {
                        final String[] split = SDFElement.splitURI(uri);
                        observerAttributes.add(new SDFAttribute(split[0], split[1], SDFDatatype.DOUBLE));
                    }
                    conditionExpression.append("(");
                    // Inject the observed attribute name for evaluation during
                    // processing
                    conditionExpression
                    .append(String.format(condition.toString(), observerAttributes.get(0).getAttributeName()));
                    conditionExpression.append(")");
                }
            }
        }

        return conditionExpression;
    }

    /**
     * Construct the vectors for the measurement properties and insert the
     * attribute name of the measurement capability. The vector consists of
     * single expressions that are evaluated during processing. The vector looks
     * like: [expr1, expr2, ...]
     *
     * @param measurementCapability
     *            The measurement capability
     * @return A map including the name of the property and a vector of
     *         expressions to evaluate
     */
    private static Map<String, StringBuilder> measurementPropertyExpressions(
            final MeasurementCapability measurementCapability) {
        QualityAO.LOG.trace("measurementPropertyExpressions({})", measurementCapability);

        final Map<String, StringBuilder> measurementPropertyExpressions = new HashMap<>();
        final List<MeasurementProperty> measurementProperties = measurementCapability.hasMeasurementProperties();

        for (final MeasurementProperty measurementProperty : measurementProperties) {
            StringBuilder expressionBuilder;
            if (!measurementPropertyExpressions.containsKey(URI.create(measurementProperty.resource()).getFragment())) {
                expressionBuilder = new StringBuilder();
                measurementPropertyExpressions.put(URI.create(measurementProperty.resource()).getFragment(),
                        expressionBuilder);
            }
            else {
                expressionBuilder = measurementPropertyExpressions
                        .get(URI.create(measurementProperty.resource()).getFragment());
            }
            if (expressionBuilder.length() != 0) {
                expressionBuilder.append(", ");
            }
            // Inject the observed attribute name for evaluation during
            // processing
            expressionBuilder.append(String.format(QualityAO.TO_DOUBLE + "(" + measurementProperty.expression() + ")",
                    measurementCapability.name()));
        }
        for (final String measurementPropertyName : measurementPropertyExpressions.keySet()) {
            final StringBuilder expressionBuilder = measurementPropertyExpressions.get(measurementPropertyName);
            expressionBuilder.insert(0, "[");
            expressionBuilder.append("]");
        }
        return measurementPropertyExpressions;
    }
}