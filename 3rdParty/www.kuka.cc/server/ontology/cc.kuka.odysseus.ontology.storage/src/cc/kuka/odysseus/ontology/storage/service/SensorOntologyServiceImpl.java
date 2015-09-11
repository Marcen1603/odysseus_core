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
package cc.kuka.odysseus.ontology.storage.service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SSNMeasurementProperty;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.storage.vocabulary.ODYSSEUS;
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

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
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
    public void createMeasurementCapability(final SensingDevice sensingDevice, final MeasurementCapability measurementCapability) {
        Objects.requireNonNull(sensingDevice);
        Objects.requireNonNull(measurementCapability);
        SensorOntologyServiceImpl.ontology.createMeasurementCapability(sensingDevice, measurementCapability);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCondition(final MeasurementCapability measurementCapability, final Condition condition) {
        Objects.requireNonNull(measurementCapability);
        Objects.requireNonNull(condition);
        SensorOntologyServiceImpl.ontology.createCondition(measurementCapability, condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMeasurementProperty(final MeasurementCapability measurementCapability, final MeasurementProperty measurementProperty) {
        Objects.requireNonNull(measurementCapability);
        Objects.requireNonNull(measurementProperty);
        SensorOntologyServiceImpl.ontology.createMeasurementProperty(measurementCapability, measurementProperty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createProperty(final FeatureOfInterest featureOfInterest, final Property property) {
        Objects.requireNonNull(featureOfInterest);
        Objects.requireNonNull(property);
        SensorOntologyServiceImpl.ontology.createProperty(featureOfInterest, property);
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
        Objects.requireNonNull(sensingDevice);
        Objects.requireNonNull(measurementCapability);
        if (featureOfInterest != null) {
            return SensorOntologyServiceImpl.ontology.getSensingDevices(featureOfInterest, sensingDevice, measurementCapability);
        }
        return SensorOntologyServiceImpl.ontology.getSensingDevices(sensingDevice, measurementCapability);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCache() {
        SensorOntologyServiceImpl.ontology.clearCache();
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
    public void addedViewDefinition(final IDataDictionary sender, final String name, final ILogicalOperator op, boolean isView, ISession session) {
        SensorOntologyServiceImpl.LOG.debug("Add view " + name + " to ontology");
        this.createSensingDevice(name, op.getOutputSchema());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removedViewDefinition(final IDataDictionary sender, final String name, final ILogicalOperator op, boolean isView, ISession session) {
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
            final Condition condition = new Condition(URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName() + "/" + attribute.getAttributeName()), property.getName(), property,
                    BigDecimal.valueOf(Double.MIN_VALUE).toPlainString() + "< %s < " + BigDecimal.valueOf(Double.MAX_VALUE).toPlainString());
            measurementCapability.addCondition(condition);

            for (final SSNMeasurementProperty ssnMeasurementProperty : SSNMeasurementProperty.values()) {
                final MeasurementProperty measurementProperty = new MeasurementProperty(URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName() + "/" + attribute.getAttributeName() + "/"
                        + ssnMeasurementProperty.toString()), ssnMeasurementProperty.toString(), ssnMeasurementProperty.getResource(), "1.0");
                measurementCapability.addMeasurementProperty(measurementProperty);
            }

            sensingDevice.addMeasurementCapability(measurementCapability);
        }
        SensorOntologyServiceImpl.ontology.createSensingDevice(sensingDevice);
    }
}
