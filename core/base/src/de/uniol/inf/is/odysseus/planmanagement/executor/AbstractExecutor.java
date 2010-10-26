package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.event.error.ExceptionEventType;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitorFactory;
import de.uniol.inf.is.odysseus.physicaloperator.access.Router;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ISettingChangeListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.querysharing.IQuerySharingOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.planmanagement.query.IQueryReoptimizeListener;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.scheduler.manager.IScheduleable;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;

/**
 * AbstractExecutor bietet eine abstrakte Implementierung der
 * Ausführungumgebung. Sie übernimmt die Aufgabe zum einbinden von OSGi-Services
 * innerhalb des Odysseus-Frameworks.
 * 
 * @author wolf
 * 
 */
public abstract class AbstractExecutor implements IExecutor, IScheduleable,
		ISettingChangeListener, IQueryReoptimizeListener,
		IPlanReoptimizeListener {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AbstractExecutor.class);
		}
		return _logger;
	}

	/**
	 * Alle in Odysseus gespeicherten Anfragen
	 */
	protected IPlan plan;

	/**
	 * Der aktuell ausgeführte physische Plan
	 */
	protected IExecutionPlan executionPlan;

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
	 * Konfiguration der Ausführungsumgebung
	 */
	protected ExecutionConfiguration configuration = new ExecutionConfiguration();

	/**
	 * Alle Listener für Anfragebearbeitungs-Nachrichten
	 */
	private List<IPlanModificationListener> planModificationListener = Collections
			.synchronizedList(new ArrayList<IPlanModificationListener>());

	/**
	 * Alle Listener für Ausführungs-Nachrichten
	 */
	private List<IPlanExecutionListener> planExecutionListener = Collections
			.synchronizedList(new ArrayList<IPlanExecutionListener>());

	/**
	 * Alle Listener für Fehler-Nachrichten
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
	
	/**
	 * optional Query-Sharing-Component
	 */
	protected IQuerySharingOptimizer querySharingOptimizer = null;

	// --------------------------------------------------------------------------------------
	// Constructors/Initialization
	// --------------------------------------------------------------------------------------

	/**
	 * Standard-Construktor. Initialisiert die Ausführungsumgebung.
	 */
	public AbstractExecutor() {
		getLogger().trace("Create Executor.");
		try {
			initialize();
		} catch (ExecutorInitializeException e) {
			getLogger().error(
					"Error activate executor. Error: " + e.getMessage());
		}
	}

	/**
	 * initializeIntern Innerhalb dieser Funktion kÃ¶nnen spezifische
	 * Initialisierungen vorgenommen werden. Dies wird von initialize
	 * aufgerufen. Hier müssen ein Plan und Ausführungsplan-Objekt erstellt
	 * werden.
	 * 
	 * @param configuration
	 *            Konfiguration der Ausführungsumgebung.
	 */
	protected abstract void initializeIntern(
			ExecutionConfiguration configuration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#getConfiguration
	 * ()
	 */
	@Override
	public ExecutionConfiguration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#initialize()
	 */
	@Override
	public void initialize() throws ExecutorInitializeException {
		getLogger().debug("Start initializing Executor.");

		initializeIntern(configuration);

		if (this.plan == null) {
			throw new ExecutorInitializeException(
					"Plan storage not initialized.");
		}

		if (this.executionPlan == null) {
			throw new ExecutorInitializeException(
					"Execution plan storage not initialized.");
		}

		this.configuration.addValueChangeListener(this);

		getLogger().debug("Stop initializing Executor.");
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
		getLogger().debug("Optimizer bound " + optimizer);
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
			getLogger().debug("Optimizer unbound " + optimizer);
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
		getLogger().debug("Schedulermanager bound " + schedulerManager);
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
			getLogger().debug("Schedulermanager unbound " + schedulerManager);
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
		getLogger().debug("Compiler bound " + compiler);
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
			getLogger().debug("Compiler unbound " + compiler);
		}
	}
	
	/**
	 * bindQuerySharingOptimizer bindet eine Querysharing-Komponente ein
	 * 
	 * @param querySharingOptimizer
	 *            neue Querysharing-Komponente
	 */
	public void bindQuerySharingOptimizer(IQuerySharingOptimizer querySharingOptimizer) {
		this.querySharingOptimizer = querySharingOptimizer;
		getLogger().debug("QuerysharingOptimizer bound " + querySharingOptimizer);
	}

	/**
	 * unbindQuerysharingOptimizer entfernt eine Querysharing-Komponente
	 * 
	 * @param querySharingOptimizer
	 *            zu entfernende Querysharing-Komponente
	 */
	public void unbindQuerySharingOptimizer(IQuerySharingOptimizer querySharingOptimizer) {
		if (this.querySharingOptimizer == querySharingOptimizer) {
			this.querySharingOptimizer = null;
			getLogger().debug("QuerysharingOptimizer unbound " + querySharingOptimizer);
		}
	}

	// ----------------------------------------------------------------------------------------
	// Getter/Setter
	// ----------------------------------------------------------------------------------------
	/**
	 * optimizer liefert der aktuelle Optimierer zurück. Sollte keiner vorhanden
	 * sein, wird eine Exception geworfen.
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
	
	/**
	 * QuerySharingOptimizer liefert den aktuellen QuerySharingOpzimizer.
	 * 
	 * @return aktueller QuerySharingOptimizer
	 * 
	 */
	@Override
	public IQuerySharingOptimizer getQuerySharingOptimizer()  {
		if (this.querySharingOptimizer != null) {
			return this.querySharingOptimizer;
		}

		return null;
	}

	// ----------------------------------------------------------------------------------------
	// Execution Plan
	// ----------------------------------------------------------------------------------------

	/**
	 * setExecutionPlan setzt den aktuellen Ausführungsplan und aktualisiert das
	 * Scheduling.
	 * 
	 * @param newExecutionPlan
	 *            neuer Ausführungsplan
	 */
	protected void setExecutionPlan(IExecutionPlan newExecutionPlan) {

		if (newExecutionPlan != null
				&& !newExecutionPlan.equals(this.executionPlan)) {
			try {
				executionPlanLock.lock();
				getLogger().info("Set execution plan.");
				// Init current execution plan with newExecutionPlan
				this.executionPlan.initWith(newExecutionPlan);
				if (isRunning()) {
					getLogger().info("Set execution plan. Open");
					this.executionPlan.open();
				}
				getLogger().info("Set execution plan. Refresh Scheduling");
				getSchedulerManager().refreshScheduling(this);
				getLogger().info("New execution plan set.");
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error(
						"Error while setting new execution plan. "
								+ e.getMessage());
				fireErrorEvent(new ErrorEvent(this, ExceptionEventType.ERROR,
						"Error while setting new execution plan. ", e));
			} finally {
				executionPlanLock.unlock();
			}
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
			listener.planModificationEvent(eventArgs);
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
			listener.planExecutionEvent(eventArgs);
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
			listener.errorEventOccured(eventArgs);
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
			getLogger().debug("Scheduler already running.");
			return;
		}

		getLogger().info("Start Scheduler.");
		getLogger().debug(
				"#PartialPlans: " + this.executionPlan.getPartialPlans().size()
						+ " #Roots: " + this.executionPlan.getRoots());
		try {
			this.executionPlan.open();

			firePlanExecutionEvent(new PlanExecutionEvent(this,
					PlanExecutionEventType.EXECUTION_PREPARED));

			getSchedulerManager().startScheduling();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		getLogger().info("Scheduler started.");

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
			getLogger().debug("Scheduler not running.");
			return;
		}
		getLogger().info("Stop Scheduler.");
		try {
			getSchedulerManager().stopScheduling();
			this.executionPlan.close();
			// Stopp Router only if it has an instance
			if (Router.hasInstance()) {
				Router.getInstance().stopRouting();
			}
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		getLogger().info("Scheduler stopped.");

		firePlanExecutionEvent(new PlanExecutionEvent(this,
				PlanExecutionEventType.EXECUTION_STOPPED));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#isRunning
	 * ()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution
	 * .IPlanExecutionHandler#addPlanExecutionListener(de.uniol.inf.is.odysseus.
	 * planmanagement
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
	 * de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution
	 * .
	 * IPlanExecutionHandler#removePlanExecutionListener(de.uniol.inf.is.odysseus
	 * .
	 * planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener
	 * )
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
	 * @see
	 * de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#getSealedPlan
	 * ()
	 */
	@Override
	public IPlan getSealedPlan() throws PlanManagementException {
		return this.plan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#
	 * getSupportedQueryParser()
	 */
	@Override
	public Set<String> getSupportedQueryParsers()
			throws NoCompilerLoadedException {
		ICompiler c;
		c = getCompiler();
		return c.getSupportedQueryParser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler #
	 * addErrorEventListener(de.uniol.inf.is.odysseus.planmanagement.event.
	 * error.IErrorEventListener)
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
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventHandler #
	 * removeErrorEventListener(de.uniol.inf.is.odysseus.planmanagement.event
	 * .error.IErrorEventListener)
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
	 * @see de.uniol.inf.is.odysseus.event.error.IErrorEventListener
	 * #sendErrorEvent(de.uniol.inf.is.odysseus.event.error. ErrorEvent)
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
	public void addCompilerListener(ICompilerListener compilerListener) {
		this.compilerListener.add(compilerListener);
		// if Compiler already bound
		if (compiler != null) {
			compiler.addCompilerListener(compilerListener);
		} // else will be done if compiler is bound
	}

//	@Override
//	public void setDefaultBufferPlacementStrategy(String strategy) {
//		IBufferPlacementStrategy strat = this
//				.getBufferPlacementStrategy(strategy);
//		this.configuration.set(new ParameterBufferPlacementStrategy(strat));
//	}

}