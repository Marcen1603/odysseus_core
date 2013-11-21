/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.Configuration;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.distribution.IDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfigurationTemplate;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.core.server.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * This Interface contains all methods from the executor that are accessable if
 * the executor is used on a server
 * 
 * @author Marco Grawunder
 * 
 */
public interface IServerExecutor extends IExecutor, IPlanScheduling,
		IPlanManager, IErrorEventHandler, IErrorEventListener, IInfoProvider {

	public Collection<Integer> addQuery(String query, String parserID,
			ISession user, String queryBuildConfigurationName, Context context,
			List<IQueryBuildSetting<?>> overwriteSetting)
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
			String queryBuildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException;

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
	public Integer addQuery(List<IPhysicalOperator> physicalPlan,
			ISession user, String queryBuildConfigurationName,
			List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException;

	/**
	 * getConfiguration liefert die aktuelle Konfiguration der
	 * AUsfuehrungsumgebung.
	 * 
	 * @return die aktuelle Konfiguration der AUsführungsumgebung
	 */
	public ExecutionConfiguration getConfiguration();

	/**
	 * Get specific query build configuration
	 */
	public IQueryBuildConfigurationTemplate getQueryBuildConfiguration(String name);

	/**
	 * Get all QueryBuildConfigurations
	 * 
	 * @return all build configuration
	 */
	public Map<String, IQueryBuildConfigurationTemplate> getQueryBuildConfigurations();

	public void addCompilerListener(ICompilerListener compilerListener);

	public IBufferPlacementStrategy getBufferPlacementStrategy(String stratID);

	/**
	 * Get the current active scheduler
	 * 
	 * @return current active scheduler
	 */

	public IScheduler getCurrentScheduler();

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
	 * 
	 * @return {@link ISystemMonitor}
	 * 
	 * @throws NoSystemMonitorLoadedException
	 */
	public ISystemMonitor getDefaultSystemMonitor()
			throws NoSystemMonitorLoadedException;

	/**
	 * Creates a new System Monitor with the specified period.
	 * 
	 * @param period
	 *            measure period.
	 * @return {@link ISystemMonitor}
	 * 
	 * @throws NoSystemMonitorLoadedException
	 */
	public ISystemMonitor newSystemMonitor(long period)
			throws NoSystemMonitorLoadedException;

	IOptimizer getOptimizer() throws NoOptimizerLoadedException;

	ICompiler getCompiler();

	Optional<ILogicalQueryDistributor> getLogicalQueryDistributor(String name);

	ImmutableCollection<String> getLogicalQueryDistributorNames();
	
	/**
	 * Returns the data fragmentation strategy by the given name, if it is bound.
	 * @author Michael Brand
	 */
	Optional<IDataFragmentation> getDataFragmentation(String name);

	/**
	 * Returns a collection of the names of all bound data fragmentation strategies.
	 * @author Michael Brand
	 */
	ImmutableCollection<String> getDataFragmentationNames();

	// Facade for Compiler
	public List<IExecutorCommand> translateQuery(String query, String parserID,
			ISession user, Context context) throws QueryParseException;

	public IPhysicalQuery transform(ILogicalQuery query,
			TransformationConfiguration transformationConfiguration,
			ISession caller) throws TransformationException;

	IDataDictionaryWritable getDataDictionary(ITenant tenant);

	public boolean removeAllQueries(ISession caller);

	void executionPlanChanged(PlanModificationEventType type,
			Collection<IPhysicalQuery> affectedQueries)
			throws SchedulerException, NoSchedulerLoadedException;

	void executionPlanChanged(PlanModificationEventType type,
			IPhysicalQuery affectedQuery) throws SchedulerException,
			NoSchedulerLoadedException;

	public ISchedulerManager getSchedulerManager();

	public QueryBuildConfiguration getBuildConfigForQuery(ILogicalQuery query);
	
	/**
	 * Allows the addition of a query using all the operators of an existing query. This can be used, when it was already determined,
	 * that the logical query in question is in every way identical to the old one and thus has no need to go through all the
	 * transformation- and re-optimization-processes again. This method wraps the old operators in a new query and adds it to the
	 * data dictionary and the execution plan
	 * @param idOfRunningQuery the old query, that provides the operators for the new query as well
	 * @param q the new logical query, to be associated with the new physical one
	 * @param user the session
	 * @param confName name of the buildconfiguration, only really needed for the datadictionary-entry
	 * @return the id of the newly created physical query
	 */
	public Integer addIdenticalQuery(Integer idOfRunningQuery, ILogicalQuery q, ISession user, String confName);

}
