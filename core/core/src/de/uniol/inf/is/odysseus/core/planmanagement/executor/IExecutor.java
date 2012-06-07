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
package de.uniol.inf.is.odysseus.core.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * IExecutor stellt die Hauptschnittstelle fuer externe Anwendungen zu Odysseus
 * da. Es werden Funktionen zur Bearbeitung von Anfragen, Steuerung der
 * Ausfuehrung und Konfigurationen zur Verfuegung gestellt. Weiterhin bietet
 * diese Schnittstelle diverse Moeglichkeit, um Nachrichten ueber aenderungen
 * innerhalb von Odysseus zu erhalten.
 * 
 * @author Wolf Bauer, Marco Grawunder
 * 
 */
public interface IExecutor extends IClientPlanManager{

	/**
	 * List of predefined registered build configurations
	 */
	public Collection<String> getQueryBuildConfigurationNames();

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
	 * addQuery fuegt Odysseus eine Anfrage hinzu, die als Zeichenkette
	 * vorliegt.
	 * 
	 * @param query
	 *            Anfrage in Form einer Zeichenkette
	 * @param parserID
	 *            ID des zu verwendenden Parsers, ueberschreibt einen evtl.
	 *            vorhandenen Eintrag in parameters
	 * @param queryBuildConfigurationName
	 *            Name der zu verwendeden Build-Configuration
	 * @return vorläufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, String queryBuildConfigurationName)
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
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException;

	/**
	 * addQuery fuegt Odysseus eine Anfrage hinzu, die als physischer Plan
	 * vorliegt.
	 * 
	 * @param physicalPlan
	 *            physischer Plan der neuen Anfrage
	 * @param queryBuildConfigurationName
	 *            Name der zu verwendeden Build-Configuration
	 * @throws PlanManagementException
	 */
	public Integer addQuery(List<IPhysicalOperator> physicalPlan, ISession user,
			String queryBuildConfigurationName) throws PlanManagementException;

	/**
	 * Return the logical query with the id
	 * @param id
	 * @return
	 */
	public ILogicalQuery getLogicalQuery(int id);
	
	/**
	 * getLogicalQueryIds gibt die Query-IDs aller Anfragen zurück
	 * 
	 * @return IDs aller Anfragen
	 */
	public Collection<Integer> getLogicalQueryIds();
	
	/**
	 * Returns for a query the physical root operators
	 * 
	 * @param queryID
	 * @return
	 */
	public List<IPhysicalOperator> getPhyiscalRoots(int queryID);
	
	/**
	 * Start all queries that are currently not running
	 * 
	 * @param user
	 * @return List of queries that could be started
	 */
	public Collection<Integer> startAllClosedQueries(ISession user);

	/**
	 * Provides a Set of registered buffer placement strategies represented by
	 * an id.
	 * 
	 * @return Set of registered buffer placement strategies represented by an
	 *         id
	 */
	public Set<String> getRegisteredBufferPlacementStrategiesIDs();

	/**
	 * Provides a Set of registered scheduling strategy strategies represented
	 * by an id.
	 * 
	 * @return Set of registered scheduling strategy strategies represented by
	 *         an id
	 */
	public Set<String> getRegisteredSchedulingStrategies();

	/**
	 * Provides a Set of registered schedulers represented by an id.
	 * 
	 * @return Set of registered schedulers represented by an id
	 */
	public Set<String> getRegisteredSchedulers();

	/**
	 * Sets the the scheduler with a scheduling strategy which should be used
	 * for creating concrete scheduler.
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
	 * Get the current active scheduling strategy factory represented by an id.
	 * 
	 * @return current active scheduling strategy factory represented by an id.
	 */
	public String getCurrentSchedulingStrategyID();

	public String getName();

	// Facade
	// Session Management methods
	ISession login(String username, byte[] password);

	void logout(ISession caller);
	
	// Facade for DataDictionary
	public ILogicalOperator removeSink(String name, ISession caller);
	public void removeViewOrStream(String name, ISession caller);
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller);
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller);

	void reloadStoredQueries(ISession caller);

}
