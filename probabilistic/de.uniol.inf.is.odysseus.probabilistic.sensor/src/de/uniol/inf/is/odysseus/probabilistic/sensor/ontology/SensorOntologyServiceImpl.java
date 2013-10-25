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
package de.uniol.inf.is.odysseus.probabilistic.sensor.ontology;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sensor.SensorOntologyService;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.ontology.vocabulary.ODYSSEUS;

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
        ontology = new SensorOntology();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensingDevice> getAllSensingDevices() {
        List<SensingDevice> sensingDevices = ontology.getAllSensingDevices();
        return sensingDevices;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSensingDevice(SensingDevice sensingDevice) {
        ontology.createSensingDevice(sensingDevice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SDFAttribute> getAllProperties() {
        List<SDFAttribute> properties = new ArrayList<SDFAttribute>();
        return properties;
    }

    public void bindExecutor(IExecutor executor) {
        if (executor instanceof IServerExecutor) {
            SensorOntologyServiceImpl.executor = (IServerExecutor) executor;
            LOG.debug("Executor " + executor + " bound");
            prepareExecutor();
        }
        registerDataDictionaryListener();
    }

    public void unbindExecutor(IExecutor executor) {
        if (SensorOntologyServiceImpl.executor == executor) {
            SensorOntologyServiceImpl.executor = null;
            LOG.debug("Executor " + executor + " unbound.");
        }
    }

    public void bindUserManagement(IUserManagement userManagement) {
        SensorOntologyServiceImpl.userManagement = userManagement;
        SensorOntologyServiceImpl.session = getUserManagement().getSessionManagement().loginSuperUser(null);
        LOG.debug("UserManagement " + userManagement + " bound");
        prepareUserManagement();
        registerDataDictionaryListener();
    }

    public void unbindUserManagement(IUserManagement userManagement) {
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
        return ontology;
    }

    public static ISession getActiveSession() {
        return session;
    }

    @Override
    public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
        LOG.debug(event.toString());
        registerDataDictionaryListener();
        if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED) {
            ((SchedulerManagerEvent) event).getValue().unSubscribeFromAll(this);
        }
        else if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET) {
            ((SchedulerManagerEvent) event).getValue().subscribeToAll(this);
        }

        if (event.getEventType() == SchedulingEventType.SCHEDULING_STARTED || event.getEventType() == SchedulingEventType.SCHEDULING_STOPPED
                || event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED || event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET) {
            try {
                createStreamsAndViewsInOntology();
            }
            catch (PlanManagementException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareExecutor() {
        if (getExecutor().getSchedulerManager() != null) {
            getExecutor().getSchedulerManager().subscribeToAll(this);
            getExecutor().getSchedulerManager().getActiveScheduler().subscribeToAll(this);
        }
        registerDataDictionaryListener();
    }

    private void prepareUserManagement() {
        getUserManagement().getSessionManagement().subscribe(this);
    }

    private void registerDataDictionaryListener() {
        if ((getExecutor() != null) && (getActiveSession() != null)) {
            getExecutor().getDataDictionary(getActiveSession().getTenant()).addListener(this);
        }
    }

    private void createStreamsAndViewsInOntology() {
        IDataDictionaryWritable dataDictionary = getExecutor().getDataDictionary(getActiveSession().getTenant());
        Set<Entry<Resource, ILogicalOperator>> streamsAndViews = dataDictionary.getStreamsAndViews(getActiveSession());
        for (Entry<Resource, ILogicalOperator> streamAndView : streamsAndViews) {
            String name = streamAndView.getKey().getResourceName();
            SDFSchema schema = streamAndView.getValue().getOutputSchema();
            createSensingDevice(name, schema);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
        LOG.debug("Add view " + name + " to ontology");
        createSensingDevice(name, op.getOutputSchema());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
        SDFSchema schema = op.getOutputSchema();
        LOG.debug("Remove view: " + name + " " + schema);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dataDictionaryChanged(IDataDictionary sender) {
        LOG.debug("Datadictionary changed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionEventOccured(ISessionEvent event) {
        LOG.debug("SessionEvent occured");
    }

    private void createSensingDevice(String name, SDFSchema schema) {
        SensingDevice sensingDevice = new SensingDevice(URI.create(ODYSSEUS.NS + name), schema);
        for (SDFAttribute attribute : schema.getAttributes()) {
            MeasurementCapability measurementCapability = new MeasurementCapability(URI.create(ODYSSEUS.NS + attribute.getAttributeName()), attribute);
            Condition condition = new Condition(URI.create(ODYSSEUS.NS + attribute.getAttributeName() + "/" + name), attribute, Interval.MAX);
            measurementCapability.addCondition(condition);
            sensingDevice.addMeasurementCapability(measurementCapability);
        }
        ontology.createSensingDevice(sensingDevice);
    }
}
