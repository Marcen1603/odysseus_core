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
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingStrategyFactory;

public abstract class AbstractSchedulerManager implements ISchedulerManager {

	protected int schedulerCount = 1;

	protected Logger logger;

	private Map<String, ISchedulerFactory> schedulerFactoryMap = Collections
			.synchronizedMap(new HashMap<String, ISchedulerFactory>());

	private Map<String, ISchedulingStrategyFactory> schedulingStrategyFactoryMap = Collections
			.synchronizedMap(new HashMap<String, ISchedulingStrategyFactory>());

	private List<IErrorEventListener> errorEventListener = Collections
			.synchronizedList(new ArrayList<IErrorEventListener>());

	public AbstractSchedulerManager() {
		this.logger = LoggerFactory.getLogger(AbstractSchedulerManager.class);
		this.logger.trace("Scheduler manager activated.");
	}

	protected void deactivate() {
		synchronized (this.schedulerFactoryMap) {
			schedulerFactoryMap.clear();
			schedulerFactoryMap = null;
		}
	}
	
	public String getInfoString(Object object, String label) {
		return getInfoString(object.toString(), label);
	}

	public String getInfoString(String object, String label) {
		String infos = AppEnv.LINE_SEPERATOR + label + ": ";
		if (object != null) {
			infos += object;
		} else {
			infos += "not set. ";
		}
		return infos;
	}

	protected synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
		}
	}

	public void bindSchedulerFactory(ISchedulerFactory schedulerFactory) {
		String sName = schedulerFactory.getName();
		logger.info("activate "+sName);
		if(this.schedulerFactoryMap.get(sName) == null) {
			this.schedulerFactoryMap.put(sName,schedulerFactory);
		}else{
			logger.warn("Duplicate Scheduling Name "+sName+" !");
		}
	}

	public void unbindSchedulerFactory(
			ISchedulerFactory schedulerFactory) {
		this.schedulerFactoryMap.remove(schedulerFactory.getName());
	}


	public void bindSchedulingStrategyFactory(
			ISchedulingStrategyFactory schedulingStrategyFactory) {
		String stratName = schedulingStrategyFactory.getName();
		logger.info("activate "+stratName);
		if(this.schedulingStrategyFactoryMap.get(stratName) == null) {
			this.schedulingStrategyFactoryMap.put(stratName, schedulingStrategyFactory);
		}else{
			logger.warn("Duplicate Scheduling Strategy Name "+stratName+" !");
		}
	}

	public void unbindSchedulingStrategyFactory(
			ISchedulingStrategyFactory schedulingStrategyFactory) {
		this.schedulingStrategyFactoryMap.remove(schedulingStrategyFactory.getName());
	}

	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		if (!this.errorEventListener.contains(errorEventListener)) {
			this.errorEventListener.add(errorEventListener);
		}
	}

	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		this.errorEventListener.remove(errorEventListener);
	}

	@Override
	public synchronized void sendErrorEvent(ErrorEvent eventArgs) {
		this.logger.error("Error while scheduling.");

		fireErrorEvent(new ErrorEvent(this, ErrorEvent.ERROR,
				"Schedulermanager exception (with inner error). "
						+ eventArgs.getMessage()));
	}

	@Override
	public int getSchedulerCount() {
		return this.schedulerCount;
	}
	
	@Override
	public Set<String> getScheduler() {
		// TODO: Sollte dies eine Kopie sein? Eigentlich nicht, da Strings immutable sind ...
		return this.schedulerFactoryMap.keySet();
	}
	
	@Override
	public Set<String> getSchedulingStrategy() {
		// TODO: Sollte dies eine Kopie sein? Eigentlich nicht, da Strings immutable sind ...
		return this.schedulingStrategyFactoryMap.keySet();
	}
	
	protected IScheduler createScheduler(String scheduler,
			String schedulingStrategy) {
		ISchedulerFactory sf = schedulerFactoryMap.get(scheduler);
		ISchedulingStrategyFactory ssf = schedulingStrategyFactoryMap.get(schedulingStrategy);
		if (sf != null && ssf != null){
			IScheduler s = sf.createScheduler(ssf);
			return s;
		}
		logger.error("No Scheduler created from "+scheduler+" "+schedulingStrategy);
		return null;
	}
	
}
