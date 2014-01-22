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
package de.uniol.inf.is.odysseus.ontology.storage.service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulingEvent.SchedulingEventType;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionEvent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.ontology.common.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.common.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.common.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.common.model.Property;
import de.uniol.inf.is.odysseus.ontology.common.model.SSNMeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.common.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.common.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.common.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.common.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.storage.vocabulary.ODYSSEUS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensorOntologyServiceImpl implements SensorOntologyService, IEventListener, IDataDictionaryListener, ISessionListener {
    private static final Logger LOG = LoggerFactory.getLogger(SensorOntologyServiceImpl.class);
    private static IServerExecutor executor;
    private static IUserManagement userManagement;
    private static SensorOntology ontology;
    private static ISession session;

    /**
     * Class constructor.
     * 
     */
    public SensorOntologyServiceImpl() {
        SensorOntologyServiceImpl.ontology = new SensorOntology();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getAllSensingDevices() {
        return SensorOntologyServiceImpl.ontology.getAllSensingDevices();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getAllSensingDevices(final String featureOfInterest) {
        Objects.requireNonNull(featureOfInterest);
        throw new IllegalArgumentException("Not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FeatureOfInterest> getAllFeaturesOfInterest() {
        return SensorOntologyServiceImpl.ontology.getAllFeaturesOfInterest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSensingDevice(final SensingDevice sensingDevice) {
        Objects.requireNonNull(sensingDevice);
        SensorOntologyServiceImpl.ontology.createSensingDevice(sensingDevice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createFeatureOfInterest(final FeatureOfInterest featureOfInterest) {
        Objects.requireNonNull(featureOfInterest);
        SensorOntologyServiceImpl.ontology.createFeatureOfInterest(featureOfInterest);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Property> getAllProperties() {
        return SensorOntologyServiceImpl.ontology.getAllProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Property> getAllProperties(final String featureOfInterest) {
        Objects.requireNonNull(featureOfInterest);
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public List<SensingDevice> getSensingDevices(final String featureOfInterest, final String sensingDevice, final String measurementCapability) {
        Objects.requireNonNull(featureOfInterest);
        Objects.requireNonNull(sensingDevice);
        Objects.requireNonNull(measurementCapability);
        return SensorOntologyServiceImpl.ontology.getSensingDevices(featureOfInterest, sensingDevice, measurementCapability);
    }

    @Override
    public List<String> getAttributes(final Property property) {
        Objects.requireNonNull(property);
        return SensorOntologyServiceImpl.ontology.getAttributes(property);
    }

    public void bindExecutor(final IExecutor executor) {
        if (executor instanceof IServerExecutor) {
            SensorOntologyServiceImpl.executor = (IServerExecutor) executor;
            SensorOntologyServiceImpl.LOG.debug("Executor " + executor + " bound");
            this.prepareExecutor();
        }
    }

    public void unbindExecutor(final IExecutor executor) {
        if (SensorOntologyServiceImpl.executor == executor) {
            SensorOntologyServiceImpl.executor = null;
            SensorOntologyServiceImpl.LOG.debug("Executor " + executor + " unbound.");
        }
    }

    public void bindUserManagement(final IUserManagement userManagement) {
        SensorOntologyServiceImpl.userManagement = userManagement;
        SensorOntologyServiceImpl.session = SensorOntologyServiceImpl.getUserManagement().getSessionManagement().loginSuperUser(null);
        SensorOntologyServiceImpl.LOG.debug("UserManagement " + userManagement + " bound");
        this.prepareUserManagement();
    }

    public void unbindUserManagement(final IUserManagement userManagement) {
        SensorOntologyServiceImpl.session = null;
        SensorOntologyServiceImpl.userManagement = null;
    }

    public static IServerExecutor getExecutor() {
        return SensorOntologyServiceImpl.executor;
    }

    public static IUserManagement getUserManagement() {
        return SensorOntologyServiceImpl.userManagement;
    }

    public static SensorOntology getOntology() {
        return SensorOntologyServiceImpl.ontology;
    }

    public static ISession getActiveSession() {
        return SensorOntologyServiceImpl.session;
    }

    @Override
    public void eventOccured(final IEvent<?, ?> event, final long nanoTimestamp) {
        SensorOntologyServiceImpl.LOG.debug(event.toString());
        this.registerDataDictionaryListener();
        if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED) {
            ((SchedulerManagerEvent) event).getValue().unSubscribeFromAll(this);
        }
        else if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET) {
            ((SchedulerManagerEvent) event).getValue().subscribeToAll(this);
        }

        if ((event.getEventType() == SchedulingEventType.SCHEDULING_STARTED) || (event.getEventType() == SchedulingEventType.SCHEDULING_STOPPED)
                || (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED) || (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET)) {
            try {
                this.createStreamsAndViewsInOntology();
            }
            catch (final PlanManagementException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareExecutor() {
        if (SensorOntologyServiceImpl.getExecutor().getSchedulerManager() != null) {
            SensorOntologyServiceImpl.getExecutor().getSchedulerManager().subscribeToAll(this);
            SensorOntologyServiceImpl.getExecutor().getSchedulerManager().getActiveScheduler().subscribeToAll(this);
        }
        this.registerDataDictionaryListener();
    }

    private void prepareUserManagement() {
        SensorOntologyServiceImpl.getUserManagement().getSessionManagement().subscribe(this);
        this.registerDataDictionaryListener();
    }

    private void registerDataDictionaryListener() {
        if ((SensorOntologyServiceImpl.getExecutor() != null) && (SensorOntologyServiceImpl.getActiveSession() != null)) {
            SensorOntologyServiceImpl.getExecutor().getDataDictionary(SensorOntologyServiceImpl.getActiveSession()).addListener(this);
            this.createStreamsAndViewsInOntology();
        }
    }

    private void createStreamsAndViewsInOntology() {
        final IDataDictionaryWritable dataDictionary = SensorOntologyServiceImpl.getExecutor().getDataDictionary(SensorOntologyServiceImpl.getActiveSession());
        final Set<Entry<Resource, ILogicalOperator>> streamsAndViews = dataDictionary.getStreamsAndViews(SensorOntologyServiceImpl.getActiveSession());
        for (final Entry<Resource, ILogicalOperator> streamAndView : streamsAndViews) {
            final String name = streamAndView.getKey().getResourceName();
            final SDFSchema schema = streamAndView.getValue().getOutputSchema();
            this.createSensingDevice(name, schema);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addedViewDefinition(final IDataDictionary sender, final String name, final ILogicalOperator op) {
        SensorOntologyServiceImpl.LOG.debug("Add view " + name + " to ontology");
        this.createSensingDevice(name, op.getOutputSchema());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removedViewDefinition(final IDataDictionary sender, final String name, final ILogicalOperator op) {
        final SDFSchema schema = op.getOutputSchema();
        SensorOntologyServiceImpl.LOG.debug("Remove view: " + name + " " + schema);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dataDictionaryChanged(final IDataDictionary sender) {
        SensorOntologyServiceImpl.LOG.debug("Datadictionary changed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionEventOccured(final ISessionEvent event) {
        SensorOntologyServiceImpl.LOG.debug("SessionEvent occured");
    }

    private void createSensingDevice(final String name, final SDFSchema schema) {
        final SensingDevice sensingDevice = new SensingDevice(URI.create(ODYSSEUS.NS + name), name);
        for (final SDFAttribute attribute : schema.getAttributes()) {
            final Property property = new Property(URI.create(ODYSSEUS.NS + attribute.getAttributeName()));
            final MeasurementCapability measurementCapability = new MeasurementCapability(URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName()), attribute.getAttributeName(), property);
            final Condition condition = new ExpressionCondition(URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName() + "/" + attribute.getAttributeName()), property, BigDecimal.valueOf(
                    Double.MIN_VALUE).toPlainString()
                    + "< %s < " + BigDecimal.valueOf(Double.MAX_VALUE).toPlainString());
            measurementCapability.addCondition(condition);

            for (final SSNMeasurementProperty ssnMeasurementProperty : SSNMeasurementProperty.values()) {
                final MeasurementProperty measurementProperty = new MeasurementProperty(URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName() + "/" + attribute.getAttributeName() + "/"
                        + ssnMeasurementProperty.toString()), ssnMeasurementProperty.getResource(), "1.0");
                measurementCapability.addMeasurementProperty(measurementProperty);
            }

            sensingDevice.addMeasurementCapability(measurementCapability);
        }
        SensorOntologyServiceImpl.ontology.createSensingDevice(sensingDevice);
    }
}
