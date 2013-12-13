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
import java.util.Objects;

import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ontology.logicaloperator.QualityAO;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.ontology.SensorOntologyServiceImpl;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityAOInitRule extends AbstractTransformationRule<QualityAO> {
    /** The Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TQualityAOTransformRule.class);

    @Override
    public final int getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(final QualityAO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getProperties());
        Objects.requireNonNull(transformConfig);

        final SDFSchema schema = operator.getInputSchema();
        final List<SDFAttribute> attributes = operator.getAttributes();

        final Map<String, List<SDFAttribute>> toJoinStreams = new HashMap<String, List<SDFAttribute>>();
        final Map<String, Double> frequencies = new HashMap<String, Double>();
        for (final SDFAttribute attribute : attributes) {

            final List<SensingDevice> sensingDevices = SensorOntologyServiceImpl.getOntology().getSensingDevices(attribute);
            if (!sensingDevices.isEmpty()) {
                // FIXME What happens if there are more than one sensing device?
                final SensingDevice sensingDevice = sensingDevices.get(0);
                final List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(attribute.getAttributeName());
                for (final MeasurementCapability measurementCapability : measurementCapabilities) {

                    final List<Condition> conditions = measurementCapability.getInConditions();
                    final List<MeasurementProperty> measurementProperties = measurementCapability.getHasMeasurementProperties();

                    double frequency = 0.0;
                    for (final MeasurementProperty measurementProperty : measurementProperties) {
                        if ((measurementProperty != null) && (measurementProperty.getResource().getURI().equalsIgnoreCase(SSN.Frequency.getURI()))) {
                            frequency = Double.parseDouble(measurementProperty.getExpression());
                        }
                    }
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
                                if (!frequencies.containsKey(observerAttributes.get(0).getSourceName())) {
                                    frequencies.put(observerAttributes.get(0).getSourceName(), 0.0);
                                }
                                toJoinStreams.get(observerAttributes.get(0).getSourceName()).add(observerAttributes.get(0));
                                frequencies.put(observerAttributes.get(0).getSourceName(), FastMath.max(frequencies.get(observerAttributes.get(0).getSourceName()), frequency));
                            }
                        }
                    }

                }
            }
            else {
                LOG.warn("No sensing devices observe {}", attribute.getAttributeName());
            }
        }
        this.concatConditionObserverStreams(operator, toJoinStreams, frequencies);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final QualityAO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(transformConfig);
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
     * @param operator
     *            The {@link QualityAO} operator
     * @param observers
     *            The map of sensing devices and the necessary properties.
     * @param frequencies
     *            The list of frequencies of each sensing device used to
     *            calculate the required window size
     */
    private void concatConditionObserverStreams(final ILogicalOperator operator, final Map<String, List<SDFAttribute>> observers, final Map<String, Double> frequencies) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema(0));
        Objects.requireNonNull(observers);
        Objects.requireNonNull(frequencies);

        final SDFSchema schema = operator.getInputSchema(0);

        for (final String sourceName : observers.keySet()) {
            final JoinAO joinAO = this.insertCrossproductAO(operator);
            joinAO.setName("Join " + sourceName);
            final ILogicalOperator viewOrStream = this.insertViewOrStream(joinAO, sourceName);
            final ProjectAO projectAO = this.insertProjectAO(joinAO, observers.get(sourceName));
            final WindowAO windowAO = this.insertWindowAO(projectAO, 1.0);

            viewOrStream.initialize();
            this.insert(viewOrStream);
            projectAO.initialize();
            this.insert(projectAO);
            windowAO.initialize();
            this.insert(windowAO);
            joinAO.initialize();
            this.insert(joinAO);

            joinAO.setOutputSchema(SDFSchema.union(schema, viewOrStream.getOutputSchema()));
        }
    }

    /**
     * Append the given source to the processing graph.
     * 
     * @param parent
     *            The parent operator
     * @param sourceName
     *            The name of the source
     * @return The view or stream with the given name subscribed to the parent
     */
    private ILogicalOperator insertViewOrStream(final ILogicalOperator parent, final String sourceName) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(sourceName);

        final ILogicalOperator viewOrStream = this.getDataDictionary().getViewOrStream(sourceName, this.getCaller());

        parent.subscribeToSource(viewOrStream, 1, 0, viewOrStream.getOutputSchema());

        return viewOrStream;
    }

    /**
     * Inserts the {@link JoinAO} operator to join the view or stream to the
     * current processing graph.
     * 
     * @param parent
     *            The parent operator
     * @return The {@link JoinAO} subscribed to the parent
     */
    private JoinAO insertCrossproductAO(final ILogicalOperator parent) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));

        final ILogicalOperator child = parent.getSubscribedToSource(0).getTarget();

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

    /**
     * Inserts the {@link ProjectAO} operator to project the stream to the
     * necessary attributes.
     * 
     * @param parent
     *            The parent operator
     * @param attributes
     *            The list of {@link SDFAttribute} attributes
     * @return The {@link ProjectAO} subscribed to the parent
     */
    private ProjectAO insertProjectAO(final ILogicalOperator parent, final List<SDFAttribute> attributes) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(1));
        Objects.requireNonNull(attributes);

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

    /**
     * Inserts the {@link WindowAO} operator to set the end timestamp for the
     * crossproduct operation.
     * 
     * @param parent
     *            The parent operator
     * @param frequency
     *            The frequency of the source
     * @return The {@link WindowAO} subscribed to the parent
     */
    private WindowAO insertWindowAO(final ILogicalOperator parent, final double frequency) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(1));
        Preconditions.checkArgument(frequency >= 0.0);

        final WindowAO windowAO = new WindowAO();
        final ILogicalOperator child = parent.getSubscribedToSource(1).getTarget();

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            windowAO.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        windowAO.subscribeToSource(child, 0, 0, child.getOutputSchema());

        if (frequency > 0.0) {
            windowAO.setWindowType(WindowType.TIME);
            windowAO.setWindowAdvance(1L);
            windowAO.setWindowSize((long) (1.0 / frequency));
        }
        else {
            windowAO.setWindowType(WindowType.TUPLE);
            windowAO.setWindowAdvance(1L);
            windowAO.setWindowSize(1L);
        }
        return windowAO;
    }

    /**
     * Check if there is already a join operator under the given operator.
     * FIXME need a better check if the {@link QualityAO} was already extended
     * 
     * @param operator
     *            The operator
     * @return <code>true</code> if there is already a join operator attached to
     *         the operator
     */
    private boolean hasJoinAOAsChild(final ILogicalOperator operator) {
        Objects.requireNonNull(operator);

        final LogicalSubscription child = operator.getSubscribedToSource(0);
        return ((child != null) && (child.getTarget() instanceof JoinAO));
    }
}
