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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
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
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityAOInitRule extends AbstractTransformationRule<QualityAO> {
    /** The Logger. */
    @SuppressWarnings("unused")
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

        Map<SensingDevice, List<Property>> toJoinStreams = new HashMap<SensingDevice, List<Property>>();

        for (SDFAttribute attribute : attributes) {

            Map<String, Map<String, String>> conditionPropertyMapping = new HashMap<String, Map<String, String>>();

            Property property = new Property(SDFUtils.getPropertyURI(attribute));
            SensingDevice sensingDevice = queryManager.getSensingDevice(SDFUtils.getSensingDeviceURI(attribute));
            FeatureOfInterest featureOfInterest = queryManager.getFeatureOfInterest(SDFUtils.getFeatureOfInterestURI(attribute));

            List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(property);
            for (MeasurementCapability measurementCapability : measurementCapabilities) {

                List<Condition> conditions = measurementCapability.getInConditions();

                for (Condition condition : conditions) {
                    Property onProperty = condition.getOnProperty();
                    List<SensingDevice> observers = queryManager.getSensingDevicesByObservedProperty(onProperty.getUri());
                    if (!observers.contains(sensingDevice)) {
                        if (!toJoinStreams.containsKey(observers.get(0))) {
                            toJoinStreams.put(observers.get(0), new ArrayList<Property>());
                        }
                        toJoinStreams.get(observers.get(0)).add(onProperty);
                    }
                }

            }
            concatConditionObserverStreams(operator, toJoinStreams);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final QualityAO operator, final TransformationConfiguration transformConfig) {
        if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
            return true;
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
        return TransformRuleFlowGroup.INIT;
    }

    /**
     * Concatenates the condition observing sensing devices to this stream.
     * 
     * @param observers
     *            The map of sensing devices and the necessary properties.
     */

    private void concatConditionObserverStreams(ILogicalOperator operator, Map<SensingDevice, List<Property>> observers) {
        for (SensingDevice sensingDevice : observers.keySet()) {
            JoinAO joinAO = insertJoinAO(operator, sensingDevice);
            insertProjectAO(joinAO, observers.get(sensingDevice));
            // FIXME Insert Window?
        }
    }

    private JoinAO insertJoinAO(ILogicalOperator operator, SensingDevice sensingDevice) {
        JoinAO joinAO = new JoinAO();
        joinAO.subscribeToSource(null, 1, 0, null);

        RestructHelper.insertOperatorBefore(joinAO, operator);
        // FIXME Get view/source of sensing device

        joinAO.initialize();
        insert(joinAO);
        return joinAO;
    }

    private void insertProjectAO(ILogicalOperator operator, List<Property> properties) {
        ProjectAO projectAO = new ProjectAO();
        // FIXME set output schema to projected attributes
        projectAO.setOutputSchema(null);
        RestructHelper.insertOperatorBefore(projectAO, operator);
        projectAO.initialize();
        insert(projectAO);
    }
}
