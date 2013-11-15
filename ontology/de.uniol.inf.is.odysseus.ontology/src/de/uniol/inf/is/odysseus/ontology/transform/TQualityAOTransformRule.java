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
package de.uniol.inf.is.odysseus.ontology.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ontology.common.SDFUtils;
import de.uniol.inf.is.odysseus.ontology.logicaloperator.QualityAO;
import de.uniol.inf.is.odysseus.ontology.manager.impl.QueryManagerImpl;
import de.uniol.inf.is.odysseus.ontology.manager.impl.SourceManagerImpl;
import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.ontology.SensorOntologyServiceImpl;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityAOTransformRule extends AbstractTransformationRule<QualityAO> {
    /** The Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TQualityAOTransformRule.class);

    @Override
    public final int getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unused")
    @Override
    public final void execute(final QualityAO operator, final TransformationConfiguration transformConfig) {
        final SDFSchema schema = operator.getInputSchema();
        List<SDFAttribute> attributes = operator.getAttributes();
        List<String> propertyNames = operator.getProperties();

        SourceManagerImpl sourceManager = new SourceManagerImpl(null);
        QueryManagerImpl queryManager = new QueryManagerImpl(null);

        for (SDFAttribute attribute : attributes) {

            Map<SensingDevice, List<Property>> toJoinStreams = new HashMap<SensingDevice, List<Property>>();
            Map<String, Map<String, String>> conditionPropertyMapping = new HashMap<String, Map<String, String>>();

            Property property = new Property(SDFUtils.getPropertyURI(attribute));
            SensingDevice sensingDevice = queryManager.getSensingDevice(SDFUtils.getSensingDeviceURI(attribute));
            FeatureOfInterest featureOfInterest = queryManager.getFeatureOfInterest(SDFUtils.getFeatureOfInterestURI(attribute));

            List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(property);
            for (MeasurementCapability measurementCapability : measurementCapabilities) {

                List<Condition> conditions = measurementCapability.getInConditions();

                String conditionExpression = toConditionString(conditions);
                conditionPropertyMapping.put(conditionExpression, new HashMap<String, String>());

                for (String propertyName : propertyNames) {
                    List<MeasurementProperty> measurementProperties = measurementCapability.getHasMeasurementProperty(propertyName);
                    if (!measurementProperties.isEmpty()) {
                        // FIXME Can we have more than one expression?
                        for (MeasurementProperty measurementProperty : measurementProperties) {

                            conditionPropertyMapping.get(conditionExpression).put(propertyName, measurementProperty.getExpression());
                        }
                    }

                }
            }
        }

        // Required parameter:
        // - Attribute
        // - Measurement Property
        final Object[] expressions = new Object[schema.getAttributes().size()];
        for (int i = 0; i < schema.getAttributes().size(); i++) {
            final SDFAttribute attribute = schema.getAttribute(i);
            final SensingDevice thisSensingDevice = SensorOntologyServiceImpl.getOntology().getSensingDevice(attribute.getQualName());
            if (thisSensingDevice != null) {
                // final List<MeasurementCapability> measurementCapabilities =
                // thisSensingDevice.getHasMeasurementCapabilities(attribute);
                //
                // if (measurementCapabilities != null) {
                // for (final MeasurementCapability measurementCapability :
                // measurementCapabilities) {
                // final List<AbstractCondition> conditions =
                // measurementCapability.getConditions();
                // for (final AbstractCondition condition : conditions) {
                // final List<SensingDevice> sensingDevices =
                // SensorOntologyServiceImpl.getOntology().getSensingdeviceByProperty(condition.getAttribute());
                // if (!sensingDevices.isEmpty()) {
                // // TODO join sensingDevice to the current stream
                // final String attributeName =
                // condition.getAttribute().getAttributeName();
                // expressions[i] = attributeName + ">" +
                // condition.getInterval().inf() + " AND " + attributeName + "<"
                // + condition.getInterval().sup();
                // }
                // }
                // }
                // }
            }
            else {
                LOG.debug("No sensing device found");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final QualityAO operator, final TransformationConfiguration transformConfig) {
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "QualityAO -> QualityPO(probabilistic)";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /**
     * Concatenates the condition observing sensing devices to this stream.
     * 
     * @param observers
     *            The map of sensing devices and the necessary properties.
     */

    private String toConditionString(List<Condition> conditions) {
        StringBuilder sb = new StringBuilder();
        for (Condition condition : conditions) {
            if (sb.length() != 0) {
                sb.append(" OR ");
            }
            sb.append(condition.toString());
        }

        return sb.toString();

    }
}
