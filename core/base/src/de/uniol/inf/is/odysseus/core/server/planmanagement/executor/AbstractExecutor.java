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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.connection.NioConnection;
import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionQuerySelector;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitorFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ISettingChangeListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQueryReoptimizeListener;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * AbstractExecutor bietet eine abstrakte Implementierung der
 * Ausf�hrungumgebung. Sie �bernimmt die Aufgabe zum einbinden von
 * OSGi-Services innerhalb des Odysseus-Frameworks.
 * 
 * @author wolf
 * 
 */
public abstract class AbstractExecutor implements IServerExecutor, ISettingChangeListener, IQueryReoptimizeListener, IPlanReoptimizeListener, IAdmissionListener {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractExecutor.class);

	/**
	 * Der aktuell ausgefuehrte physische Plan
	 */
	final protected IExecutionPlan executionPlan = new ExecutionPlan();

	/**
	 * Scheduling-Komponente
	 */
	private ISchedulerManager schedulerManager;

	/**
	 * Optimierungs-Komponente
	 */
	private IOptimizer optimizer;

	/**
	 * Anfragebearbeitungs-Komponente
	 */
	private ICompiler compiler;

	/**
	 * Admission Control-Komponente (optional)
	 */
	private IAdmissionControl admissionControl = null;
	private IAdmissionQuerySelector admissionQuerySelector = null;

	private Map<String, ILogicalQueryDistributor> logicalQueryDistributors = Maps.newHashMap();

	private IPlanAdaptionEngine planAdaptionEngine = null;
	
	/**
	 * Konfiguration der Ausf�hrungsumgebung
	 */
	protected ExecutionConfiguration configuration = new ExecutionConfiguration();

	/**
	 * Nutzer- und Rechteverwaltung
	 */
	protected IUserManagement usrMgmt;
	protected ISessionManagement sessMgmt;

	/**
	 * Data Dictionary
	 */
	private IDataDictionary dataDictionary;

	/**
	 * Standard Configurationen
	 */
	// protected Map<String, List<IQueryBuildSetting<?>>> queryBuildConfigs =
	// new HashMap<String, List<IQueryBuildSetting<?>>>();
	protected Map<String, IQueryBuildConfiguration> queryBuildConfigs = new HashMap<String, IQueryBuildConfiguration>();

	/**
	 * Alle Listener f�r Anfragebearbeitungs-Nachrichten
	 */
	private List<IPlanModificationListener> planModificationListener = Collections.synchronizedList(new ArrayList<IPlanModificationListener>());

	/**
	 * Alle Listener f�r Ausf�hrungs-Nachrichten
	 */
	private List<IPlanExecutionListener> planExecutionListener = Collections.synchronizedList(new ArrayList<IPlanExecutionListener>());

	/**
	 * Alle Listener f�r Fehler-Nachrichten
	 */
	private List<IErrorEventListener> errorEventListener = Collections.synchronizedList(new ArrayList<IErrorEventListener>());

	/**
	 * Compiler Listener
	 */
	private List<ICompilerListener> compilerListener = new CopyOnWriteArrayList<ICompilerListener>();

	/**
	 * Lock for synchronizing execution plan changes.
	 */
	protected ReentrantLock executionPlanLock = new ReentrantLock();

	/**
	 * The default System Monitor.
	 */
	protected ISystemMonitor defaultSystemMonitor = null;

	/**
	 * System Monitor Komponente
	 */
	protected ISystemMonitorFactory systemMonitorFactory = null;

	// --------------------------------------------------------------------------------------
	// Constructors/Initialization
	// --------------------------------------------------------------------------------------

	/**
	 * Standard-Construktor. Initialisiert die Ausf�hrungsumgebung.
	 */
	public AbstractExecutor() {
		LOG.trace("Create Executor.");
		try {
			initialize();
		} catch (ExecutorInitializeException e) {
			LOG.error("Error activate executor. Error: " + e.getMessage());
		}
	}

	/**
	 * initializeIntern Innerhalb dieser Funktion können spezifische
	 * Initialisierungen vorgenommen werden. Dies wird von initialize
	 * aufgerufen. Hier m�ssen ein Plan und Ausf�hrungsplan-Objekt erstellt
	 * werden.
	 * 
	 * @param configuration
	 *            Konfiguration der Ausf�hrungsumgebung.
	 */
	protected abstract void initializeIntern(ExecutionConfiguration configuration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getConfiguration ()
	 */
	@Override
	public ExecutionConfiguration getConfiguration() {
		return configuration;
	}

	private void initialize() throws ExecutorInitializeException {
		LOG.debug("Initializing Executor.");

		initializeIntern(configuration);

		if (this.executionPlan == null) {
			throw new ExecutorInitializeException("Execution plan storage not initialized.");
		}

		this.configuration.addValueChangeListener(this);

		LOG.debug("Initializing Executor done.");
	}

	// ----------------------------------------------------------------------------------------
	// OSGI-Framework spezific
	// ----------------------------------------------------------------------------------------

	/**
	 * bindOptimizer bindet einen Optimierer ein
	 * 
	 * @param optimizer
	 *            neuer Optimierer
	 */
	public void bindOptimizer(IOptimizer optimizer) {
		this.optimizer = optimizer;
		this.optimizer.addErrorEventListener(this);
		LOG.debug("Optimizer bound " + optimizer);
	}

	/**
	 * unbindOptimizer entfernt einen Optimierer
	 * 
	 * @param optimizer
	 *            zu entfernender Optimierer
	 */
	public void unbindOptimizer(IOptimizer optimizer) {
		if (this.optimizer == optimizer) {
			this.optimizer = null;
			LOG.debug("Optimizer unbound " + optimizer);
		}

	}

	/**
	 * bindSchedulerManager bindet einen Scheduling-Manager ein
	 * 
	 * @param schedulerManager
	 *            neuer Scheduling-Manager
	 */
	public void bindSchedulerManager(ISchedulerManager schedulerManager) {
		this.schedulerManager = schedulerManager;
		this.schedulerManager.addErrorEventListener(this);
		if (this.schedulerManager instanceof IPlanModificationListener) {
			this.addPlanModificationListener((IPlanModificationListener) this.schedulerManager);
		}

		LOG.debug("Schedulermanager bound " + schedulerManager);
	}

	/**
	 * unbindSchedulerManager entfernt einen Scheduling-Manager
	 * 
	 * @param schedulerManager
	 *            zu entfernender Scheduling-Manager
	 */
	public void unbindSchedulerManager(ISchedulerManager schedulerManager) {
		if (this.schedulerManager == schedulerManager) {
			this.schedulerManager = null;
			LOG.debug("Schedulermanager unbound " + schedulerManager);
		}
	}

	public void bindAdmissionControl(IAdmissionControl control) {
		if (admissionControl != null){
			admissionControl.removeListener(this);
			admissionControl.unsetExecutor(this);
		}

		admissionControl = control;
		if (admissionControl != null){
			admissionControl.addListener(this);
			admissionControl.setExecutor(this);
		}
	}

	public void unbindAdmissionControl(IAdmissionControl control) {
		if (admissionControl == control) {
			if (admissionControl != null){
				admissionControl.removeListener(this);
				admissionControl.unsetExecutor(this);
			}

			admissionControl = null;
		}
	}

	public void bindAdmissionQuerySelector(IAdmissionQuerySelector selector) {
		admissionQuerySelector = selector;
	}

	public void unbindAdmissionQuerySelector(IAdmissionQuerySelector selector) {
		if (admissionQuerySelector == selector) {
			admissionQuerySelector = null;
		}
	}

	public final void bindLogicalQueryDistributor(ILogicalQueryDistributor d) {

		logicalQueryDistributors.put(d.getName(), d);

		LOG.debug("Logical query distributor bound '{}'", d.getName());
	}

	public final void unbindLogicalQueryDistributor(ILogicalQueryDistributor d) {
		String distributorName = d.getName();
		if (logicalQueryDistributors.containsKey(distributorName)) {
			logicalQueryDistributors.remove(distributorName);
			
			LOG.debug("Logical query distributor unbound '{}'", distributorName);
		}
	}
	
	@Override
	public final ImmutableCollection<String> getLogicalQueryDistributorNames() {
		return ImmutableSet.copyOf(logicalQueryDistributors.keySet());
	}

	@Override
	public final Optional<ILogicalQueryDistributor> getLogicalQueryDistributor(String name) {
		return Optional.fromNullable(logicalQueryDistributors.get(name));
	}



	public void bindPlanAdaption(IPlanAdaptionEngine adaption) {
		this.planAdaptionEngine = adaption;
		this.planAdaptionEngine.setExecutor(this);
	}
	
	public void unbindPlanAdaption(IPlanAdaptionEngine adaption) {
		if(adaption.equals(this.planAdaptionEngine)) {
			this.planAdaptionEngine.unsetExecutor(this);
			this.planAdaptionEngine = null;
		}
	}
	
	/**
	 * bindCompiler bindet eine Anfragebearbeitungs-Komponente ein
	 * 
	 * @param compiler
	 *            neue Anfragebearbeitungs-Komponente
	 */
	public void bindCompiler(ICompiler compiler) {
		this.compiler = compiler;
		for (ICompilerListener l : compilerListener) {
			compiler.addCompilerListener(l);
		}
		LOG.debug("Compiler bound " + compiler);
	}

	/**
	 * unbindCompiler entfernt eine Anfragebearbeitungs-Komponente
	 * 
	 * @param compiler
	 *            zu entfernende Anfragebearbeitungs-Komponente
	 */
	public void unbindCompiler(ICompiler compiler) {
		for (ICompilerListener l : compilerListener) {
			compiler.removeCompilerListener(l);
		}
		if (this.compiler == compiler) {
			this.compiler = null;
			LOG.debug("Compiler unbound " + compiler);
		}
	}

	/**
	 * Binding of predefinded build configurations
	 * 
	 * @param config
	 */
	public void bindQueryBuildConfiguration(IQueryBuildConfiguration config) {
		queryBuildConfigs.put(config.getName(), config);
		LOG.debug("Query Build Configuration " + config + " bound");
	}

	/**
	 * Unbinding of predefinded build configurations
	 * 
	 * @param config
	 */
	public void unbindQueryBuildConfiguration(IQueryBuildConfiguration config) {
		queryBuildConfigs.remove(config.getName());
	}

	protected void bindUserManagement(IUserManagement usermanagement) {
		if (usrMgmt == null) {
			usrMgmt = usermanagement;
		} else {
			throw new RuntimeException("UserManagement already bound!");
		}
	}

	// TODO: Will man das?
	protected void unbindUserManagement(IUserManagement usermanagement) {
		usermanagement = null;
	}

	protected void bindSessionManagement(ISessionManagement sessionmanagement) {
		if (sessMgmt == null) {
			sessMgmt = sessionmanagement;
		} else {
			throw new RuntimeException("SessionManagement already bound!");
		}
	}

	protected void unbindSessionManagement(ISessionManagement sessionmanagement) {
		sessionmanagement = null;
	}

	protected void bindDataDictionary(IDataDictionary datadictionary) {
		if (dataDictionary == null) {
			dataDictionary = datadictionary;
		} else {
			throw new RuntimeException("DataDictionary already bound!");
		}
	}

	protected void unbindDataDictionary(IDataDictionary dd) {
		dataDictionary = null;
	}

	// ----------------------------------------------------------------------------------------
	// Getter/Setter
	// ----------------------------------------------------------------------------------------
	/**
	 * optimizer liefert der aktuelle Optimierer zur�ck. Sollte keiner
	 * vorhanden sein, wird eine Exception geworfen.
	 * 
	 * @return aktueller Optimierer
	 * @throws NoOptimizerLoadedException
	 */
	@Override
	public IOptimizer getOptimizer() throws NoOptimizerLoadedException {
		if (this.optimizer != null) {
			return this.optimizer;
		}

		throw new NoOptimizerLoadedException();
	}

	/**
	 * schedulerManager liefert den aktuellen Scheduling-Manager. Sollte keiner
	 * registriert sein, wird eine Exception geworfen.
	 * 
	 * @return aktueller Scheduling-Manager
	 * @throws SchedulerException
	 */
	@Override
	public ISchedulerManager getSchedulerManager() throws SchedulerException {
		if (this.schedulerManager != null) {
			return this.schedulerManager;
		}

		throw new SchedulerException();
	}

	@Override
	public ICompiler getCompiler() throws NoCompilerLoadedException {
		if (this.compiler != null) {
			return this.compiler;
		}
		throw new NoCompilerLoadedException();
	}

	public final IAdmissionControl getAdmissionControl() {
		return admissionControl;
	}

	public final IAdmissionQuerySelector getAdmissionQuerySelector() {
		return admissionQuerySelector;
	}

	public final boolean hasAdmissionControl() {
		return admissionControl != null;
	}

	public final boolean hasAdmissionQuerySelector() {
		return admissionQuerySelector != null;
	}
	
	public final IPlanAdaptionEngine getPlanAdaptionEngine() {
		return planAdaptionEngine;
	}
	
	public final boolean hasPlanAdaptionEngine() {
		return planAdaptionEngine != null;
	}

	// ----------------------------------------------------------------------------------------
	// Execution Plan
	// ----------------------------------------------------------------------------------------

	/**
	 * aktualisiert das Scheduling.
	 * 
	 * @throws NoSchedulerLoadedException
	 * @throws SchedulerException
	 */
	@Override
	public void executionPlanChanged() throws SchedulerException, NoSchedulerLoadedException {
		LOG.info("Refresh Scheduling");
		getSchedulerManager().refreshScheduling(this.getExecutionPlan());
	}

	// ----------------------------------------------------------------------------------------
	// Events
	// ----------------------------------------------------------------------------------------

	/**
	 * firePlanModificationEvent sendet ein Plan-Bearbeitungs-Event an alle
	 * registrierten Listener.
	 * 
	 * @param eventArgs
	 *            zu sendendes Event
	 */
	protected synchronized void firePlanModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		for (IPlanModificationListener listener : this.planModificationListener) {
			try {
				listener.planModificationEvent(eventArgs);
			} catch (Throwable t) {
				LOG.error("Exception during fireing plan modification event", t);
			}
		}
	}

	/**
	 * firePlanExecutionEvent sendet ein Plan-Scheduling-Event an alle
	 * registrierten Listener.
	 * 
	 * @param eventArgs
	 *            zu sendendes Event
	 */
	protected synchronized void firePlanExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {
		for (IPlanExecutionListener listener : this.planExecutionListener) {
			try {
				listener.planExecutionEvent(eventArgs);
			} catch (Throwable t) {
				LOG.error("Exception during fireing plan execution event", t);
			}
		}
	}

	/**
	 * fireErrorEvent sendet ein Fehler-Event an alle registrierten Listener.
	 * 
	 * @param eventArgs
	 *            zu sendendes Event
	 */
	@Override
	public synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			try {
				listener.errorEventOccured(eventArgs);
			} catch (Throwable t) {
				LOG.error("Exception during firering error event", t);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#
	 * startExecution()
	 */
	@Override
	public void startExecution() throws SchedulerException {
		if (isRunning()) {
			LOG.debug("Scheduler already running.");
			return;
		}
		LOG.info("Start Scheduler.");
		try {
			getSchedulerManager().startScheduling();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		LOG.info("Scheduler started.");

		firePlanExecutionEvent(new PlanExecutionEvent(this, PlanExecutionEventType.EXECUTION_STARTED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#
	 * stopExecution()
	 */
	@Override
	public void stopExecution() throws SchedulerException {
		if (!isRunning()) {
			LOG.debug("Scheduler not running.");
			return;
		}
		LOG.info("Stop Scheduler.");
		try {
			getSchedulerManager().stopScheduling();
			// Stopp only if it has an instance
			if (NioConnection.hasInstance()) {
				NioConnection.getInstance().stopRouting();
			}
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		LOG.info("Scheduler stopped.");

		firePlanExecutionEvent(new PlanExecutionEvent(this, PlanExecutionEventType.EXECUTION_STOPPED));
		// Stop Event Handler
		EventHandler.stopDispatching();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanScheduling
	 * #isRunning ()
	 */
	@Override
	public boolean isRunning() throws SchedulerException {
		try {
			return getSchedulerManager().isRunning();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#
	 * getExecutionPlan()
	 */
	@Override
	public IExecutionPlan getExecutionPlan() {
		return this.executionPlan;
	}

	@Override
	public List<IPhysicalOperator> getPhysicalRoots(int queryID) {
		IPhysicalQuery pq = executionPlan.getQueryById(queryID);
		return pq.getRoots();
	}

	@Override
	@Deprecated
	public ILogicalQuery getLogicalQuery(int id) {
		return getLogicalQueryById(id);
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id) {
		IPhysicalQuery pq = executionPlan.getQueryById(id);
		ILogicalQuery lq = null;
		if (pq != null) {
			lq = pq.getLogicalQuery();
		}
		return lq;
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(String name) {
		IPhysicalQuery pq = executionPlan.getQueryByName(name);
		ILogicalQuery lq = null;
		if (pq != null) {
			lq = pq.getLogicalQuery();
		}
		return lq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.
	 * planmodification
	 * .IPlanModificationHandler#addPlanModificationListener(de.uniol
	 * .inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.
	 * IPlanModificationListener)
	 */
	@Override
	public void addPlanModificationListener(IPlanModificationListener listener) {
		synchronized (this.planModificationListener) {
			if (!this.planModificationListener.contains(listener)) {
				this.planModificationListener.add(listener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.
	 * planmodification
	 * .IPlanModificationHandler#removePlanModificationListener(de
	 * .uniol.inf.is.odysseus
	 * .planmanagement.executor.eventhandling.planmodification
	 * .IPlanModificationListener)
	 */
	@Override
	public void removePlanModificationListener(IPlanModificationListener listener) {
		synchronized (this.planModificationListener) {
			this.planModificationListener.remove(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling
	 * .planexecution
	 * .IPlanExecutionHandler#addPlanExecutionListener(de.uniol.inf
	 * .is.odysseus.core.server. planmanagement
	 * .executor.eventhandling.planexecution.IPlanExecutionListener)
	 */
	@Override
	public void addPlanExecutionListener(IPlanExecutionListener listener) {
		synchronized (this.planExecutionListener) {
			if (!this.planExecutionListener.contains(listener)) {
				this.planExecutionListener.add(listener);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling
	 * .planexecution .
	 * IPlanExecutionHandler#removePlanExecutionListener(de.uniol
	 * .inf.is.odysseus.core.server .
	 * planmanagement.executor.eventhandling.planexecution
	 * .IPlanExecutionListener )
	 */
	@Override
	public void removePlanExecutionListener(IPlanExecutionListener listener) {
		synchronized (this.planExecutionListener) {
			this.planExecutionListener.remove(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getSupportedQueryParser()
	 */
	@Override
	public Set<String> getSupportedQueryParsers() throws NoCompilerLoadedException {
		ICompiler c;
		c = getCompiler();
		return c.getSupportedQueryParser();
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
		synchronized (this.errorEventListener) {
			if (!this.errorEventListener.contains(errorEventListener)) {
				this.errorEventListener.add(errorEventListener);
			}
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
		synchronized (this.errorEventListener) {
			this.errorEventListener.remove(errorEventListener);
		}
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
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR, "Executor exception (with inner error). ", eventArgs.getValue()));
	}

	public void bindSystemMonitorFactory(ISystemMonitorFactory systemMonitorFactory) {
		this.systemMonitorFactory = systemMonitorFactory;
		// initialize default system monitor
		this.defaultSystemMonitor = this.systemMonitorFactory.newSystemMonitor();
		this.defaultSystemMonitor.initialize(30000L);
	}

	public void unbindSystemMonitorFactory(ISystemMonitorFactory systemMonitorFactory) {
		this.systemMonitorFactory = null;
		if (this.defaultSystemMonitor != null) {
			this.defaultSystemMonitor.stop();
			this.defaultSystemMonitor = null;
		}
	}

	@Override
	public void addCompilerListener(ICompilerListener compilerListener) {
		this.compilerListener.add(compilerListener);
		// if Compiler already bound
		if (compiler != null) {
			compiler.addCompilerListener(compilerListener);
		} // else will be done if compiler is bound
	}

	@Override
	public IDataDictionary getDataDictionary() {
		return dataDictionary;
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		if (dataDictionary != null) {
			List<ILogicalQuery> q = dataDictionary.getQueries(caller.getUser(), caller);
			for (ILogicalQuery query : q) {
				try {
					if (query.getQueryText() != null) {
						addQuery(query.getQueryText(), query.getParserId(), caller, dataDictionary.getQueryBuildConfigName(query.getID()));
					} else if (query.getLogicalPlan() != null) {
						addQuery(query.getLogicalPlan(), caller, dataDictionary.getQueryBuildConfigName(query.getID()));
					} else {
						LOG.warn("Query " + query + " cannot be loaded");
					}
				} catch (Throwable t) {
					LOG.error("Could not execute stored query", t);
				}
			}
		}
	}

	// Session specific delegates

	@Override
	public ISession login(String username, byte[] password, String tenant) {
		return sessMgmt.login(username, password, tenant);
	}

	@Override
	public void logout(ISession caller) {
		sessMgmt.logout(caller);
	}

	// Compiler Facade
	@Override
	public List<ILogicalQuery> translateQuery(String queries, String parser, ISession currentUser) {
		return getCompiler().translateQuery(queries, parser, currentUser, getDataDictionary());
	}

	@Override
	public IPhysicalQuery transform(ILogicalQuery query, TransformationConfiguration transformationConfiguration, ISession caller) throws TransformationException {
		return getCompiler().transform(query, transformationConfiguration, caller, dataDictionary);
	}

	// DataDictionary Facade
	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		return getDataDictionary().removeSink(name, caller);
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		getDataDictionary().removeViewOrStream(name, caller);
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller) {
		return getDataDictionary().getStreamsAndViews(caller);
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller) {
		return getDataDictionary().getSinks(caller);
	}
}