package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.Set;

import javax.security.auth.login.Configuration;

import de.uniol.inf.is.odysseus.monitoring.ISystemMonitor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoOptimizerLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoSystemMonitorLoadedException;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.OptimizationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.exception.QueryOptimizationException;

/**
 * IAdvancedExecutor describes an advanced {@link IExecutor}. IAdvancedExecutor
 * provides methods to get all available buffer placement strategies, scheduling
 * strategy factories and scheduler factories. It also provides Methods to set
 * the buffer placement strategy, the scheduling strategy factory and the
 * scheduler factory which should be used.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public interface IAdvancedExecutor extends IExecutor {
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
