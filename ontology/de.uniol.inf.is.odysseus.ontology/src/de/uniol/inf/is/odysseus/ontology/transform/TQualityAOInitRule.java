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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ontology.logicaloperator.QualityAO;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.ontology.SensorOntologyServiceImpl;
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
        final List<SDFAttribute> attributes = operator.getAttributes();
        final List<String> measurementPropertyNames = operator.getProperties();

        final Map<String, List<SDFAttribute>> toJoinStreams = new HashMap<String, List<SDFAttribute>>();

        for (final SDFAttribute attribute : attributes) {

            final Map<String, Map<String, String>> conditionPropertyMapping = new HashMap<String, Map<String, String>>();

            final List<SensingDevice> sensingDevices = SensorOntologyServiceImpl.getOntology().getSensingDevices(attribute);
            if (!sensingDevices.isEmpty()) {
                // FIXME What happens if there are more than one sensing device?
                final SensingDevice sensingDevice = sensingDevices.get(0);
                final List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(attribute.getAttributeName());
                for (final MeasurementCapability measurementCapability : measurementCapabilities) {

                    final List<Condition> conditions = measurementCapability.getInConditions();

                    for (final Condition condition : conditions) {
                        final List<SDFAttribute> observerAttributes = SensorOntologyServiceImpl.getOntology().getAttributes(condition);

                        boolean inSchema = false;
                        if (!observerAttributes.isEmpty()) {
                            for (final SDFAttribute observerAttribute : observerAttributes) {
                                if (schema.contains(observerAttribute)) {
                                    inSchema = true;
                                }
                            }
                            if (!inSchema) {
                                if (!toJoinStreams.containsKey(observerAttributes.get(0).getSourceName())) {
                                    toJoinStreams.put(observerAttributes.get(0).getSourceName(), new ArrayList<SDFAttribute>());
                                }
                                toJoinStreams.get(observerAttributes.get(0).getSourceName()).add(observerAttributes.get(0));
                            }
                        }
                    }

                }
                this.concatConditionObserverStreams(operator, toJoinStreams);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final QualityAO operator, final TransformationConfiguration transformConfig) {
        // if (operator.getInputSchema().getType() == ProbabilisticTuple.class)
        // {
        if (!this.hasJoinAOAsChild(operator)) {
            return true;
        }
        // }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "QualityAO -> QualityAO|JoinAO|ProjectAO|StreamAO";
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

    private void concatConditionObserverStreams(final ILogicalOperator operator, final Map<String, List<SDFAttribute>> observers) {
        final SDFSchema schema = operator.getInputSchema(0);

        for (final String sourceName : observers.keySet()) {
            final JoinAO joinAO = this.insertJoinAO(operator);
            joinAO.setName("Join " + sourceName);
            final ILogicalOperator viewOrStream = this.insertViewOrStream(joinAO, sourceName);
            final ProjectAO projectAO = this.insertProjectAO(joinAO, observers.get(sourceName));

            viewOrStream.initialize();
            this.insert(viewOrStream);
            projectAO.initialize();
            this.insert(projectAO);
            joinAO.initialize();
            this.insert(joinAO);

            joinAO.setOutputSchema(SDFSchema.union(schema, viewOrStream.getOutputSchema()));

            // FIXME Insert Window?
        }
    }

    private ILogicalOperator insertViewOrStream(final ILogicalOperator parent, final String sourceName) {
        final ILogicalOperator viewOrStream = this.getDataDictionary().getViewOrStream(sourceName, this.getCaller());

        parent.subscribeToSource(viewOrStream, 1, 0, viewOrStream.getOutputSchema());

        return viewOrStream;
    }

    private JoinAO insertJoinAO(final ILogicalOperator parent) {
        final ILogicalOperator child = parent.getSubscribedToSource(0).getTarget();

        System.out.println(child);
        final JoinAO joinAO = new JoinAO();

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            // What about the source out port
            joinAO.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        joinAO.subscribeToSource(child, 0, 0, child.getOutputSchema());

        return joinAO;
    }

    private ProjectAO insertProjectAO(final ILogicalOperator parent, final List<SDFAttribute> attributes) {
        final ProjectAO projectAO = new ProjectAO();
        final ILogicalOperator child = parent.getSubscribedToSource(1).getTarget();
        final List<SDFAttribute> userAwareAttributes = new ArrayList<SDFAttribute>();
        for (final SDFAttribute attr : attributes) {
            userAwareAttributes.add(child.getOutputSchema().findAttribute(attr.getAttributeName()));
        }

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            projectAO.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        projectAO.subscribeToSource(child, 0, 0, child.getOutputSchema());
        projectAO.setOutputSchemaWithList(userAwareAttributes);

        return projectAO;
    }

    private boolean hasJoinAOAsChild(final ILogicalOperator operator) {
        final LogicalSubscription child = operator.getSubscribedToSource(0);
        return ((child != null) && (child.getTarget() instanceof JoinAO));
    }
}
