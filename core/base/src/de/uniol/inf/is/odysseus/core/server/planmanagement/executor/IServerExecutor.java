package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.Configuration;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
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
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.configuration.ExecutionConfiguration;
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
			ISession user, String queryBuildConfigurationName, List<IQueryBuildSetting<?>> overwriteSetting)
			throws PlanManagementException;

	/**
	 * addQuery fuegt Odysseus eine Anfrage hinzu, die als logischer Plan
	 * vorliegt.
	 * 
	 * @param logicalPlan
	 *            logischer Plan der Anfrage
	 * @param queryBuildConfigurationName
	 *            Name der zu verwendeden Build-Configuration
	 * @return vorl‰ufige ID der neuen Anfrage
	 * @throws PlanManagementException
	 */
	public Integer addQuery(ILogicalOperator logicalPlan, ISession user,
			String queryBuildConfigurationName, List<IQueryBuildSetting<?>> overwriteSetting) throws PlanManagementException;

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
			String queryBuildConfigurationName, List<IQueryBuildSetting<?>> overwriteSetting) throws PlanManagementException;

	
	
	/**
	 * getConfiguration liefert die aktuelle Konfiguration der
	 * AUsfuehrungsumgebung.
	 * 
	 * @return die aktuelle Konfiguration der AUsf√ºhrungsumgebung
	 */
	public ExecutionConfiguration getConfiguration();
	
	/**
	 * Get specific query build configuration
	 */
	public IQueryBuildConfiguration getQueryBuildConfiguration(String name);
	
	/**
	 * Get all QueryBuildConfigurations
	 * 
	 * @return all build configuration
	 */
	public Map<String, IQueryBuildConfiguration> getQueryBuildConfigurations();

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
	
	// Facade for Compiler
	public List<ILogicalQuery> translateQuery(String query, String parserID,
			ISession user) throws QueryParseException;

	public IPhysicalQuery transform(ILogicalQuery query,
			TransformationConfiguration transformationConfiguration,
			ISession caller) throws TransformationException;

	IDataDictionary getDataDictionary();

	public boolean removeAllQueries(ISession caller);

	void executionPlanChanged() throws SchedulerException,
			NoSchedulerLoadedException;

	public ISchedulerManager getSchedulerManager();

	public QueryBuildConfiguration getBuildConfigForQuery(ILogicalQuery query);

}
