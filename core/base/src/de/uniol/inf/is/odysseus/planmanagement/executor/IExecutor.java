package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.Configuration;

import de.uniol.inf.is.odysseus.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.AbstractQueryBuildSetting;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * IExecutor stellt die Hauptschnittstelle für externe Anwendungen zu Odysseus
 * da. Es werden Funktionen zur Bearbeitung von Anfragen, Steuerung der
 * Ausführung und Konfigurationen zur Verfügung gestellt. Weiterhin bietet diese
 * Schnittstelle diverse Möglichkeit, um Nachrichten über Änderungen innerhalb
 * von Odysseus zu erhalten.
 * 
 * @author wolf
 * 
 */
public interface IExecutor extends IOptimizable, IPlanManager, IPlanScheduling,
		IInfoProvider, IErrorEventHandler, IErrorEventListener {
	/**
	 * initialize initialisiert die AUsführungsumgebung. ggf. gehen Anfragen,
	 * Pläne und Einstellungen verloren.
	 * 
	 * @throws ExecutorInitializeException
	 */
	public void initialize() throws ExecutorInitializeException;

	/**
	 * getConfiguration liefert die aktuelle Konfiguration der
	 * AUsführungsumgebung.
	 * 
	 * @return die aktuelle Konfiguration der AUsführungsumgebung
	 */
	public ExecutionConfiguration getConfiguration();

	/**
	 * getSupportedQueryParser liefert alle IDs der zur Verfügung stehenden
	 * Parser zur Übersetzung von Anfragen, die als Zeichenkette vorliegen.
	 * 
	 * @return Liste aller IDs der zur Verfügung stehenden Parser zur
	 *         Übersetzung von Anfragen, die als Zeichenkette vorliegen
	 * @throws PlanManagementException
	 */
	public Set<String> getSupportedQueryParser()
			throws PlanManagementException;

	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als logischer Plan
	 * vorliegt. Es kann sein, dass die Anfrage nicht direkt der Auführung
	 * hinzugefügt wird (bspw. bei interner asychronen Optimierung). Die
	 * zurückgebegen ID ist daher nur vorläufig. Erst beim Empfangen des
	 * Hinzufügen-Events kann davon ausgegangen werden, dass die Anfrage
	 * hinzugefügt wurde.
	 * 
	 * @param logicalPlan
	 *            logischer Plan der Anfrage
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public int addQuery(ILogicalOperator logicalPlan, User user,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException;

	
	
	/**
	 * addQuery f�gt Odysseus eine Anfrage hinzu, die bereits als Query
	 * vorliegt. Es kann sein, dass die Anfrage nicht direkt der Auf�hrung
	 * hinzugef�gt wird (bspw. bei interner asychronen Optimierung). Die
	 * zur�ckgebegen ID ist daher nur vorl�ufig. Erst beim Empfangen des
	 * Hinzuf�en-Events kann davon ausgegangen werden, dass die Anfrage
	 * hinzugef�gt wurde.
	 * 
	 * @param query die query
	 * @return vorl�ufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */	
	public int addQuery(Query query) throws PlanManagementException;
	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als Zeichenkette vorliegt.
	 * Es kann sein, dass die Anfrage nicht direkt der Auführung hinzugefügt
	 * wird (bspw. bei interner asychronen Optimierung). Die zurückgebegen ID
	 * ist daher nur vorläufig. Erst beim Empfangen des Hinzufügen-Events kann
	 * davon ausgegangen werden, dass die Anfrage hinzugefügt wurde.
	 * 
	 * @param query
	 *            Anfrage in Form einer Zeichenkette
	 * @param compilerID
	 *            ID des zu verwendenden Parsers
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public Collection<Integer> addQuery(String query, String compilerID, User user,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException;
	
	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als Zeichenkette vorliegt.
	 * Es kann sein, dass die Anfrage nicht direkt der Auführung hinzugefügt
	 * wird (bspw. bei interner asychronen Optimierung). Die zurückgebegen ID
	 * ist daher nur vorläufig. Erst beim Empfangen des Hinzufügen-Events kann
	 * davon ausgegangen werden, dass die Anfrage hinzugefügt wurde.
	 * 
	 * @param query
	 *            Anfrage in Form einer Zeichenkette
	 * @param compilerID
	 *            ID des zu verwendenden Parsers
	 * @param doRestruct
	 *            If true, the query plan will be restructured, if false, it will not.
	 * @param rulesToUse
	 *            Contains the names of the rules to be used for restructuring. Other
	 *            rules will not be used.
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public Collection<Integer> addQuery(String query, String parserID, User user,
			boolean doRestruct,
			Set<String> rulesToUse,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException;

	/**
	 * addQuery fügt Odysseus eine Anfrage hinzu, die als physischer Plan
	 * vorliegt. Es kann sein, dass die Anfrage nicht direkt der Auführung
	 * hinzugefügt wird (bspw. bei interner asychronen Optimierung). Die
	 * zurückgebegen ID ist daher nur vorläufig. Erst beim Empfangen des
	 * Hinzufügen-Events kann davon ausgegangen werden, dass die Anfrage
	 * hinzugefügt wurde.
	 * 
	 * @param physicalPlan
	 *            physischer Plan der neuen Anfrage
	 * @param parameters
	 *            Parameter zum Bearbeiten, Erstellen und Verändern der Anfrage
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public int addQuery(List<IPhysicalOperator> physicalPlan, User user,
			AbstractQueryBuildSetting<?>... parameters)
			throws PlanManagementException;
	
	public void addCompilerListener(ICompilerListener compilerListener);

	// --- Moved vom AdvancedExecutor, easier handling
	
	/**
	 * Provides a Set of registered buffer placement strategies represented by
	 * an id.
	 * 
	 * @return Set of registered buffer placement strategies represented by an
	 *         id
	 */
	public Set<String> getRegisteredBufferPlacementStrategies();

	/**
	 * Set the buffer placement strategy which should be used.
	 * 
	 * @param strategy
	 *            new buffer placement strategy which should be used.
	 */
	public void setDefaultBufferPlacementStrategy(String strategy);

	/**
	 * Provides a Set of registered scheduling strategy factories represented by
	 * an id.
	 * 
	 * @return Set of registered scheduling strategy factories represented by an
	 *         id
	 */
	public Set<String> getRegisteredSchedulingStrategyFactories();

	/**
	 * Provides a Set of registered scheduler factories represented by an id.
	 * 
	 * @return Set of registered scheduler factories represented by an id
	 */
	public Set<String> getRegisteredSchedulerFactories();

	/**
	 * Sets the the scheduler factory with a scheduling strategy factory which
	 * should be used for creating concrete scheduler.
	 * 
	 * @param scheduler
	 *            scheduler factory which should be used for creating concrete
	 *            scheduler.
	 * @param schedulerStrategy
	 *            scheduling strategy factory which should be used by scheduler
	 *            for creating concrete scheduler.
	 */
	public void setScheduler(String scheduler, String schedulerStrategy);

	/**
	 * Get the current active scheduler factory represented by an id.
	 * 
	 * @return current active scheduler factory represented by an id.
	 */
	public String getCurrentScheduler();

	/**
	 * Get the current active scheduling strategy factory represented by an id.
	 * 
	 * @return current active scheduling strategy factory represented by an id.
	 */
	public String getCurrentSchedulingStrategy();

	/**
	 * 
	 * @return {@link Configuration} of current {@link IOptimizer}.
	 * 
	 * @throws NoOptimizerLoadedException
	 */
	public OptimizationConfiguration getOptimizerConfiguration()
			throws NoOptimizerLoadedException;
	
	/**
	 * Returns the default System Monitor with an fixed measure period.
	 * @return {@link ISystemMonitor}
	 * 
	 * @throws NoSystemMonitorLoadedException
	 */
	public ISystemMonitor getDefaultSystemMonitor()
			throws NoSystemMonitorLoadedException;
	
	/**
	 * Creates a new System Monitor with the specified period.
	 * @param period measure period.
	 * @return {@link ISystemMonitor}
	 * 
	 * @throws NoSystemMonitorLoadedException
	 */
	public ISystemMonitor newSystemMonitor(long period)
			throws NoSystemMonitorLoadedException;
	
	/**
	 * Updates the execution plan to find new iterable sources, if the plan 
	 * has changed.
	 * 
	 * @throws NoOptimizerLoadedException
	 * @throws QueryOptimizationException
	 */
	public void updateExecutionPlan() throws NoOptimizerLoadedException,
			QueryOptimizationException;
}
