/**
 * 
 */
package de.uniol.inf.is.odysseus.ontology.rewrite.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.SDFElement;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ontology.Activator;
import de.uniol.inf.is.odysseus.ontology.common.SSN;
import de.uniol.inf.is.odysseus.ontology.common.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.common.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.common.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.common.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.logicaloperator.QualityAO;
import de.uniol.inf.is.odysseus.ontology.utils.SDFUtils;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
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
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getProperties());
        Objects.requireNonNull(config);

        final SDFSchema schema = operator.getInputSchema();
        final List<SDFAttribute> attributes = operator.getAttributes();

        final Map<String, List<SDFAttribute>> toJoinStreams = new HashMap<String, List<SDFAttribute>>();
        final Map<String, Double> frequencies = new HashMap<String, Double>();
        for (final SDFAttribute attribute : attributes) {

            final List<SensingDevice> sensingDevices = Activator.getSensorOntologyService().getSensingDevices(SDFUtils.getFeatureOfInterestLabel(attribute), SDFUtils.getSensingDeviceLabel(attribute),
                    attribute.getAttributeName());
            if (!sensingDevices.isEmpty()) {
                // FIXME What happens if there are more than one sensing
                // device?
                final SensingDevice sensingDevice = sensingDevices.get(0);
                final List<MeasurementCapability> measurementCapabilities = sensingDevice.getHasMeasurementCapabilities(attribute.getAttributeName());
                for (final MeasurementCapability measurementCapability : measurementCapabilities) {
                    final List<Condition> conditions = measurementCapability.getInConditions();
                    final List<MeasurementProperty> measurementProperties = measurementCapability.getHasMeasurementProperties();

                    double frequency = 0.0;
                    for (final MeasurementProperty measurementProperty : measurementProperties) {
                        if ((measurementProperty != null) && (measurementProperty.getResource().equalsIgnoreCase(SSN.NS + "Frequency"))) {
                            frequency = Double.parseDouble(measurementProperty.getExpression());
                        }
                    }
                    for (final Condition condition : conditions) {
                        final List<String> observerAttributeUris = Activator.getSensorOntologyService().getAttributes(condition);
                        final List<SDFAttribute> observerAttributes = new ArrayList<>();
                        for (final String uri : observerAttributeUris) {
                            final String[] split = SDFElement.splitURI(uri);
                            observerAttributes.add(new SDFAttribute(split[0], split[1], SDFDatatype.DOUBLE, null));
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
                RInsertSensingDevicesRule.LOG.warn("No sensing devices observe {}", attribute.getAttributeName());
            }
        }
        this.concatConditionObserverStreams(operator, toJoinStreams, frequencies);

        final MapAO mapAO = this.insertMapAO(operator);

        final Collection<ILogicalOperator> toUpdate = RestructHelper.removeOperator(operator, true);
        for (final ILogicalOperator o : toUpdate) {
            this.update(o);
        }
        update(mapAO);
        mapAO.initialize();
        retract(operator);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(final QualityAO operator, final RewriteConfiguration config) {
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
    private void concatConditionObserverStreams(final ILogicalOperator operator, final Map<String, List<SDFAttribute>> observers, final Map<String, Double> frequencies) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema(0));
        Objects.requireNonNull(observers);
        Objects.requireNonNull(frequencies);

        for (final String sourceName : observers.keySet()) {
            final JoinAO joinAO = this.insertCrossproductAO(operator);
            joinAO.setName("Join " + sourceName);
            final ILogicalOperator viewOrStream = this.insertViewOrStream(joinAO, sourceName);
            final ProjectAO projectAO = this.insertProjectAO(joinAO, observers.get(sourceName));
            // TODO Set frequency
            final WindowAO windowAO = this.insertWindowAO(projectAO, frequencies.get(sourceName));

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
        Objects.requireNonNull(parent);
        Objects.requireNonNull(sourceName);

        final ILogicalOperator viewOrStream = this.getDataDictionary().getViewOrStream(sourceName, this.getCaller());
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
    private MapAO insertMapAO(final QualityAO parent) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));

        final MapAO mapAO = new MapAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            mapAO.addOwner(owner);
        }
        final ILogicalOperator child = parent.getSubscribedToSource(0).getTarget();

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            mapAO.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
        }
        mapAO.subscribeToSource(child, 0, 0, child.getOutputSchema());

        final List<NamedExpressionItem> namedExpressions = new ArrayList<>();
        final IAttributeResolver attrRes = new DirectAttributeResolver(mapAO.getInputSchema());
        for (int i = 0; i < parent.getOutputSchema().size(); i++) {
            SDFExpression expr = new SDFExpression(parent.getExpressions()[i].getMEPExpression(), attrRes, MEP.getInstance());
            namedExpressions.add(new NamedExpressionItem(parent.getOutputSchema().get(i).getURI(), expr));
        }
        mapAO.setExpressions(namedExpressions);
        if (RInsertSensingDevicesRule.LOG.isDebugEnabled()) {
            RInsertSensingDevicesRule.LOG.debug("Insert map operator: {}", mapAO.toString());
            for (int i = 0; i < mapAO.getExpressionList().size(); i++) {
                RInsertSensingDevicesRule.LOG.debug("{}. {} ", i, mapAO.getExpressionList().get(i));
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
    private JoinAO insertCrossproductAO(final ILogicalOperator parent) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));

        final ILogicalOperator child = parent.getSubscribedToSource(0).getTarget();

        final JoinAO joinAO = new JoinAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            joinAO.addOwner(owner);
        }
        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            // What about the source out port
            joinAO.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
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
    private ProjectAO insertProjectAO(final ILogicalOperator parent, final List<SDFAttribute> attributes) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(1));
        Objects.requireNonNull(attributes);

        final ProjectAO projectAO = new ProjectAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            projectAO.addOwner(owner);
        }
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
        if (RInsertSensingDevicesRule.LOG.isDebugEnabled()) {
            RInsertSensingDevicesRule.LOG.debug("Insert project operator: {}", projectAO.toString());
            RInsertSensingDevicesRule.LOG.debug("{} -> {}", projectAO.getInputSchema().toString(), projectAO.getOutputSchema().toString());
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
    private WindowAO insertWindowAO(final ILogicalOperator parent, final double frequency) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(parent.getSubscribedToSource(0));
        Preconditions.checkArgument(frequency >= 0.0);

        final WindowAO windowAO = new WindowAO();
        for (final IOperatorOwner owner : parent.getOwner()) {
            windowAO.addOwner(owner);
        }
        final ILogicalOperator child = parent.getSubscribedToSource(0).getTarget();

        final Collection<LogicalSubscription> subs = child.getSubscriptions();
        for (final LogicalSubscription sub : subs) {
            child.unsubscribeSink(sub);
            windowAO.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
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
