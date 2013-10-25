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
package de.uniol.inf.is.odysseus.probabilistic.sensor.transform;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.sensor.logicaloperator.QualityAO;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.SensorOntologyServiceImpl;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityAORule extends AbstractTransformationRule<QualityAO> {
    /** The Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TQualityAORule.class);

    @Override
    public final int getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(final QualityAO operator, final TransformationConfiguration transformConfig) {
        SDFSchema schema = operator.getInputSchema();

        Object[] expressions = new Object[schema.getAttributes().size()];
        for (int i = 0; i < schema.getAttributes().size(); i++) {
            SDFAttribute attribute = schema.getAttribute(i);
            SensingDevice thisSensingDevice = SensorOntologyServiceImpl.getOntology().getSensingDevice(attribute.getQualName());
            if (thisSensingDevice != null) {
                List<MeasurementCapability> measurementCapabilities = thisSensingDevice.getCapabilities(attribute);

                if (measurementCapabilities != null) {
                    for (MeasurementCapability measurementCapability : measurementCapabilities) {
                        List<Condition> conditions = measurementCapability.getConditions();
                        for (Condition condition : conditions) {
                            List<SensingDevice> sensingDevices = SensorOntologyServiceImpl.getOntology().getSensingdeviceByProperty(condition.getAttribute());
                            if (!sensingDevices.isEmpty()) {
                                // TODO join sensingDevice to the current stream
                                String attributeName = condition.getAttribute().getAttributeName();
                                expressions[i] = attributeName + ">" + condition.getInterval().inf() + " AND " + attributeName + "<" + condition.getInterval().sup();
                            }
                        }
                    }
                }
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

}
