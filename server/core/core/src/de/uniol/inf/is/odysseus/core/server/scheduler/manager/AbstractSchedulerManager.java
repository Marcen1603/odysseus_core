/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.scheduler.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulerFactory;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.core.server.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;

/**
 * AbstractSchedulerManager is a base implementation for scheduling manager. It
 * manages OSGi-services and error handling for the scheduling module. This base
 * is designed for using one scheduler at the same time.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractSchedulerManager implements ISchedulerManager {

	/**
	 * Count of active scheduler.
	 */
	protected int schedulerCount = 1;

	/**
	 * Logger for informations.
	 */
	private Logger logger;

	/**
	 * Map of all registered {@link ISchedulerFactory}.
	 */
	private Map<String, ISchedulerFactory> schedulerFactoryMap = Collections
			.synchronizedMap(new HashMap<String, ISchedulerFactory>());

	/**
	 * Map of all registered {@link ISchedulingFactory}.
	 */
	private Map<String, ISchedulingFactory> schedulingStrategyFactoryMap = Collections
			.synchronizedMap(new HashMap<String, ISchedulingFactory>());

	/**
	 * List of all objects which are informed if an exception occurs.
	 */
	private List<IErrorEventListener> errorEventListener = Collections
			.synchronizedList(new ArrayList<IErrorEventListener>());

	final EventHandler eventHandler;

	/**
	 * Creates a new manager and initializes the logger. Used by OSGi (no
	 * parameter allowed).
	 */
	public AbstractSchedulerManager() {
		this.eventHandler = EventHandler.getInstance(this);
		this.logger = LoggerFactory.getLogger(AbstractSchedulerManager.class);
		this.logger.trace("Scheduler manager activated.");

	}

	/**
	 * OSGi-Method: Is called when this object will be deactivted by OSGi.
	 */
	protected void deactivate() {
		synchronized (this.schedulerFactoryMap) {
			schedulerFactoryMap.clear();
			// schedulerFactoryMap = null;
		}
	}

	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + class name of object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(Object object, String label) {
		return getInfoString(object.toString(), label);
	}

	/**
	 * Get a formated info string for object. if object not null
	 * 
	 * @param object
	 *            object to describe
	 * @param label
	 *            label for description
	 * @return String: "LINE_SEPERATOR + label + ":" + object" or
	 *         "LINE_SEPERATOR + label + ":" + not set"
	 */
	public String getInfoString(String object, String label) {
		String infos = AppEnv.LINE_SEPARATOR + label + ": ";
		if (object != null) {
			infos += object;
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	/**
	 * Sends an {@link ErrorEvent} to all registered EventListenern
	 * 
	 * @param eventArgs
	 *            {@link ErrorEvent} to send
	 */
	@Override
	public synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.errorEventOccured(eventArgs);
		}
	}

	/**
	 * Method to bind a {@link ISchedulerFactory}. Used by OSGi.
	 * 
	 * @param schedulerFactory
	 *            new {@link ISchedulerFactory} service
	 */
	public void bindSchedulerFactory(ISchedulerFactory schedulerFactory) {
		String sName = schedulerFactory.getName();
		logger.trace("bind Scheduler " + sName);
		if (this.schedulerFactoryMap.get(sName) == null) {
			this.schedulerFactoryMap.put(sName, schedulerFactory);
		} else {
			logger.warn("Duplicate Scheduling Name " + sName + " !");
		}
		schedulingsChanged();
	}

	/**
	 * Method to unbind a {@link ISchedulerFactory}. Used by OSGi.
	 * 
	 * @param schedulerFactory
	 *            {@link ISchedulerFactory} service to unbind
	 */
	public void unbindSchedulerFactory(ISchedulerFactory schedulerFactory) {
		if (schedulerFactory != null && schedulerFactory.getName() != null) {
			this.schedulerFactoryMap.remove(schedulerFactory.getName());
		} else {
			logger.error("Trying to unbound Scheduler " + schedulerFactory);
		}
	}

	/**
	 * Method to bind a {@link ISchedulingFactory}. Used by OSGi.
	 * 
	 * @param schedulingStrategyFactory
	 *            new {@link ISchedulingFactory} service
	 */
	public void bindSchedulingStrategyFactory(
			ISchedulingFactory schedulingStrategyFactory) {
		String stratName = schedulingStrategyFactory.getName();
		logger.trace("activate Strategy " + stratName);
		if (this.schedulingStrategyFactoryMap.get(stratName) == null) {
			this.schedulingStrategyFactoryMap.put(stratName,
					schedulingStrategyFactory);

			fire(new SchedulerManagerEvent(this,
					SchedulerManagerEventType.SCHEDULING_STRATEGY_ADDED, null));
			// For internal processing
			schedulingsChanged();
		} else {
			logger.warn("Duplicate Scheduling Strategy Name " + stratName
					+ " !");
		}
	}

	/**
	 * Method to unbind a {@link ISchedulingFactory}. Used by OSGi.
	 * 
	 * @param schedulingStrategyFactory
	 *            {@link ISchedulingFactory} service to unbind
	 */
	public void unbindSchedulingStrategyFactory(
			ISchedulingFactory schedulingStrategyFactory) {
		this.schedulingStrategyFactoryMap.remove(schedulingStrategyFactory
				.getName());
		fire(new SchedulerManagerEvent(this,
				SchedulerManagerEventType.SCHEDULING_STRATEGY_REMOVED, null));
		// For internal processing
		schedulingsChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler
	 * #
	 * addErrorEventListener(de.uniol.inf.is.odysseus.core.server.planmanagement
	 * .event. error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		if (!this.errorEventListener.contains(errorEventListener)) {
			this.errorEventListener.add(errorEventListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler
	 * #
	 * removeErrorEventListener(de.uniol.inf.is.odysseus.core.server.planmanagement
	 * .event .error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener
	 * #sendErrorEvent(de.uniol.inf.is.odysseus.core.server.event.error.
	 * ErrorEvent)
	 */
	@Override
	public synchronized void errorEventOccured(ErrorEvent eventArgs) {
		this.logger.error("Error while scheduling. " + eventArgs);

		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR,
				"Schedulermanager exception (with inner error). ",
				eventArgs.getValue()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * getSchedulerCount()
	 */
	@Override
	public int getSchedulerCount() {
		return this.schedulerCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager
	 * #getScheduler ()
	 */
	@Override
	public Set<String> getScheduler() {
		return this.schedulerFactoryMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#
	 * getSchedulingStrategy()
	 */
	@Override
	public Set<String> getSchedulingStrategy() {
		return this.schedulingStrategyFactoryMap.keySet();
	}

	/**
	 * Creates a scheduler with the given parameters if they are valid.
	 * 
	 * @param scheduler
	 *            Name of the scheduler which should be created.
	 * @param schedulingStrategy
	 *            Name of the scheduling strategy with which the new scheduler
	 *            should be initialized.
	 * @return A new scheduler instance.
	 */
	protected IScheduler createScheduler(String scheduler,
			String schedulingStrategy) {
		// get the factories
		ISchedulerFactory sf = schedulerFactoryMap.get(scheduler);
		ISchedulingFactory ssf = schedulingStrategyFactoryMap
				.get(schedulingStrategy);
		if (sf != null && ssf != null) {
			// create the new scheduler
			int executorThreadsCount = (int) OdysseusConfiguration.getLong(
					"scheduler_simpleThreadScheduler_executorThreadsCount", -1);
			if (executorThreadsCount <= 0) {
				executorThreadsCount = Runtime.getRuntime()
						.availableProcessors();
			}
			IScheduler s = sf.createScheduler(ssf, executorThreadsCount);
			return s;
		}
		logger.error("No Scheduler created from " + scheduler + " "
				+ schedulingStrategy);
		return null;
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(this, listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(this, listener, type);
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(this, listener);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(this, listener);
	}

	@Override
	public final void fire(IEvent<?, ?> event) {
		eventHandler.fire(this, event);
	}

}
