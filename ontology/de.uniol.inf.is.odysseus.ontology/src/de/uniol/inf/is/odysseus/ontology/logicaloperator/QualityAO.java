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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.ontology.SensorOntologyServiceImpl;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc = "Append quality information to the incoming stream object.", name = "Quality", category = { LogicalOperatorCategory.ONTOLOGY })
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
	 * @param qualityAO
	 *            The instance to clone from
	 */
	public QualityAO(final QualityAO qualityAO) {
		super(qualityAO);
		Objects.requireNonNull(qualityAO);
		this.attributes = new ArrayList<SDFAttribute>(qualityAO.attributes);
		this.properties = new ArrayList<String>(qualityAO.properties);
		if (qualityAO.expressions != null) {
			this.expressions = qualityAO.expressions.clone();
		}
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = false, isList = true)
	public void setAttributes(List<SDFAttribute> attributes) {
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
	public void setProperties(List<String> properties) {
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

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		calcOutputSchema();
		return getOutputSchema();
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
	
	@Override
	public void initialize() {
		super.initialize();
		Objects.requireNonNull(attributes);
		Objects.requireNonNull(properties);
		Preconditions.checkArgument(!attributes.isEmpty());
		Preconditions.checkArgument(!properties.isEmpty());
		Preconditions.checkState(SensorOntologyServiceImpl.getOntology()!=null);
	}

	private void calcOutputSchema() {
		List<SDFAttribute> outputSchemaAttributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : getAttributes()) {
			outputSchemaAttributes.add(attribute);
		}
		for (SDFAttribute attribute : getAttributes()) {
			for (String property : properties) {
				SDFAttribute propertyAttribute = new SDFAttribute(
						attribute.getSourceName(), attribute.getAttributeName()
								+ "_" + property, SDFDatatype.DOUBLE);
				outputSchemaAttributes.add(propertyAttribute);
			}
		}
		setOutputSchema(new SDFSchema(getInputSchema().getURI(),
				getInputSchema().getType(), outputSchemaAttributes));
		this.expressions = estimateExpressions();
	}

	private SDFExpression[] estimateExpressions() {
		List<SDFAttribute> attributes = getAttributes();
		List<String> measurementPropertyNames = getProperties();

		SDFExpression[] expressions = new SDFExpression[attributes.size()
				* measurementPropertyNames.size() + attributes.size()];

		int i = 0;
		for (SDFAttribute attribute : attributes) {
			expressions[i] = new SDFExpression(attribute.getAttributeName(),
					MEP.getInstance());
			i++;
		}

		for (SDFAttribute attribute : attributes) {
			List<SensingDevice> sensingDevices = getSensingDevices(attribute);
			// if (!sensingDevices.isEmpty()) {
			// FIXME What happens if there are more than one sensing device?
			SensingDevice sensingDevice = sensingDevices.get(0);

			List<MeasurementCapability> measurementCapabilities = sensingDevice
					.getHasMeasurementCapabilities(attribute.getAttributeName());
			Map<String, Map<StringBuilder, StringBuilder>> attributeMeasurementPropertyExpression = new HashMap<String, Map<StringBuilder, StringBuilder>>();

			for (MeasurementCapability measurementCapability : measurementCapabilities) {

				Map<String, StringBuilder> measurementPropertyExpression = getMeasurementPropertyExpressions(measurementCapability);
				StringBuilder conditionExpression = getConditionExpression(measurementCapability);

				for (String measurementPropertyName : measurementPropertyExpression
						.keySet()) {
					if (!attributeMeasurementPropertyExpression
							.containsKey(measurementPropertyName)) {
						attributeMeasurementPropertyExpression.put(
								measurementPropertyName,
								new HashMap<StringBuilder, StringBuilder>());
					}
					Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression
							.get(measurementPropertyName);

					conditionMeasurementPropertyMapping.put(
							conditionExpression, measurementPropertyExpression
									.get(measurementPropertyName));
				}

			}
			// }
			for (String measurementPropertyName : measurementPropertyNames) {
				Map<StringBuilder, StringBuilder> conditionMeasurementPropertyMapping = attributeMeasurementPropertyExpression
						.get(measurementPropertyName);
				StringBuilder expression = new StringBuilder();

				for (StringBuilder conditionExpression : conditionMeasurementPropertyMapping
						.keySet()) {
					if (expression.length() != 0) {
						expression.append(", ");
					}
					expression.append("eif(");
					expression.append(conditionExpression);
					expression.append(",");
					expression.append("sMin(");
					expression.append(conditionMeasurementPropertyMapping
							.get(conditionExpression));
					expression.append(")");
					expression.append(",");
					// expression.append(Double.MAX_VALUE);
					expression.append(1.0);
					expression.append(")");
				}
				expression.insert(0, "sMin([");
				expression.append("])");
				expressions[i] = new SDFExpression(expression.toString(),
						MEP.getInstance());
				i++;
			}

		}
		return expressions;
	}

	private List<SensingDevice> getSensingDevices(SDFAttribute attribute) {
		return SensorOntologyServiceImpl.getOntology().getSensingDevices(
				attribute);
	}

	private StringBuilder getConditionExpression(
			MeasurementCapability measurementCapability) {
		List<Condition> conditions = measurementCapability.getInConditions();

		StringBuilder conditionExpression = new StringBuilder();

		for (Condition condition : conditions) {
			if (conditionExpression.length() != 0) {
				conditionExpression.append(" AND ");
			}

			List<SDFAttribute> observerAttributes = SensorOntologyServiceImpl
					.getOntology().getAttributes(condition);

			conditionExpression.append("(");
			conditionExpression.append(String.format(condition.toString(),
					observerAttributes.get(0).getAttributeName()));
			conditionExpression.append(")");
		}

		return conditionExpression;
	}

	private Map<String, StringBuilder> getMeasurementPropertyExpressions(
			MeasurementCapability measurementCapability) {
		Map<String, StringBuilder> measurementPropertyExpressions = new HashMap<String, StringBuilder>();
		List<MeasurementProperty> measurementProperties = measurementCapability
				.getHasMeasurementProperties();

		for (MeasurementProperty measurementProperty : measurementProperties) {
			if (measurementProperty != null) {
				StringBuilder expressionBuilder;
				if (!measurementPropertyExpressions
						.containsKey(measurementProperty.getResource()
								.getLocalName())) {
					expressionBuilder = new StringBuilder();
					measurementPropertyExpressions.put(measurementProperty
							.getResource().getLocalName(), expressionBuilder);
				} else {
					expressionBuilder = measurementPropertyExpressions
							.get(measurementProperty.getResource()
									.getLocalName());
				}
				if (expressionBuilder.length() != 0) {
					expressionBuilder.append(", ");
				}
				if (measurementProperty.getExpression() != null) {
					expressionBuilder.append(measurementProperty
							.getExpression());
				} else {
					expressionBuilder.append(0.0);
				}
			}
		}
		for (String measurementPropertyName : measurementPropertyExpressions
				.keySet()) {
			StringBuilder expressionBuilder = measurementPropertyExpressions
					.get(measurementPropertyName);
			expressionBuilder.insert(0, "[");
			expressionBuilder.append("]");
		}
		return measurementPropertyExpressions;
	}
}
