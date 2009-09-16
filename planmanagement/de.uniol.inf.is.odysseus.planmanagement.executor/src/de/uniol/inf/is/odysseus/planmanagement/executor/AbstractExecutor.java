package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent;
import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IEditablePlan;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlan;
import de.uniol.inf.is.odysseus.base.planmanagement.plan.IPlanReoptimizeListener;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQueryReoptimizeListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IEditableExecutionPlan;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ISettingChangeListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
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
		ISettingChangeListener, CommandProvider, IOptimizable,
		IQueryReoptimizeListener, IPlanReoptimizeListener {

	/**
	 * Logger zum Ausgeben von Systemmeldungen
	 */
	protected Logger logger;

	/**
	 * Alle in Odysseus gespeicherten Anfragen
	 */
	protected IEditablePlan plan;

	/**
	 * Der aktuell ausgeführte physische Plan
	 */
	protected IEditableExecutionPlan executionPlan;

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
	 * Lock for synchronizing execution plan changes.
	 */
	protected ReentrantLock executionPlanLock = new ReentrantLock();

	/**
	 * setExecutionPlan setzt den aktuellen Ausführungsplan und aktualisiert das
	 * Scheduling.
	 * 
	 * @param newExecutionPlan
	 *            neuer Ausführungsplan
	 */
	protected void setExecutionPlan(IExecutionPlan newExecutionPlan) {
		if (newExecutionPlan != null && !newExecutionPlan.equals(this.executionPlan)) {
			try {
				this.logger.info("Set execution plan.");
				// Init current execution plan with newExecutionPlan
				this.executionPlan.initWith(newExecutionPlan);

				if (isRunning()) {
					this.executionPlan.open();
				}

				schedulerManager().refreshScheduling(this);
				this.logger.info("New execution plan set.");
			} catch (Exception e) {
				this.logger.error("Error while setting new execution plan. "
						+ e.getMessage());
				fireErrorEvent(new ErrorEvent(this, ErrorEvent.ERROR,
						"Error while setting new execution plan. "
								+ e.getMessage()));
			}
		}
	}

	/**
	 * Standard-Construktor. Initialisiert die Ausführungsumgebung.
	 */
	public AbstractExecutor() {
		this.logger = LoggerFactory.getLogger(AbstractExecutor.class);
		this.logger.trace("Create Executor.");
		try {
			initialize();
		} catch (ExecutorInitializeException e) {
			this.logger.error("Error activate executor. Error: "
					+ e.getMessage());
		}
	}

	/**
	 * bindOptimizer bindet einen Optimierer ein
	 * 
	 * @param optimizer
	 *            neuer Optimierer
	 */
	public void bindOptimizer(IOptimizer optimizer) {
		this.optimizer = optimizer;
		this.optimizer.addErrorEventListener(this);
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
		}
	}

	/**
	 * optimizer liefert der aktuelle Optimierer zurück. Sollte keiner vorhanden
	 * sein, wird eine Exception geworfen.
	 * 
	 * @return aktueller Optimierer
	 * @throws NoOptimizerLoadedException
	 */
	protected IOptimizer optimizer() throws NoOptimizerLoadedException {
		if (this.optimizer != null) {
			return this.optimizer;
		}

		throw new NoOptimizerLoadedException();
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
		}
	}

	/**
	 * schedulerManager liefert den aktuellen Scheduling-Manager. Sollte keiner
	 * registriert sein, wird eine Exception geworfen.
	 * 
	 * @return aktueller Scheduling-Manager
	 * @throws SchedulerException
	 */
	protected ISchedulerManager schedulerManager() throws SchedulerException {
		if (this.schedulerManager != null) {
			return this.schedulerManager;
		}

		throw new SchedulerException();
	}

	/**
	 * bindCompiler bindet eine Anfragebearbeitungs-Komponente ein
	 * 
	 * @param compiler
	 *            neue Anfragebearbeitungs-Komponente
	 */
	public void bindCompiler(ICompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * unbindCompiler entfernt eine Anfragebearbeitungs-Komponente
	 * 
	 * @param compiler
	 *            zu entfernende Anfragebearbeitungs-Komponente
	 */
	public void unbindCompiler(ICompiler compiler) {
		if (this.compiler == compiler) {
			this.compiler = null;
		}
	}

	/**
	 * compiler liefert die aktuelle Anfragebearbeitungs-Komponente
	 * 
	 * @return die aktuelle Anfragebearbeitungs-Komponente
	 * @throws NoCompilerLoadedException
	 */
	protected ICompiler compiler() throws NoCompilerLoadedException {
		if (this.compiler != null) {
			return this.compiler;
		}

		throw new NoCompilerLoadedException();
	}

	/**
	 * _ExecutorInfo schreibt Informationen über die Ausführungsumgebung in die
	 * Konsole. Kann in der OSGi-Konsole verwendet werden.
	 * 
	 * @param ci
	 */
	public void _ExecutorInfo(CommandInterpreter ci) {
		System.out.print(getInfos());
	}

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
	protected synchronized void fireErrorEvent(ErrorEvent eventArgs) {
		for (IErrorEventListener listener : this.errorEventListener) {
			listener.sendErrorEvent(eventArgs);
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#getConfiguration()
	 */
	@Override
	public ExecutionConfiguration getConfiguration() {
		return configuration;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#initialize()
	 */
	@Override
	public void initialize() throws ExecutorInitializeException {
		this.logger.info("Start initializing Executor.");

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

		this.logger.info("Stop initializing Executor.");
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable#getSchedulerManager()
	 */
	@Override
	public ISchedulerManager getSchedulerManager() {
		return this.schedulerManager;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#startExecution()
	 */
	@Override
	public void startExecution() throws SchedulerException {
		if (isRunning()) {
			this.logger.debug("Scheduler already running.");
			return;
		}

		this.logger.info("Start Scheduler.");
		try {
			this.executionPlan.open();
			schedulerManager().startScheduling();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		this.logger.info("Scheduler started.");

		firePlanExecutionEvent(new PlanExecutionEvent(this,
				PlanExecutionEvent.EXECUTION_STARTED));
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#stopExecution()
	 */
	@Override
	public void stopExecution() throws SchedulerException {
		if (!isRunning()) {
			this.logger.debug("Scheduler not running.");
			return;
		}
		this.logger.info("Stop Scheduler.");
		try {
			schedulerManager().stopScheduling();
			this.executionPlan.close();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		this.logger.info("Scheduler stopped.");

		firePlanExecutionEvent(new PlanExecutionEvent(this,
				PlanExecutionEvent.EXECUTION_STOPPED));
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#isRunning()
	 */
	@Override
	public boolean isRunning() throws SchedulerException {
		try {
			return schedulerManager().isRunning();
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling#getExecutionPlan()
	 */
	@Override
	public IExecutionPlan getExecutionPlan() {
		return this.executionPlan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.IQueryOptimizable#getCompiler()
	 */
	@Override
	public ICompiler getCompiler() {
		try {
			return compiler();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationHandler#addPlanModificationListener(de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener)
	 */
	@Override
	public void addPlanModificationListener(IPlanModificationListener listener) {
		synchronized (this.planModificationListener) {
			if (!this.planModificationListener.contains(listener)) {
				this.planModificationListener.add(listener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationHandler#removePlanModificationListener(de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener)
	 */
	@Override
	public void removePlanModificationListener(
			IPlanModificationListener listener) {
		synchronized (this.planModificationListener) {
			this.planModificationListener.remove(listener);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionHandler#addPlanExecutionListener(de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener)
	 */
	@Override
	public void addPlanExecutionListener(IPlanExecutionListener listener) {
		synchronized (this.planExecutionListener) {
			if (!this.planExecutionListener.contains(listener)) {
				this.planExecutionListener.add(listener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionHandler#removePlanExecutionListener(de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener)
	 */
	@Override
	public void removePlanExecutionListener(IPlanExecutionListener listener) {
		synchronized (this.planExecutionListener) {
			this.planExecutionListener.remove(listener);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager#getSealedPlan()
	 */
	@Override
	public IPlan getSealedPlan() throws PlanManagementException {
		return this.plan;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor#getSupportedQueryParser()
	 */
	@Override
	public Set<String> getSupportedQueryParser()
			throws NoCompilerLoadedException {
		return compiler().getSupportedQueryParser();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler#addErrorEventListener(de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener)
	 */
	@Override
	public void addErrorEventListener(IErrorEventListener errorEventListener) {
		synchronized (this.errorEventListener) {
			if (!this.errorEventListener.contains(errorEventListener)) {
				this.errorEventListener.add(errorEventListener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler#removeErrorEventListener(de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener)
	 */
	@Override
	public void removeErrorEventListener(IErrorEventListener errorEventListener) {
		synchronized (this.errorEventListener) {
			this.errorEventListener.remove(errorEventListener);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventListener#sendErrorEvent(de.uniol.inf.is.odysseus.base.planmanagement.event.error.ErrorEvent)
	 */
	@Override
	public synchronized void sendErrorEvent(ErrorEvent eventArgs) {
		fireErrorEvent(new ErrorEvent(this, ErrorEvent.ERROR,
				"Executor exception (with inner error). "
						+ eventArgs.getMessage()));
	}

}