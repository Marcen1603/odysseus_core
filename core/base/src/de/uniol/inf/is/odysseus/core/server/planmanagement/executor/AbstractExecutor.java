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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.connection.NioConnection;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionListener;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionQuerySelector;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitorFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ISettingChangeListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
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
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * AbstractExecutor bietet eine abstrakte Implementierung der Ausf�hrungumgebung. Sie �bernimmt die Aufgabe zum einbinden von OSGi-Services innerhalb des Odysseus-Frameworks.
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

	/**
	 * Mapping (name -> implementation) of all integrated data fragmentation strategies.
	 * 
	 * @author Michael Brand
	 */
	private Map<String, IDataFragmentation> dataFragmentationStrategies = Maps.newHashMap();

	private IPlanAdaptionEngine planAdaptionEngine = null;

	/**
	 * Konfiguration der Ausf�hrungsumgebung
	 */
	protected ExecutionConfiguration configuration = new ExecutionConfiguration();

	/**
	 * Standard Configurationen
	 */
	// protected Map<String, List<IQueryBuildSetting<?>>> queryBuildConfigs =
	// new HashMap<String, List<IQueryBuildSetting<?>>>();
	protected Map<String, IQueryBuildConfigurationTemplate> queryBuildConfigs = new HashMap<String, IQueryBuildConfigurationTemplate>();

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
	 * initializeIntern Innerhalb dieser Funktion können spezifische Initialisierungen vorgenommen werden. Dies wird von initialize aufgerufen. Hier m�ssen ein Plan und Ausf�hrungsplan-Objekt erstellt werden.
	 * 
	 * @param configuration
	 *            Konfiguration der Ausf�hrungsumgebung.
	 */
	protected abstract void initializeIntern(ExecutionConfiguration configuration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor# getConfiguration ()
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

	public void bindUserManagement(IUserManagement mgmt) {
		// do nothing --> use UserManagement instead
	}

	public void unbindUserManagement(IUserManagement mgmt) {
		// do nothing --> use UserManagement instead
	}

	public void bindAdmissionControl(IAdmissionControl control) {
		if (admissionControl != null) {
			admissionControl.removeListener(this);
			admissionControl.unsetExecutor(this);
		}

		admissionControl = control;
		if (admissionControl != null) {
			admissionControl.addListener(this);
			admissionControl.setExecutor(this);
		}
	}

	public void unbindAdmissionControl(IAdmissionControl control) {
		if (admissionControl == control) {
			if (admissionControl != null) {
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

	/**
	 * Binds the referenced {@link IDataFragmentation}. <br />
	 * Called by OSGI-DS.
	 * 
	 * @see #unbindDataFragmentation(IDataFragmentation)
	 * @param dfStrategy
	 *            An instance of an {@link IDataFragmentation} implementation.
	 * @author Michael Brand
	 */
	public final void bindDataFragmentation(IDataFragmentation dfStrategy) {

		dataFragmentationStrategies.put(dfStrategy.getName(), dfStrategy);
		LOG.debug("Data fragmentation strategy bound '{}'", dfStrategy.getName());

	}

	/**
	 * Unbinds an referenced {@link IDataFragmentation}, if <code>dfStrategy</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * 
	 * @see #bindDataFragmentation(IDataFragmentation)
	 * @param dfStrategy
	 *            An instance of an {@link IDataFragmentation} implementation.
	 * @author Michael Brand
	 */
	public final void unbindDataFragmentation(IDataFragmentation dfStrategy) {

		String strategyName = dfStrategy.getName();
		if (dataFragmentationStrategies.containsKey(strategyName)) {

			dataFragmentationStrategies.remove(strategyName);
			LOG.debug("Data fragmentation strategy unbound '{}'", strategyName);

		}

	}

	@Override
	public final ImmutableCollection<String> getDataFragmentationNames() {

		return ImmutableSet.copyOf(dataFragmentationStrategies.keySet());

	}

	@Override
	public final Optional<IDataFragmentation> getDataFragmentation(String name) {

		return Optional.fromNullable(dataFragmentationStrategies.get(name));

	}

	public void bindPlanAdaption(IPlanAdaptionEngine adaption) {
		this.planAdaptionEngine = adaption;
		this.planAdaptionEngine.setExecutor(this);
	}

	public void unbindPlanAdaption(IPlanAdaptionEngine adaption) {
		if (adaption.equals(this.planAdaptionEngine)) {
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
	public void bindQueryBuildConfiguration(IQueryBuildConfigurationTemplate config) {
		queryBuildConfigs.put(config.getName(), config);
		LOG.debug("Query Build Configuration " + config + " bound");
	}

	/**
	 * Unbinding of predefinded build configurations
	 * 
	 * @param config
	 */
	public void unbindQueryBuildConfiguration(IQueryBuildConfigurationTemplate config) {
		queryBuildConfigs.remove(config.getName());
	}

	// ----------------------------------------------------------------------------------------
	// Getter/Setter
	// ----------------------------------------------------------------------------------------
	/**
	 * optimizer liefert der aktuelle Optimierer zur�ck. Sollte keiner vorhanden sein, wird eine Exception geworfen.
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
	 * schedulerManager liefert den aktuellen Scheduling-Manager. Sollte keiner registriert sein, wird eine Exception geworfen.
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
	public void executionPlanChanged(PlanModificationEventType type, Collection<IPhysicalQuery> affectedQueries) throws SchedulerException, NoSchedulerLoadedException {
		if (affectedQueries != null) {
			for (IPhysicalQuery q : affectedQueries) {
				executionPlanChanged(type, q);
			}
		} else {
			executionPlanChanged(type, (IPhysicalQuery) null);
		}
	}

	@Override
	public void executionPlanChanged(PlanModificationEventType type, IPhysicalQuery affectedQuery) throws SchedulerException, NoSchedulerLoadedException {
		switch (type) {
		case PLAN_REOPTIMIZE:
		case QUERY_REOPTIMIZE:
			LOG.info("Refresh Scheduling");
			getSchedulerManager().refreshScheduling(this.getExecutionPlan());
			break;
		case QUERY_ADDED:
			getSchedulerManager().addQuery(affectedQuery);
			break;
		case QUERY_REMOVE:
			getSchedulerManager().removeQuery(affectedQuery);
			break;
		case QUERY_START:
			getSchedulerManager().startedQuery(affectedQuery);
			break;
		case QUERY_STOP:
			getSchedulerManager().stoppedQuery(affectedQuery);
			break;
		}
	}

	// ----------------------------------------------------------------------------------------
	// Events
	// ----------------------------------------------------------------------------------------

	/**
	 * firePlanModificationEvent sendet ein Plan-Bearbeitungs-Event an alle registrierten Listener.
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
	 * firePlanExecutionEvent sendet ein Plan-Scheduling-Event an alle registrierten Listener.
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
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling# startExecution()
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
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling# stopExecution()
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanScheduling #isRunning ()
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
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling# getExecutionPlan()
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
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.eventhandling. planmodification .IPlanModificationHandler#addPlanModificationListener(de.uniol .inf.is.odysseus.planmanagement.executor.eventhandling.planmodification. IPlanModificationListener)
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
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.eventhandling. planmodification .IPlanModificationHandler#removePlanModificationListener(de .uniol.inf.is.odysseus .planmanagement.executor.eventhandling.planmodification .IPlanModificationListener)
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling .planexecution .IPlanExecutionHandler#addPlanExecutionListener(de.uniol.inf .is.odysseus.core.server. planmanagement .executor.eventhandling.planexecution.IPlanExecutionListener)
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
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling .planexecution . IPlanExecutionHandler#removePlanExecutionListener(de.uniol .inf.is.odysseus.core.server . planmanagement.executor.eventhandling.planexecution .IPlanExecutionListener )
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
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor# getSupportedQueryParser()
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
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler # addErrorEventListener(de.uniol.inf.is.odysseus.core.server.planmanagement .event. error.IErrorEventListener)
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
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler # removeErrorEventListener(de.uniol.inf.is.odysseus.core.server.planmanagement .event .error.IErrorEventListener)
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
	 * @see de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener #sendErrorEvent(de.uniol.inf.is.odysseus.core.server.event.error. ErrorEvent)
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
	public IDataDictionaryWritable getDataDictionary(ITenant tenant) {
		return (IDataDictionaryWritable) DataDictionaryProvider.getDataDictionary(tenant);
	}

	@Override
	public void reloadStoredQueries(ISession caller) {
		LOG.debug("Try to load queries from data dictionary");
		if (getDataDictionary(caller.getTenant()) != null) {
			List<ILogicalQuery> q = getDataDictionary(caller.getTenant()).getQueries(caller.getUser(), caller);
			for (ILogicalQuery query : q) {
				try {
					if (query.getQueryText() != null) {
						addQuery(query.getQueryText(), query.getParserId(), caller, getDataDictionary(caller.getTenant()).getQueryBuildConfigName(query.getID()), (Context) null);
					} else if (query.getLogicalPlan() != null) {
						addQuery(query.getLogicalPlan(), caller, getDataDictionary(caller.getTenant()).getQueryBuildConfigName(query.getID()));
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
		ITenant tenantObj = UserManagementProvider.getTenant(tenant);
		return UserManagementProvider.getSessionmanagement().login(username, password, tenantObj);
	}

	@Override
	public void logout(ISession caller) {
		UserManagementProvider.getSessionmanagement().logout(caller);
	}

	// Compiler Facade
	@Override
	public List<IExecutorCommand> translateQuery(String queries, String parser, ISession currentUser, Context context) {
		return getCompiler().translateQuery(queries, parser, currentUser, getDataDictionary(currentUser.getTenant()), context);
	}

	@Override
	public IPhysicalQuery transform(ILogicalQuery query, TransformationConfiguration transformationConfiguration, ISession caller) throws TransformationException {
		return getCompiler().transform(query, transformationConfiguration, caller, getDataDictionary(caller.getTenant()));
	}

	// DataDictionary Facade
	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		return getDataDictionary(caller.getTenant()).removeSink(name, caller);
	}

	@Override
	public ILogicalOperator removeSink(Resource name, ISession caller) {
		return getDataDictionary(caller.getTenant()).removeSink(name, caller);
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		getDataDictionary(caller.getTenant()).removeViewOrStream(name, caller);
	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		getDataDictionary(caller.getTenant()).removeViewOrStream(name, caller);
	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getStreamsAndViews(ISession caller) {
		return getDataDictionary(caller.getTenant()).getStreamsAndViews(caller);
	}

	@Override
	public Set<Entry<Resource, ILogicalOperator>> getSinks(ISession caller) {
		return getDataDictionary(caller.getTenant()).getSinks(caller);
	}
	
	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		return getDataDictionary(caller.getTenant()).containsViewOrStream(name, caller);
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		return getDataDictionary(caller.getTenant()).containsViewOrStream(name, caller);
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getRegisteredDatatypes(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		return getDataDictionary(caller.getTenant()).getDatatypes();
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getRegisteredAggregateFunctions(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<String> getRegisteredAggregateFunctions(@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel, ISession caller) {		
		TreeSet<String> set = new TreeSet<>();
		set.addAll(AggregateFunctionBuilderRegistry.getFunctionNames(datamodel));		
		return set;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getOperatorNames(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<String> getOperatorNames(ISession caller) {
		return new ArrayList<String>(OperatorBuilderFactory.getOperatorBuilderNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getOperatorInformation(java.lang.String, de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public LogicalOperatorInformation getOperatorInformation(String name, ISession caller) {
		IOperatorBuilder builder = OperatorBuilderFactory.createOperatorBuilder(name, caller, getDataDictionary(caller.getTenant()));
		LogicalOperatorInformation loi = new LogicalOperatorInformation();
		loi.setOperatorName(builder.getName());
		loi.setMaxPorts(builder.getMaxInputOperatorCount());
		loi.setMinPorts(builder.getMinInputOperatorCount());
		loi.setDoc(builder.getDoc());
		loi.setCategories(builder.getCategories());

		for (IParameter<?> param : builder.getParameters()) {
			LogicalParameterInformation lpi = new LogicalParameterInformation();
			lpi.setName(param.getName());
			lpi.setMandatory(param.isMandatory());
			lpi.setParameterClass(param.getClass());
			lpi.setDoc(param.getDoc());
			lpi.setPossibleValues(resolvePossibleOperatorParameterValue(builder, param, caller));
			lpi.setPossibleValuesAreDynamic(param.arePossibleValuesDynamic());
			loi.addParameter(lpi);
			if (param instanceof ListParameter<?>) {
				lpi.setList(true);
				ListParameter<?> listParam = (ListParameter<?>) param;
				lpi.setParameterClass(listParam.getSingleParameter().getClass());
			} else {
				lpi.setList(false);
			}
		}
		return loi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#getOperatorInformations(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(ISession caller) {
		List<LogicalOperatorInformation> infos = new ArrayList<>();
		for (String name : getOperatorNames(caller)) {
			infos.add(getOperatorInformation(name, caller));
		}
		return infos;
	}

	private List<String> resolvePossibleOperatorParameterValue(IOperatorBuilder builder, IParameter<?> param, ISession caller) {
		if (param instanceof EnumParameter){
			EnumParameter eParam = (EnumParameter) param;
			@SuppressWarnings("rawtypes")
			Class<? extends Enum> enumClass = eParam.getEnum();
			List<String> ret = new LinkedList<>();
			for (@SuppressWarnings("rawtypes") Enum u:enumClass.getEnumConstants()){
				ret.add(u.toString());
			}
			return ret;
		}
		if (param.getPossibleValueMethod().isEmpty()) {
			return new ArrayList<>();
		}
		// treat special cases
		if (param.getPossibleValueMethod().equalsIgnoreCase("__DD_SOURCES")){
			Set<Entry<Resource, ILogicalOperator>> v = getDataDictionary(caller.getTenant()).getViews(caller);
			Set<Entry<Resource, ILogicalOperator>> s = getDataDictionary(caller.getTenant()).getStreams(caller);
			List<String> ret = new LinkedList<>();
			if (v != null){
				for (Entry<Resource, ILogicalOperator> e:v){
					ret.add(e.getKey().toString());
				}
			}
			if (s != null){
				for (Entry<Resource, ILogicalOperator> e:s){
					ret.add(e.getKey().toString());
				}
			}
			return ret;
		}
		if (param.getPossibleValueMethod().equalsIgnoreCase("__DD_DATATYPES")){
			Set<String> s = getDataDictionary(caller.getTenant()).getDatatypeNames();
			return new LinkedList<>(s);
		}
		if (param.getPossibleValueMethod().equalsIgnoreCase("__DD_SINKS")){
			Set<Entry<Resource, ILogicalOperator>> s = getDataDictionary(caller.getTenant()).getSinks(caller);			
			List<String> ret = new LinkedList<>();
			if (s != null){
				for (Entry<Resource, ILogicalOperator> e: s){
					ret.add(e.getKey().toString());
				}
			}
			return ret;
		}
		if (param.getPossibleValueMethod().equalsIgnoreCase("__JAVA_TIMEUNITS")){
			List<String> ret = new LinkedList<>();
			for (TimeUnit u:TimeUnit.values()){
				ret.add(u.toString());
			}
			return ret;
		}
		try {
			ILogicalOperator op = builder.getOperatorClass().newInstance();			
			Method m = op.getClass().getMethod(param.getPossibleValueMethod());
			@SuppressWarnings("unchecked")
			List<String> values = (List<String>) m.invoke(op);
			return values;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}
	
	@Override
	public Set<String> getRegisteredWrapperNames(ISession caller) {
		return new TreeSet<String>(WrapperRegistry.getWrapperNames());		
	}
}