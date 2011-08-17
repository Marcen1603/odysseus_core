/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.Configuration;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * IExecutor stellt die Hauptschnittstelle fuer externe Anwendungen zu Odysseus
 * da. Es werden Funktionen zur Bearbeitung von Anfragen, Steuerung der
 * Ausfuehrung und Konfigurationen zur Verfuegung gestellt. Weiterhin bietet diese
 * Schnittstelle diverse Moeglichkeit, um Nachrichten ueber aenderungen innerhalb
 * von Odysseus zu erhalten.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IExecutor extends IPlanManager, IPlanScheduling,
		IInfoProvider, IErrorEventHandler, IErrorEventListener, IOptimizable {
	/**
	 * initialize initialisiert die Ausfuehrungsumgebung. ggf. gehen Anfragen,
	 * Pläne und Einstellungen verloren.
	 * 
	 * @throws ExecutorInitializeException
	 */
	public void initialize() throws ExecutorInitializeException;

	/**
	 * getConfiguration liefert die aktuelle Konfiguration der
	 * AUsfuehrungsumgebung.
	 * 
	 * @return die aktuelle Konfiguration der AUsführungsumgebung
	 */
	public ExecutionConfiguration getConfiguration();

	/**
	 * List of predefined registered build configurations
	 */
	public Collection<String> getQueryBuildConfigurationNames();
	
	/**
	 * Get specific query build configuration
	 */
	public IQueryBuildConfiguration getQueryBuildConfiguration(String name); 
	
	/**
	 * Get all QueryBuildConfigurations
	 * @return all build configuration
	 */
	public Map<String, IQueryBuildConfiguration> getQueryBuildConfigurations();
	/**
	 * getSupportedQueryParser liefert alle IDs der zur Verfuegung stehenden
	 * Parser zur uebersetzung von Anfragen, die als Zeichenkette vorliegen.
	 * 
	 * @return Liste aller IDs der zur Verfuegung stehenden Parser zur
	 *         uebersetzung von Anfragen, die als Zeichenkette vorliegen
	 * @throws PlanManagementException
	 */
	public Set<String> getSupportedQueryParsers()
			throws PlanManagementException;

	/**
	 * addQuery fuegt Odysseus eine Anfrage hinzu, die als Zeichenkette vorliegt.
	 * 
	 * @param query
	 *            Anfrage in Form einer Zeichenkette
	 * @param parserID
	 *            ID des zu verwendenden Parsers, ueberschreibt einen evtl. vorhandenen Eintrag in parameters
	 * @param queryBuildConfigurationName
	 *            Name der zu verwendeden Build-Configuration
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */	
	public Collection<IQuery> addQuery(String query, String parserID, User user, IDataDictionary dd,
			String queryBuildConfigurationName)
			throws PlanManagementException;

	
		
	/**
	 * addQuery fuegt Odysseus eine Anfrage hinzu, die als logischer Plan
	 * vorliegt. 
	 * 
	 * @param logicalPlan
	 *            logischer Plan der Anfrage
	 * @param queryBuildConfigurationName
	 *            Name der zu verwendeden Build-Configuration
	 * @return vorl�ufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */	
	public IQuery addQuery(ILogicalOperator logicalPlan, User user, IDataDictionary dd,
			String queryBuildConfigurationName)
			throws PlanManagementException;

	/**
	 * addQuery fuegt Odysseus eine Anfrage hinzu, die als physischer Plan
	 * vorliegt. 
	 * 
	 * @param physicalPlan
	 *            physischer Plan der neuen Anfrage
	 * @param queryBuildConfigurationName
	 *            Name der zu verwendeden Build-Configuration
	 * @return vorl�ufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */	
	public IQuery addQuery(List<IPhysicalOperator> physicalPlan, User user, IDataDictionary dd,
			String queryBuildConfigurationName)
			throws PlanManagementException;
	

	/**
	 * Start all queries that are currently not running
	 * @param user
	 * @return List of queries that could be started
	 */
	public List<IQuery> startAllClosedQueries(User user);
	
	public void addCompilerListener(ICompilerListener compilerListener);
	
	/**
	 * Provides a Set of registered buffer placement strategies represented by
	 * an id.
	 * 
	 * @return Set of registered buffer placement strategies represented by an
	 *         id
	 */
	public Set<String> getRegisteredBufferPlacementStrategiesIDs();
	public IBufferPlacementStrategy getBufferPlacementStrategy(String stratID);

//	/**
//	 * Set the buffer placement strategy which should be used.
//	 * 
//	 * @param strategy
//	 *            new buffer placement strategy which should be used.
//	 */
//	public void setDefaultBufferPlacementStrategy(String strategy);

	/**
	 * Provides a Set of registered scheduling strategy strategies represented by
	 * an id.
	 * 
	 * @return Set of registered scheduling strategy strategies represented by an
	 *         id
	 */
	public Set<String> getRegisteredSchedulingStrategies();

	/**
	 * Provides a Set of registered schedulers represented by an id.
	 * 
	 * @return Set of registered schedulers represented by an id
	 */
	public Set<String> getRegisteredSchedulers();

	/**
	 * Sets the the scheduler with a scheduling strategy which
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
	 * Get the current active scheduler represented by an id.
	 * 
	 * @return current active scheduler represented by an id.
	 */
	public String getCurrentSchedulerID();

	/**
	 * Get the current active scheduler
	 * 
	 * @return current active scheduler
	 */
	
	public IScheduler getCurrentScheduler();
	
	/**
	 * Get the current active scheduling strategy factory represented by an id.
	 * 
	 * @return current active scheduling strategy factory represented by an id.
	 */
	public String getCurrentSchedulingStrategyID();

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

	public String getName();

	IOptimizer getOptimizer() throws NoOptimizerLoadedException;
	@Override
	List<IQuery> getQueries();

}
