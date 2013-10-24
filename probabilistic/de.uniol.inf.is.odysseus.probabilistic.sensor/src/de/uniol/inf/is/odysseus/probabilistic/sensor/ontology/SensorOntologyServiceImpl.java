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
import de.uniol.inf.is.odysseus.probabilistic.sensor.SensorOntologyService;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensorOntologyServiceImpl implements SensorOntologyService, IEventListener, IDataDictionaryListener, ISessionListener {
    private static final Logger LOG = LoggerFactory.getLogger(SensorOntologyServiceImpl.class);
    private static IServerExecutor executor;
    private static IUserManagement userManagement;
    private SensorOntology ontology;
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
                registerDataDictionaryListener();
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

    private void registerDataDictionaryListener() {
        if (getActiveSession() != null) {
            IDataDictionaryWritable dataDictionary = getExecutor().getDataDictionary(getActiveSession().getTenant());
            Set<Entry<Resource, ILogicalOperator>> streamsAndViews = dataDictionary.getStreamsAndViews(getActiveSession());
            getUserManagement().getSessionManagement().subscribe(this);
            for (Entry<Resource, ILogicalOperator> streamAndView : streamsAndViews) {
                String uri = streamAndView.getKey().getResourceName();
                SDFSchema schema = streamAndView.getValue().getOutputSchema();
                LOG.debug(streamAndView.getValue().getName());
                for (SDFAttribute attr : schema.getAttributes()) {
                    LOG.debug(attr.getSourceName() + " " + attr.getQualName() + " " + attr.getAttributeName());
                }
                LOG.debug(uri + " " + schema.toString());
            }
            // // FIXME Need to get the registered streams and views!!
            // Collection<Integer> ids = getExecutor().getLogicalQueryIds();
            // Set<ITenant> tenantsToRemove = new
            // HashSet<ITenant>(this.tenants);
            // for (Integer id : ids) {
            //
            // ILogicalQuery query = getExecutor().getLogicalQueryById(id);
            // ITenant tenant = query.getUser().getTenant();
            // if (!this.tenants.contains(tenant)) {
            // LOG.debug("Register listener for " + tenant);
            // getExecutor().getDataDictionary(tenant).addListener(this);
            // this.tenants.add(tenant);
            // }
            // tenantsToRemove.remove(tenant);
            // }
            // for (ITenant tenant : tenantsToRemove) {
            // getExecutor().getDataDictionary(tenant).removeListener(this);
            // }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
        SDFSchema schema = op.getOutputSchema();

        // ontology.createSensingDevice(name, schema);
        LOG.debug("Add view: " + name + " " + schema);
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
        registerDataDictionaryListener();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionEventOccured(ISessionEvent event) {
        registerDataDictionaryListener();

    }
}
