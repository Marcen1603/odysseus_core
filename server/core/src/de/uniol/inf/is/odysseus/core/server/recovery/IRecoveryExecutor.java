package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * A recovery executor represents a complete non-distributed recovery (NDR)
 * strategy by calling certain {@link IRecoveryComponent}s in a certain order.
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryExecutor {

	/**
	 * Gets the name of the executor.
	 * 
	 * @return A string unique for recovery executors.
	 */
	public String getName();

	/**
	 * Creates a new recovery executor with a given configuration.
	 * 
	 * @param config
	 *            the configuration for the executor. Typically, it is the
	 *            totality of configurations for the
	 *            {@link IRecoveryComponent}s.
	 * @return A new recovery executor instance.
	 */
	public IRecoveryExecutor newInstance(Properties config);

	/**
	 * Runs the NDR mechanism for given queries.
	 * 
	 * @param qbConfig
	 *            The used query build configuration.
	 * @param session
	 *            The session of the user, who wants to recover the data.
	 * @param queries
	 *            The queries to recover.
	 * @param caller
	 *            The executor that called this method.
	 * @return {@code queries} either modified for recovery or not. Depends on
	 *         the used recovery strategy.
	 */
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller);

	/**
	 * Activates the backup mechanism for given queries.
	 * 
	 * @param qbConfig
	 *            The used query build configuration.
	 * @param session
	 *            The session of the user, who wants to back up the data.
	 * @param queries
	 *            The queries to backup.
	 * @param caller
	 *            The executor that called this method.
	 * @return {@code queries} either modified for backup or not. Depends on the
	 *         used recovery strategy.
	 */
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller);

	/**
	 * Checks, if a recovery is needed.
	 * 
	 * @return True, if
	 *         {@link #activateRecovery(QueryBuildConfiguration, ISession, List)}
	 *         should be called.
	 */
	public boolean isRecoveryNeeded();

}