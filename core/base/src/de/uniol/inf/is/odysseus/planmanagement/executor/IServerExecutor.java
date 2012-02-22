package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.Configuration;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.SchedulerException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;
import de.uniol.inf.is.odysseus.scheduler.manager.ISchedulerManager;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * This Interface contains all methods from the executor that are accessable if
 * the executor is used on a server
 * 
 * @author Marco Grawunder
 * 
 */
public interface IServerExecutor extends IExecutor, IPlanScheduling,
		IPlanManager, IErrorEventHandler, IErrorEventListener {
	
	public Collection<ILogicalQuery> addQuery(String query, String parserID,
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
	public IPhysicalQuery addQuery(ILogicalOperator logicalPlan, ISession user,
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
	public IPhysicalQuery addQuery(List<IPhysicalOperator> physicalPlan, ISession user,
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

	IDataDictionary getDataDictionary();

	public void removeAllQueries(ISession caller);

	void executionPlanChanged() throws SchedulerException,
			NoSchedulerLoadedException;

	public ISchedulerManager getSchedulerManager();

}
