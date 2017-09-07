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

import java.io.File;
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
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
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
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SensorOntologyServiceImpl
		implements SensorOntologyService, IEventListener, IDataDictionaryListener, ISessionListener {
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SensingDevice> allSensingDevices() {
		return getOntology().getAllSensingDevices();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SensingDevice> allSensingDevices(final String featureOfInterest) {
		Objects.requireNonNull(featureOfInterest);
		throw new IllegalArgumentException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FeatureOfInterest> allFeaturesOfInterests() {
		return getOntology().getAllFeaturesOfInterest();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final SensingDevice sensingDevice) {
		Objects.requireNonNull(sensingDevice);
		getOntology().createSensingDevice(sensingDevice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final FeatureOfInterest featureOfInterest) {
		Objects.requireNonNull(featureOfInterest);
		getOntology().createFeatureOfInterest(featureOfInterest);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final SensingDevice sensingDevice, final MeasurementCapability measurementCapability) {
		Objects.requireNonNull(sensingDevice);
		Objects.requireNonNull(measurementCapability);
		getOntology().createMeasurementCapability(sensingDevice, measurementCapability);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final MeasurementCapability measurementCapability, final Condition condition) {
		Objects.requireNonNull(measurementCapability);
		Objects.requireNonNull(condition);
		getOntology().createCondition(measurementCapability, condition);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final MeasurementCapability measurementCapability,
			final MeasurementProperty measurementProperty) {
		Objects.requireNonNull(measurementCapability);
		Objects.requireNonNull(measurementProperty);
		getOntology().createMeasurementProperty(measurementCapability, measurementProperty);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final FeatureOfInterest featureOfInterest, final Property property) {
		Objects.requireNonNull(featureOfInterest);
		Objects.requireNonNull(property);
		getOntology().createProperty(featureOfInterest, property);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Property> allProperties() {
		return getOntology().getAllProperties();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Property> allProperties(final String featureOfInterest) {
		Objects.requireNonNull(featureOfInterest);
		throw new IllegalArgumentException("Not implemented");
	}

	@Override
	public List<SensingDevice> sensingDevices(final String featureOfInterest, final String sensingDevice,
			final String measurementCapability) {
		Objects.requireNonNull(sensingDevice);
		Objects.requireNonNull(measurementCapability);
		if (featureOfInterest != null) {
			return getOntology().getSensingDevices(featureOfInterest, sensingDevice, measurementCapability);
		}
		return getOntology().getSensingDevices(sensingDevice, measurementCapability);
	}

	@Override
	public List<String> attributes(final Property property) {
		Objects.requireNonNull(property);
		return getOntology().getAttributes(property);
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
		SensorOntologyServiceImpl.session = SessionManagement.instance.loginSuperUser(null);
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
		if (SensorOntologyServiceImpl.ontology == null) {
			SensorOntologyServiceImpl.ontology = new SensorOntology();
		}
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
		} else if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET) {
			((SchedulerManagerEvent) event).getValue().subscribeToAll(this);
		}

		if ((event.getEventType() == SchedulingEventType.SCHEDULING_STARTED)
				|| (event.getEventType() == SchedulingEventType.SCHEDULING_STOPPED)
				|| (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED)
				|| (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET)) {
			try {
				this.createStreamsAndViewsInOntology();
			} catch (final PlanManagementException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearCache() {
		getOntology().clearCache();
	}

	private void prepareExecutor() {
		SensorOntologyServiceImpl.getExecutor().subscribeToAllSchedulerEvents(this);
		this.registerDataDictionaryListener();
	}

	private void prepareUserManagement() {
		SessionManagement.instance.subscribe(this);
		this.registerDataDictionaryListener();
	}

	private void registerDataDictionaryListener() {
		if ((SensorOntologyServiceImpl.getExecutor() != null)
				&& (SensorOntologyServiceImpl.getActiveSession() != null)) {
			SensorOntologyServiceImpl.getExecutor().getDataDictionary(SensorOntologyServiceImpl.getActiveSession())
					.addListener(this);
			this.createStreamsAndViewsInOntology();
		}
	}

	private void createStreamsAndViewsInOntology() {
		final IDataDictionaryWritable dataDictionary = SensorOntologyServiceImpl.getExecutor()
				.getDataDictionary(SensorOntologyServiceImpl.getActiveSession());
		final Set<Entry<Resource, ILogicalPlan>> streamsAndViews = dataDictionary
				.getStreamsAndViews(SensorOntologyServiceImpl.getActiveSession());
		for (final Entry<Resource, ILogicalPlan> streamAndView : streamsAndViews) {
			final String name = streamAndView.getKey().getResourceName();
			final SDFSchema schema = streamAndView.getValue().getOutputSchema();
			this.createSensingDevice(name, schema);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addedViewDefinition(final IDataDictionary sender, final String name, final ILogicalPlan op,
			boolean isView, ISession session) {
		SensorOntologyServiceImpl.LOG.debug("Add view " + name + " to ontology");
		this.createSensingDevice(name, op.getOutputSchema());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removedViewDefinition(final IDataDictionary sender, final String name, final ILogicalPlan op,
			boolean isView, ISession session) {
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
			final MeasurementCapability measurementCapability = new MeasurementCapability(
					URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName()), attribute.getAttributeName(),
					property);
			final Condition condition = new Condition(
					URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName() + "/"
							+ attribute.getAttributeName()),
					property.name(), property, BigDecimal.valueOf(Double.MIN_VALUE).toPlainString() + "< %s < "
							+ BigDecimal.valueOf(Double.MAX_VALUE).toPlainString());
			measurementCapability.add(condition);

			for (final SSNMeasurementProperty ssnMeasurementProperty : SSNMeasurementProperty.values()) {
				final MeasurementProperty measurementProperty = new MeasurementProperty(
						URI.create(ODYSSEUS.NS + name + "/" + attribute.getAttributeName() + "/"
								+ attribute.getAttributeName() + "/" + ssnMeasurementProperty.toString()),
						ssnMeasurementProperty.toString(), ssnMeasurementProperty.resource(), "1.0");
				measurementCapability.add(measurementProperty);
			}

			sensingDevice.add(measurementCapability);
		}
		getOntology().createSensingDevice(sensingDevice);
	}

	@Override
	public void update(Property property) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MeasurementCapability measurementCapability) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MeasurementProperty easurementProperty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(FeatureOfInterest featureOfInterest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SensingDevice sensingDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Property property) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(MeasurementCapability measurementCapability) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(MeasurementProperty easurementProperty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(FeatureOfInterest featureOfInterest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(SensingDevice sensingDevice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Condition condition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(File file) {
		// TODO Auto-generated method stub

	}
}
