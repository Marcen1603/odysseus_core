package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.List;

import javax.security.auth.login.Configuration;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.ICompilerListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;

/**
 * This Interface contains all methods from the executor that are accessable if
 * the executor is used on a server
 * 
 * @author Marco Grawunder
 *
 */
public interface IServerExecutor extends IExecutor, IPlanScheduling, IOptimizable, IPlanManager {
	
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

	@Override
	List<IQuery> getQueries();

	// IUserManagement getUserManagement();
	// ISessionManagement getSessionManagement();
	IDataDictionary getDataDictionary();


}
