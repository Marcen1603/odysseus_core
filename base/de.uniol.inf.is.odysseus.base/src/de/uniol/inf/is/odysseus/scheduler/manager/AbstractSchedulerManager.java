package de.uniol.inf.is.odysseus.scheduler.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.configuration.AppEnv;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

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
	protected Logger logger;

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

	/**
	 * Creates a new manager and initializes the logger. Used by OSGi (no
	 * parameter allowed).
	 */
	public AbstractSchedulerManager() {
		this.logger = LoggerFactory.getLogger(AbstractSchedulerManager.class);
		this.logger.trace("Scheduler manager activated.");
	}

	/**
	 * OSGi-Method: Is called when this object will be deactivted by OSGi.
	 */
	protected void deactivate() {
		synchronized (this.schedulerFactoryMap) {
			schedulerFactoryMap.clear();
			schedulerFactoryMap = null;
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
	protected synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
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
		logger.info("activate " + sName);
		if (this.schedulerFactoryMap.get(sName) == null) {
			this.schedulerFactoryMap.put(sName, schedulerFactory);
		} else {
			logger.warn("Duplicate Scheduling Name " + sName + " !");
		}
	}

	/**
	 * Method to unbind a {@link ISchedulerFactory}. Used by OSGi.
	 * 
	 * @param schedulerFactory
	 *            {@link ISchedulerFactory} service to unbind
	 */
	public void unbindSchedulerFactory(ISchedulerFactory schedulerFactory) {
		if (schedulerFactory!= null && schedulerFactory.getName()!=null){
			this.schedulerFactoryMap.remove(schedulerFactory.getName());
		}else{
			logger.error("Trying to unbound "+schedulerFactory);
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
		logger.info("activate " + stratName);
		if (this.schedulingStrategyFactoryMap.get(stratName) == null) {
			this.schedulingStrategyFactoryMap.put(stratName,
					schedulingStrategyFactory);
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler
	 * #
	 * addErrorEventListener(de.uniol.inf.is.odysseus.base.planmanagement.event.
	 * error.IErrorEventListener)
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
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler
	 * #
	 * removeErrorEventListener(de.uniol.inf.is.odysseus.base.planmanagement.event
	 * .error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener
	 * #sendErrorEvent(de.uniol.inf.is.odysseus.base.planmanagement.event.error.
	 * ErrorEvent)
	 */
	@Override
	public synchronized void sendErrorEvent(ErrorEvent eventArgs) {
		this.logger.error("Error while scheduling.");

		fireErrorEvent(new ErrorEvent(this, ErrorEvent.ERROR,
				"Schedulermanager exception (with inner error). "
						+ eventArgs.getMessage()));
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
	 * de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager#getScheduler
	 * ()
	 */
	@Override
	public Set<String> getScheduler() {
		// TODO: Sollte dies eine Kopie sein? Eigentlich nicht, da Strings
		// immutable sind ...
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
		// TODO: Sollte dies eine Kopie sein? Eigentlich nicht, da Strings
		// immutable sind ...
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
			IScheduler s = sf.createScheduler(ssf);
			return s;
		}
		logger.error("No Scheduler created from " + scheduler + " "
				+ schedulingStrategy);
		return null;
	}

}
