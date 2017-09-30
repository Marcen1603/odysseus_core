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
package cc.kuka.odysseus.ontology.rewrite.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.FastMath;
// import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import cc.kuka.odysseus.ontology.Activator;
import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.Frequency;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.logicaloperator.QualityAO;
import cc.kuka.odysseus.ontology.utils.SDFUtils;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
//@SuppressWarnings("null")
public class RInsertSensingDevicesRule extends AbstractRewriteRule<QualityAO> {
    /** The Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(RInsertSensingDevicesRule.class);

    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(final QualityAO operator, final RewriteConfiguration config) {
        RInsertSensingDevicesRule.LOG.trace("execute({}, {})", operator, config);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getProperties());
        Objects.requireNonNull(config);

        final SDFSchema schema = operator.getInputSchema();
        final List<SDFAttribute> attributes = operator.getAttributes();

        final Map<String, List<SDFAttribute>> toJoinStreams = new HashMap<>();
        final Map<String, Double> frequencies = new HashMap<>();
        for (final SDFAttribute attribute : attributes) {

            final SensorOntologyService ontologyService = Activator.getSensorOntologyService();
            if (ontologyService != null) {
                final List<SensingDevice> sensingDevices = ontologyService.sensingDevices(
                        SDFUtils.getFeatureOfInterestLabel(attribute), SDFUtils.getSensingDeviceLabel(attribute),
                        attribute.getAttributeName());
                if (!sensingDevices.isEmpty()) {
                    // FIXME What happens if there are more than one sensing
                    // device?
                    final SensingDevice sensingDevice = sensingDevices.get(0);
                    final List<MeasurementCapability> measurementCapabilities = sensingDevice
                            .hasMeasurementCapabilities(attribute.getAttributeName());
                    for (final MeasurementCapability measurementCapability : measurementCapabilities) {
                        final List<Condition> conditions = measurementCapability.inConditions();
                        final List<MeasurementProperty> measurementProperties = measurementCapability
                                .hasMeasurementProperties();

                        double frequency = 0.0;
                        for (final MeasurementProperty measurementProperty : measurementProperties) {
                            if ((measurementProperty != null)
                                    && measurementProperty.resource().equalsIgnoreCase(Frequency.RESOURCE)) {
                                frequency = Double.parseDouble(measurementProperty.expression());
                            }
                        }
                        for (final Condition condition : conditions) {
                            final List<String> observerAttributeUris = ontologyService.attributes(condition);
                            final List<SDFAttribute> observerAttributes = new ArrayList<>();
                            for (final String uri : observerAttributeUris) {
                                final String[] split = SDFElement.splitURI(uri);
                                observerAttributes.add(new SDFAttribute(split[0], split[1], SDFDatatype.DOUBLE));
                            }
                            boolean inSchema = false;
                            if (!observerAttributes.isEmpty()) {
                                for (final SDFAttribute observerAttribute : observerAttributes) {
                                    if (schema.contains(observerAttribute)) {
                                        inSchema = true;
                                        break;
                                    }
                                }
                                if (!inSchema) {
                                    if (!toJoinStreams.containsKey(observerAttributes.get(0).getSourceName())) {
                                        toJoinStreams.put(observerAttributes.get(0).getSourceName(),
                                                new ArrayList<SDFAttribute>());
                                    }
                                    if (!frequencies.containsKey(observerAttributes.get(0).getSourceName())) {
                                        frequencies.put(observerAttributes.get(0).getSourceName(), new Double(0.0));
                                    }
                                    toJoinStreams.get(observerAttributes.get(0).getSourceName())
                                            .add(observerAttributes.get(0));
                                    frequencies.put(observerAttributes.get(0).getSourceName(),
                                            new Double(FastMath.max(frequencies
                                                    .get(observerAttributes.get(0).getSourceName()).doubleValue(),
                                                    frequency)));
                                }
                            }
                        }

                    }
                }
                else {
                    RInsertSensingDevicesRule.LOG.warn("No sensing devices observe {}", attribute.getAttributeName());
                }
            }
        }
        this.concatConditionObserverStreams(operator, toJoinStreams, frequencies);

        final MapAO mapAO = RInsertSensingDevicesRule.insertMapAO(operator);

        final Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(operator, true);
        for (final ILogicalOperator o : toUpdate) {
            this.update(o);
        }
        this.update(mapAO);
        mapAO.initialize();
        this.retract(operator);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(final QualityAO operator, final RewriteConfiguration config) {
        RInsertSensingDevicesRule.LOG.trace("isExecutable({}, {})", operator, config);

        Objects.requireNonNull(operator);
        Objects.requireNonNull(config);
        return operator.getInputSchema().getType().isAssignableFrom(Tuple.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return RewriteRuleFlowGroup.DELETE;
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
    private void concatConditionObserverStreams(final ILogicalOperator operator,
            final Map<String, List<SDFAttribute>> observers, final Map<String, Double> frequencies) {
        RInsertSensingDevicesRule.LOG.trace("concatConditionObserverStreams({}, {}, {})", operator, observers,
                frequencies);

        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema(0));
        Objects.requireNonNull(observers);
        Objects.requireNonNull(frequencies);

        for (final String sourceName : observers.keySet()) {
            final JoinAO joinAO = RInsertSensingDevicesRule.insertCrossproductAO(operator);
            joinAO.setName(UUID.randomUUID() + "_Join " + sourceName);
            final ILogicalOperator viewOrStream = this.insertViewOrStream(joinAO, sourceName);
            final ProjectAO projectAO = RInsertSensingDevicesRule.insertProjectAO(joinAO, observers.get(sourceName));
            final WindowAO windowAO = RInsertSensingDevicesRule.insertWindowAO(projectAO,
                    frequencies.get(sourceName).doubleValue());

            viewOrStream.initialize();
            this.insert(viewOrStream);
            projectAO.initialize();
            this.insert(projectAO);
            windowAO.initialize();
            this.insert(windowAO);
            joinAO.initialize();
            this.insert(joinAO);

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
        RInsertSensingDevicesRule.LOG.trace("insertViewOrStream({}, {})", parent, sourceName);
        Objects.requireNonNull(parent);
        Objects.requireNonNull(sourceName);

        final ILogicalOperator viewOrStream = this.getDataDictionary().getViewOrStream(sourceName, this.getCaller()).getRoot();
        for (final IOperatorOwner owner : parent.getOwner()) {
            viewOrStream.addOwner(owner);
        }
        parent.subscribeToSource(viewOrStream, 1, 0, viewOrStream.getOutputSchema());
        RInsertSensingDevicesRule.LOG.debug("Insert view or stream: {}", viewOrStream.toString());
        return viewOrStream;
    }

    /**
     * Inserts the {@link MapAO} operator to estimate the quality dimensions.
     *
     * @param parent
     *            The parent operator
     * @return The {@link MapAO} subscribed to the parent
     */
    private static MapAO insertMapAO(final QualityAO parent) {
        RInsertSensingDevicesRule.LOG.trace("insertMap({})", parent);

        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));

        final MapAO mapAO = new MapAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            mapAO.addOwner(owner);
        }
        final ILogicalOperator child = parent.getSubscribedToSource(0).getSource();

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            mapAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        mapAO.subscribeToSource(child, 0, 0, child.getOutputSchema());

        final List<NamedExpression> namedExpressions = new ArrayList<>();
        for (int i = 0; i < parent.getOutputSchema().size(); i++) {
            // @Nullable
            final SDFExpression expression = parent.expressions()[i];
            if (expression != null) {
                final SDFExpression expr = new SDFExpression(expression.getMEPExpression().toString(),
                        expression.getAttributeResolver(), MEP.getInstance());
                namedExpressions.add(new NamedExpression(parent.getOutputSchema().get(i).getURI(), expr, null));
            }
            else {
                namedExpressions.add(null);
            }
        }
        mapAO.setExpressions(namedExpressions);
        if (RInsertSensingDevicesRule.LOG.isDebugEnabled()) {
            RInsertSensingDevicesRule.LOG.debug("Insert map operator: {}", mapAO.toString());
            for (int i = 0; i < mapAO.getExpressions().size(); i++) {
                RInsertSensingDevicesRule.LOG.debug("{}. {} ", new Integer(i), mapAO.getExpressions().get(i));
            }
        }

        return mapAO;
    }

    /**
     * Inserts the {@link JoinAO} operator to join the view or stream to the
     * current processing graph.
     *
     * @param parent
     *            The parent operator
     * @return The {@link JoinAO} subscribed to the parent
     */
    private static JoinAO insertCrossproductAO(final ILogicalOperator parent) {
        RInsertSensingDevicesRule.LOG.trace("insertCrossproductAO({})", parent);

        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));

        final ILogicalOperator child = parent.getSubscribedToSource(0).getSource();

        final JoinAO joinAO = new JoinAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            joinAO.addOwner(owner);
        }
        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            // What about the source out port
            joinAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        joinAO.subscribeToSource(child, 0, 0, child.getOutputSchema());
        RInsertSensingDevicesRule.LOG.debug("Insert crossproduct operator: {}", joinAO.toString());
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
    private static ProjectAO insertProjectAO(final ILogicalOperator parent, final List<SDFAttribute> attributes) {
        RInsertSensingDevicesRule.LOG.trace("insertProjectAO({}, {})", parent, attributes);

        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(1));
        Objects.requireNonNull(attributes);

        final ProjectAO projectAO = new ProjectAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            projectAO.addOwner(owner);
        }
        final ILogicalOperator child = parent.getSubscribedToSource(1).getSource();
        final List<SDFAttribute> userAwareAttributes = new ArrayList<>();
        for (final SDFAttribute attr : attributes) {
            userAwareAttributes.add(child.getOutputSchema().findAttribute(attr.getAttributeName()));
        }

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            projectAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        projectAO.subscribeToSource(child, 0, 0, child.getOutputSchema());
        projectAO.setOutputSchemaWithList(userAwareAttributes);
        if (RInsertSensingDevicesRule.LOG.isDebugEnabled()) {
            RInsertSensingDevicesRule.LOG.debug("Insert project operator: {}", projectAO.toString());
            RInsertSensingDevicesRule.LOG.debug("{} -> {}", projectAO.getInputSchema().toString(),
                    projectAO.getOutputSchema().toString());
        }
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
    private static WindowAO insertWindowAO(final ILogicalOperator parent, final double frequency) {
        RInsertSensingDevicesRule.LOG.trace("insertWindowAO({}, {})", parent, frequency);

        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));
        Preconditions.checkArgument(frequency >= 0.0);

        final WindowAO windowAO = new WindowAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            windowAO.addOwner(owner);
        }
        final ILogicalOperator child = parent.getSubscribedToSource(0).getSource();

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            windowAO.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        windowAO.subscribeToSource(child, 0, 0, child.getOutputSchema());

        if (frequency > 0.0) {
            windowAO.setWindowType(WindowType.TIME);
            windowAO.setWindowAdvance(new TimeValueItem(1L, null));
            windowAO.setWindowSize(new TimeValueItem((long) (1.0 / frequency), null));
            windowAO.setBaseTimeUnit(TimeUnit.MILLISECONDS);
        }
        else {
            windowAO.setWindowType(WindowType.TUPLE);
            windowAO.setWindowAdvance(new TimeValueItem(1L, null));
            windowAO.setWindowSize(new TimeValueItem(1L, null));
        }
        RInsertSensingDevicesRule.LOG.debug("Insert window operator: {}", windowAO.toString());
        return windowAO;
    }
}