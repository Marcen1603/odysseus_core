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
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.connection.NioConnection;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.IQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.core.server.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
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
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan.ExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IQueryReoptimizeListener;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionEvent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * AbstractExecutor bietet eine abstrakte Implementierung der
 * Ausf�hrungumgebung. Sie �bernimmt die Aufgabe zum einbinden von
 * OSGi-Services innerhalb des Odysseus-Frameworks.
 * 
 * @author wolf
 * 
 */
public abstract class AbstractExecutor implements IServerExecutor,
		ISettingChangeListener, IQueryReoptimizeListener,
		IPlanReoptimizeListener, IDataDictionaryListener, ISessionListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractExecutor.class);

	// Generic Event Handling
	final Map<String, List<IUpdateEventListener>> updateEventListener = new HashMap<>();
	final Map<IDataDictionary, List<IUpdateEventListener>> dataDictEventListener = new HashMap<>();

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

	private static Map<String, Class<? extends IPreTransformationHandler>> preTransformationHandlerMap = Maps
			.newHashMap();
	private IQueryDistributor queryDistributor;

	/**
	 * Mapping (name -> implementation) of all integrated data fragmentation
	 * strategies.
	 * 
	 * @author Michael Brand
	 */
	private Map<String, IDataFragmentation> dataFragmentationStrategies = Maps
			.newHashMap();

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
	private List<IPlanModificationListener> planModificationListener = Collections
			.synchronizedList(new ArrayList<IPlanModificationListener>());
	
	/**
	 * All Listener for query added events.
	 */
	private List<IQueryAddedListener> queryAddedListener = Collections
			.synchronizedList(new ArrayList<IQueryAddedListener>());

	/**
	 * Alle Listener f�r Ausf�hrungs-Nachrichten
	 */
	private List<IPlanExecutionListener> planExecutionListener = Collections
			.synchronizedList(new ArrayList<IPlanExecutionListener>());

	/**
	 * Alle Listener f�r Fehler-Nachrichten
	 */
	private List<IErrorEventListener> errorEventListener = Collections
			.synchronizedList(new ArrayList<IErrorEventListener>());

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
	protected abstract void initializeIntern(
			ExecutionConfiguration configuration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getConfiguration ()
	 */
	@Override
	public ExecutionConfiguration getConfiguration(ISession session) {
		return configuration;
	}

	private void initialize() throws ExecutorInitializeException {
		LOG.debug("Initializing Executor.");

		initializeIntern(configuration);

		if (this.executionPlan == null) {
			throw new ExecutorInitializeException(
					"Execution plan storage not initialized.");
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
		LOG.trace("Optimizer bound " + optimizer);
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
			LOG.trace("Optimizer unbound " + optimizer);
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
		try {
			schedulerManager.startScheduling();
		} catch (OpenFailedException | NoSchedulerLoadedException e) {
			e.printStackTrace();
		}

		LOG.trace("Schedulermanager bound " + schedulerManager);
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
			LOG.trace("Schedulermanager unbound " + schedulerManager);
		}
	}

	public void bindUserManagement(IUserManagement mgmt) {
		// do nothing --> use UserManagement instead
	}

	public void unbindUserManagement(IUserManagement mgmt) {
		// do nothing --> use UserManagement instead
	}

	public final void bindQueryDistributor(IQueryDistributor distributor) {
		queryDistributor = distributor;
	}

	public final void unbindQueryDistributor(IQueryDistributor distributor) {
		if (queryDistributor == distributor) {
			distributor = null;
		}
	}

	// called by OSGi-DS
	public static void bindPreTransformationHandler(
			IPreTransformationHandler serv) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(serv.getName()),
				"preTransformationHandler's name must not be null or empty!");
		Preconditions.checkArgument(!preTransformationHandlerMap
				.containsKey(serv.getName().toUpperCase()),
				"There is already a preTransformationHandler called '%s'", serv
						.getName().toUpperCase());
		Preconditions
				.checkArgument(
						canCreateInstance(serv.getClass()),
						"Could not create instance of IPreTransformationHandler-class '%s'",
						serv.getClass());

		LOG.trace("Bound preTransformationHandler called '{}': {}", serv
				.getName().toUpperCase(), serv.getClass());
		preTransformationHandlerMap.put(serv.getName().toUpperCase(),
				serv.getClass());
	}

	// called by OSGi-DS
	public static void unbindPreTransformationHandler(
			IPreTransformationHandler serv) {
		if (preTransformationHandlerMap.containsKey(serv.getName()
				.toUpperCase())) {
			preTransformationHandlerMap.remove(serv.getName().toUpperCase());

			LOG.trace("Unbound preTransformationHandler called '{}' : {}", serv
					.getName().toUpperCase(), serv.getClass());
		}
	}

	private static boolean canCreateInstance(
			Class<? extends IPreTransformationHandler> handler) {
		try {
			handler.newInstance();
			return true;
		} catch (InstantiationException | IllegalAccessException e) {
			return false;
		}
	}

	public IPreTransformationHandler getPreTransformationHandler(String name)
			throws InstantiationException, IllegalAccessException {
		Preconditions.checkNotNull(Strings.isNullOrEmpty(name),
				"Name of PreTransformationHandler must not be null or empty!");

		Class<? extends IPreTransformationHandler> clazz = preTransformationHandlerMap
				.get(name.toUpperCase());
		if (clazz != null) {
			return clazz.newInstance();
		}

		throw new InstantiationException("IPreTransformationHandler '"
				+ name.toUpperCase() + "' is not known!");
	}

	@Override
	public boolean hasPreTransformationHandler(String name) {
		return preTransformationHandlerMap.containsKey(name.toUpperCase());
	}

	@Override
	public Collection<String> getPreTransformationHandlerNames() {
		return Lists.newArrayList(preTransformationHandlerMap.keySet());
	}

	public final boolean hasQueryDistributor() {
		return queryDistributor != null;
	}

	public final IQueryDistributor getQueryDistributor() {
		return queryDistributor;
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
		LOG.trace("Data fragmentation strategy bound '{}'",
				dfStrategy.getName());

	}

	/**
	 * Unbinds an referenced {@link IDataFragmentation}, if
	 * <code>dfStrategy</code> is the binded one. <br />
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
			LOG.trace("Data fragmentation strategy unbound '{}'", strategyName);

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
		LOG.trace("Compiler bound " + compiler);
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
			LOG.trace("Compiler unbound " + compiler);
		}
	}

	/**
	 * Binding of predefinded build configurations
	 * 
	 * @param config
	 */
	public void bindQueryBuildConfiguration(
			IQueryBuildConfigurationTemplate config) {
		queryBuildConfigs.put(config.getName(), config);
		LOG.trace("Query Build Configuration " + config + " bound");
	}

	/**
	 * Unbinding of predefinded build configurations
	 * 
	 * @param config
	 */
	public void unbindQueryBuildConfiguration(
			IQueryBuildConfigurationTemplate config) {
		queryBuildConfigs.remove(config.getName());
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
		// TODO:
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
	public ISchedulerManager getSchedulerManager(ISession session)
			throws SchedulerException {
		// TODO: Check access rights
		return getSchedulerManager();
	}

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
	public void executionPlanChanged(PlanModificationEventType type,
			Collection<IPhysicalQuery> affectedQueries)
			throws SchedulerException, NoSchedulerLoadedException {
		if (affectedQueries != null) {
			for (IPhysicalQuery q : affectedQueries) {
				executionPlanChanged(type, q);
			}
		} else {
			executionPlanChanged(type, (IPhysicalQuery) null);
		}
	}

	@Override
	public void executionPlanChanged(PlanModificationEventType type,
			IPhysicalQuery affectedQuery) throws SchedulerException,
			NoSchedulerLoadedException {
		switch (type) {
		case PLAN_REOPTIMIZE:
		case QUERY_REOPTIMIZE:
			LOG.info("Refresh Scheduling");
			getSchedulerManager().refreshScheduling(this.getExecutionPlan());
			fireGenericEvent(IUpdateEventListener.SCHEDULING);
			break;
		case QUERY_ADDED:
			getSchedulerManager().addQuery(affectedQuery);
			fireGenericEvent(IUpdateEventListener.QUERY);
			break;
		case QUERY_REMOVE:
			getSchedulerManager().removeQuery(affectedQuery);
			fireGenericEvent(IUpdateEventListener.QUERY);
			break;
		case QUERY_START:
			getSchedulerManager().startedQuery(affectedQuery);
			fireGenericEvent(IUpdateEventListener.QUERY);
			break;
		case QUERY_STOP:
			getSchedulerManager().stoppedQuery(affectedQuery);
			fireGenericEvent(IUpdateEventListener.QUERY);
			break;
		case QUERY_SUSPEND:
		case QUERY_RESUME:
		case QUERY_PARTIAL:
			// Nothing to do for scheduler
			break;
		}
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
	protected synchronized void firePlanModificationEvent(
			AbstractPlanModificationEvent<?> eventArgs) {
		for (IPlanModificationListener listener : this.planModificationListener) {
			try {
				listener.planModificationEvent(eventArgs);
			} catch (Throwable t) {
				LOG.error("Exception during fireing plan modification event", t);
			}
		}
	}
	
	/**
	 * Sends an added query to all listeners.
	 */
	protected synchronized void fireQueryAddedEvent(
			String query, List<Integer> queryIds, String buildConfig,
			String parserID, ISession user) {
		for (IQueryAddedListener listener : this.queryAddedListener) {
			try {
				listener.queryAddedEvent(query, queryIds, buildConfig, parserID, user);
			} catch (Throwable t) {
				LOG.error("Exception during fireing query added event", t);
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
	protected synchronized void firePlanExecutionEvent(
			AbstractPlanExecutionEvent<?> eventArgs) {
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
			LOG.trace("Scheduler already running.");
			return;
		}
		LOG.trace("Start Scheduler.");
		try {
			getSchedulerManager().startScheduling();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		LOG.trace("Scheduler started.");

		fireGenericEvent(IUpdateEventListener.SCHEDULING);

		firePlanExecutionEvent(new PlanExecutionEvent(this,
				PlanExecutionEventType.EXECUTION_STARTED));
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
			LOG.trace("Scheduler not running.");
			return;
		}
		LOG.trace("Stop Scheduler.");
		try {
			getSchedulerManager().stopScheduling();
			// Stopp only if it has an instance
			if (NioConnection.hasInstance()) {
				NioConnection.getInstance().stopRouting();
			}
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		LOG.trace("Scheduler stopped.");

		firePlanExecutionEvent(new PlanExecutionEvent(this,
				PlanExecutionEventType.EXECUTION_STOPPED));
		// Stop Event Handler
		EventHandler.stopDispatching();
		fireGenericEvent(IUpdateEventListener.SCHEDULING);
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
	public List<IPhysicalOperator> getPhysicalRoots(int queryID,
			ISession session) {
		// TODO: Check access rights
		IPhysicalQuery pq = executionPlan.getQueryById(queryID);
		if (pq != null) {
			return pq.getRoots();
		} else {
			return null;
		}
	}

	@Override
	public ILogicalQuery getLogicalQueryById(int id, ISession session) {
		// TODO: Check access rights
		IPhysicalQuery pq = executionPlan.getQueryById(id);
		if (pq.getSession().getUser() == session.getUser()) {
			ILogicalQuery lq = null;
			if (pq != null) {
				lq = pq.getLogicalQuery();
				lq.setParameter("STATE", pq.getState());
			}
			return lq;
		}

		return null;
	}

	@Override
	public ILogicalQuery getLogicalQueryByName(String name, ISession session) {
		// TODO: Check access rights
		IPhysicalQuery pq = executionPlan.getQueryByName(name);
		ILogicalQuery lq = null;
		if (pq != null) {
			lq = pq.getLogicalQuery();
		}
		return lq;
	}

	@Override
	public QueryState getQueryState(int queryID, ISession session) {
		// For local processing, currently no session is needed
		return getQueryState(queryID);
	}

	@Override
	public QueryState getQueryState(int queryID) {
		IPhysicalQuery pq = executionPlan.getQueryById(queryID);
		return getQueryState(pq);
	}

	@Override
	public QueryState getQueryState(String queryName, ISession session) {
		// For local processing, currently no session is needed
		return getQueryState(queryName);
	}

	@Override
	public QueryState getQueryState(String queryID) {
		IPhysicalQuery pq = executionPlan.getQueryByName(queryID);
		return getQueryState(pq);
	}

	private QueryState getQueryState(IPhysicalQuery pq) {
		if (pq != null) {
			return pq.getState();
		} else {
			return QueryState.UNDEF;
		}
	}

	@Override
	public List<QueryState> getQueryStates(List<Integer> id,
			List<ISession> session) {
		// For local processing, currently no session is needed
		return getQueryStates(id);
	}

	@Override
	public List<QueryState> getQueryStates(List<Integer> id) {
		List<QueryState> ret = new ArrayList<QueryState>();
		for (Integer qid : id) {
			ret.add(getQueryState(qid));
		}
		return ret;
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
	public void removePlanModificationListener(
			IPlanModificationListener listener) {
		synchronized (this.planModificationListener) {
			this.planModificationListener.remove(listener);
		}
	}
	
	@Override
	public void addQueryAddedListener(IQueryAddedListener listener) {
		synchronized (this.queryAddedListener) {
			if (!this.queryAddedListener.contains(listener)) {
				this.queryAddedListener.add(listener);
			}
		}
	}

	@Override
	public void removeQueryAddedListener(
			IQueryAddedListener listener) {
		synchronized (this.queryAddedListener) {
			this.queryAddedListener.remove(listener);
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

	// ----------------------------------------------------------------------------------------------------------
	// GENERIC UPDATE EVENTS
	// ----------------------------------------------------------------------------------------------------------

	@Override
	public void addUpdateEventListener(IUpdateEventListener listener,
			String type, ISession session) {
		synchronized (updateEventListener) {
			// Remember Listener (must be different for dd and other, because
			// each
			// session could have another dd)
			List<IUpdateEventListener> l;
			if (type != IUpdateEventListener.DATADICTIONARY) {
				l = updateEventListener.get(type);
			} else {
				IDataDictionary dd = getDataDictionary(session);
				l = dataDictEventListener.get(dd);
			}
			if (l == null) {
				l = new CopyOnWriteArrayList<>();
				if (type != IUpdateEventListener.DATADICTIONARY) {
					this.updateEventListener.put(type, l);
				} else {
					IDataDictionary dd = getDataDictionary(session);
					dataDictEventListener.put(dd, l);
				}
			}
			l.add(listener);

			// Register for remote events (if first listener)
			if (l.size() == 1) {
				switch (type) {
				case IUpdateEventListener.DATADICTIONARY:
					getDataDictionary(session).addListener(this);
					break;
				case IUpdateEventListener.QUERY:
				case IUpdateEventListener.SCHEDULING:
					// Nothing to do. Is already listener
					break;
				case IUpdateEventListener.SESSION:
					UserManagementProvider.getSessionmanagement().subscribe(
							this);
					break;
				case IUpdateEventListener.USER:
					break;
				}
			}
		}
	}

	@Override
	public void removeUpdateEventListener(IUpdateEventListener listener,
			String type, ISession session) {
		synchronized (updateEventListener) {
			List<IUpdateEventListener> l;
			if (type != IUpdateEventListener.DATADICTIONARY) {
				l = updateEventListener.get(type);
			} else {
				IDataDictionary dd = getDataDictionary(session);
				l = dataDictEventListener.get(dd);
			}
			if (l != null) {
				l.remove(listener);
				if (l.isEmpty()) {
					if (type != IUpdateEventListener.DATADICTIONARY) {
						updateEventListener.remove(l);
					} else {
						dataDictEventListener.remove(l);
					}
				}
			}

			// Deregister from remote events
			if (l != null && l.isEmpty()) {
				switch (type) {
				case IUpdateEventListener.DATADICTIONARY:
					getDataDictionary(session).removeListener(this);
					break;
				case IUpdateEventListener.QUERY:
				case IUpdateEventListener.SCHEDULING:
					// Nothing to do. Stays listener
					break;
				case IUpdateEventListener.SESSION:
					UserManagementProvider.getSessionmanagement().unsubscribe(
							this);
					break;
				case IUpdateEventListener.USER:
					break;
				}
			}
		}
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name,
			ILogicalOperator op) {
		fireDataDictionaryEvent(sender);
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name,
			ILogicalOperator op) {
		fireDataDictionaryEvent(sender);
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		fireDataDictionaryEvent(sender);
	}

	@Override
	public void sessionEventOccured(ISessionEvent event) {
		fireGenericEvent(IUpdateEventListener.SESSION);
	}

	public void fireDataDictionaryEvent(IDataDictionary dd) {
		synchronized (updateEventListener) {
			List<IUpdateEventListener> list = dataDictEventListener.get(dd);
			// LOG.trace("Fire to " + list);
			if (list != null) {
				for (IUpdateEventListener l : list) {
					l.eventOccured(IUpdateEventListener.DATADICTIONARY);
				}
			}
		}
	}

	public void fireGenericEvent(String type) {
		synchronized (updateEventListener) {
			List<IUpdateEventListener> list = updateEventListener.get(type);
			// LOG.trace("Fire " + type + " to " + list);
			if (list != null) {
				for (IUpdateEventListener l : list) {
					l.eventOccured(type);
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getSupportedQueryParser()
	 */
	@Override
	public Set<String> getSupportedQueryParsers(ISession session)
			throws NoCompilerLoadedException {
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
		fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR,
				"Executor exception (with inner error). ", eventArgs.getValue()));
	}

	public void bindSystemMonitorFactory(
			ISystemMonitorFactory systemMonitorFactory) {
		this.systemMonitorFactory = systemMonitorFactory;
		// initialize default system monitor
		this.defaultSystemMonitor = this.systemMonitorFactory
				.newSystemMonitor();
		this.defaultSystemMonitor.initialize(30000L);
	}

	public void unbindSystemMonitorFactory(
			ISystemMonitorFactory systemMonitorFactory) {
		this.systemMonitorFactory = null;
		if (this.defaultSystemMonitor != null) {
			this.defaultSystemMonitor.stop();
			this.defaultSystemMonitor = null;
		}
	}

	@Override
	public void addCompilerListener(ICompilerListener compilerListener,
			ISession session) {
		// TODO: Check right
		this.compilerListener.add(compilerListener);
		// if Compiler already bound
		if (compiler != null) {
			compiler.addCompilerListener(compilerListener);
		} // else will be done if compiler is bound
	}

	@Override
	public IDataDictionaryWritable getDataDictionary(ITenant tenant) {
		return (IDataDictionaryWritable) DataDictionaryProvider
				.getDataDictionary(tenant);
	}

	@Override
	public IDataDictionaryWritable getDataDictionary(ISession session) {
		return (IDataDictionaryWritable) DataDictionaryProvider
				.getDataDictionary(session.getTenant());
	}

	@Override
	public void reloadStoredQueries(ISession caller) {

		// LOG.debug("Try to load queries from data dictionary");
		// if (getDataDictionary(caller) != null) {
		// List<ILogicalQuery> q =
		// getDataDictionary(caller).getQueries(caller.getUser(), caller);
		// for (ILogicalQuery query : q) {
		// try {
		// if (query.getQueryText() != null) {
		// addQuery(query.getQueryText(), query.getParserId(), caller,
		// getDataDictionary(caller).getQueryBuildConfigName(query.getID()),
		// (Context) null);
		// } else if (query.getLogicalPlan() != null) {
		// addQuery(query.getLogicalPlan(), caller,
		// getDataDictionary(caller).getQueryBuildConfigName(query.getID()));
		// } else {
		// LOG.warn("Query " + query + " cannot be loaded");
		// }
		// } catch (Throwable t) {
		// LOG.error("Could not execute stored query", t);
		// }
		// }
		// }
	}

	// Session specific delegates

	@Override
	public ISession login(String username, byte[] password, String tenant) {
		ITenant tenantObj = UserManagementProvider.getTenant(tenant);
		return UserManagementProvider.getSessionmanagement().login(username,
				password, tenantObj);
	}

	@Override
	public ISession login(String username, byte[] password) {
		ITenant tenantObj = UserManagementProvider.getDefaultTenant();
		return UserManagementProvider.getSessionmanagement().login(username,
				password, tenantObj);
	}

	@Override
	public void logout(ISession caller) {
		UserManagementProvider.getSessionmanagement().logout(caller);
	}

	@Override
	public boolean isValid(ISession session) {
		return UserManagementProvider.getSessionmanagement().isValid(session);
	}

	// Compiler Facade
	@Override
	public List<IExecutorCommand> translateQuery(String queries, String parser,
			ISession currentUser, Context context, IMetaAttribute metaAttribute) {
		return getCompiler().translateQuery(queries, parser, currentUser,
				getDataDictionary(currentUser), context, metaAttribute, this);
	}

	@Override
	public IPhysicalQuery transform(ILogicalQuery query,
			TransformationConfiguration transformationConfiguration,
			ISession caller) throws TransformationException {
		return getCompiler().transform(query, transformationConfiguration,
				caller, getDataDictionary(caller));
	}

	// DataDictionary Facade
	@Override
	public ILogicalOperator removeSink(String name, ISession caller) {
		return getDataDictionary(caller).removeSink(name, caller);
	}

	@Override
	public ILogicalOperator removeSink(Resource name, ISession caller) {
		return getDataDictionary(caller).removeSink(name, caller);
	}

	@Override
	public void removeViewOrStream(String name, ISession caller) {
		getDataDictionary(caller).removeViewOrStream(name, caller);
	}

	@Override
	public void removeViewOrStream(Resource name, ISession caller) {
		getDataDictionary(caller).removeViewOrStream(name, caller);
	}

	@Override
	public List<ViewInformation> getStreamsAndViewsInformation(ISession caller) {
		Set<Entry<Resource, ILogicalOperator>> source = getDataDictionary(
				caller).getStreamsAndViews(caller);
		List<ViewInformation> ret = new ArrayList<>();
		for (Entry<Resource, ILogicalOperator> s : source) {
			ViewInformation vi = new ViewInformation();
			vi.setName(s.getKey());
			vi.setOutputSchema(s.getValue().getOutputSchema());
			ret.add(vi);
		}
		return ret;
	}

	@Override
	public List<SinkInformation> getSinks(ISession caller) {
		Set<Entry<Resource, ILogicalOperator>> sinks = getDataDictionary(caller)
				.getSinks(caller);
		List<SinkInformation> ret = new ArrayList<>();
		for (Entry<Resource, ILogicalOperator> s : sinks) {
			SinkInformation si = new SinkInformation();
			si.setName(s.getKey());
			si.setOutputSchema(s.getValue().getOutputSchema());
			ret.add(si);
		}
		return ret;
	}

	@Override
	public boolean containsViewOrStream(Resource name, ISession caller) {
		return getDataDictionary(caller).containsViewOrStream(name, caller);
	}

	@Override
	public boolean containsViewOrStream(String name, ISession caller) {
		return getDataDictionary(caller).containsViewOrStream(name, caller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getRegisteredDatatypes
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<SDFDatatype> getRegisteredDatatypes(ISession caller) {
		return getDataDictionary(caller).getDatatypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getRegisteredAggregateFunctions
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public Set<String> getRegisteredAggregateFunctions(
			@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel,
			ISession caller) {
		TreeSet<String> set = new TreeSet<>();
		set.addAll(AggregateFunctionBuilderRegistry.getFunctionNames(datamodel));
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorNames(de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<String> getOperatorNames(ISession caller) {
		return new ArrayList<String>(
				OperatorBuilderFactory.getOperatorBuilderNames());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorInformation(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public LogicalOperatorInformation getOperatorInformation(String name,
			ISession caller) {
		IOperatorBuilder builder = OperatorBuilderFactory
				.createOperatorBuilder(name, caller, getDataDictionary(caller),
						Context.empty(), this);
		LogicalOperatorInformation loi = new LogicalOperatorInformation();
		loi.setOperatorName(builder.getName());
		LogicalOperator annotation = builder.getOperatorClass().getAnnotation(
				LogicalOperator.class);
		if (annotation != null) {
			loi.setHidden(annotation.hidden());
			loi.setDeprecated(annotation.deprecation());
		} else {
			loi.setHidden(false);
			loi.setDeprecated(false);
		}
		loi.setMaxPorts(builder.getMaxInputOperatorCount());
		loi.setMinPorts(builder.getMinInputOperatorCount());
		loi.setDoc(builder.getDoc());
		loi.setUrl(builder.getUrl());
		loi.setCategories(builder.getCategories());

		for (IParameter<?> param : builder.getParameters()) {
			LogicalParameterInformation lpi = new LogicalParameterInformation();
			lpi.setName(param.getName());
			lpi.setMandatory(param.isMandatory());
			lpi.setDeprecated(param.isDeprecated());
			lpi.setParameterClass(param.getClass());
			lpi.setDoc(param.getDoc());
			lpi.setPossibleValues(resolvePossibleOperatorParameterValue(
					builder, param, caller));
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
	 * @see de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor#
	 * getOperatorInformations
	 * (de.uniol.inf.is.odysseus.core.usermanagement.ISession)
	 */
	@Override
	public List<LogicalOperatorInformation> getOperatorInformations(
			ISession caller) {
		List<LogicalOperatorInformation> infos = new ArrayList<>();
		for (String name : getOperatorNames(caller)) {
			infos.add(getOperatorInformation(name, caller));
		}
		return infos;
	}

	private List<String> resolvePossibleOperatorParameterValue(
			IOperatorBuilder builder, IParameter<?> param, ISession caller) {
		if (param instanceof EnumParameter) {
			EnumParameter eParam = (EnumParameter) param;
			@SuppressWarnings("rawtypes")
			Class<? extends Enum> enumClass = eParam.getEnum();
			List<String> ret = new LinkedList<>();
			for (@SuppressWarnings("rawtypes")
			Enum u : enumClass.getEnumConstants()) {
				ret.add(u.toString());
			}
			return ret;
		}
		if (param.getPossibleValueMethod().isEmpty()) {
			return new ArrayList<>();
		}
		// treat special cases
		if (param.getPossibleValueMethod().equalsIgnoreCase("__DD_SOURCES")) {
			Set<Entry<Resource, ILogicalOperator>> v = getDataDictionary(caller)
					.getViews(caller);
			Set<Entry<Resource, ILogicalOperator>> s = getDataDictionary(caller)
					.getStreams(caller);
			List<String> ret = new LinkedList<>();
			if (v != null) {
				for (Entry<Resource, ILogicalOperator> e : v) {
					ret.add(e.getKey().toString());
				}
			}
			if (s != null) {
				for (Entry<Resource, ILogicalOperator> e : s) {
					ret.add(e.getKey().toString());
				}
			}
			return ret;
		}
		if (param.getPossibleValueMethod().equalsIgnoreCase("__DD_DATATYPES")) {
			Set<String> s = getDataDictionary(caller).getDatatypeNames();
			return new LinkedList<>(s);
		}
		if (param.getPossibleValueMethod().equalsIgnoreCase("__DD_SINKS")) {
			Set<Entry<Resource, ILogicalOperator>> s = getDataDictionary(caller)
					.getSinks(caller);
			List<String> ret = new LinkedList<>();
			if (s != null) {
				for (Entry<Resource, ILogicalOperator> e : s) {
					ret.add(e.getKey().toString());
				}
			}
			return ret;
		}
		if (param.getPossibleValueMethod().equalsIgnoreCase("__JAVA_TIMEUNITS")) {
			List<String> ret = new LinkedList<>();
			for (TimeUnit u : TimeUnit.values()) {
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

	@Override
	public List<String> getQueryParserSuggestions(String queryParser,
			String hint, ISession user) {
		return getCompiler().getQueryParserSuggestions(queryParser, hint, user);
	}

	@Override
	public Map<String, List<String>> getQueryParserTokens(String queryParser,
			ISession user) {
		return getCompiler().getQueryParserTokens(queryParser, user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IUser> getUsers(ISession caller) {
		return (List<IUser>) UserManagementProvider.getUsermanagement(true)
				.getUsers(caller);
	}

	@Override
	public Collection<String> getUdfs() {
		return OperatorBuilderFactory.getUdfs();
	}

}