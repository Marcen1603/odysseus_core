package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * A recovery executor represents a complete recovery strategy by calling
 * certain {@link IRecoveryComponent}s in a certain order.
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
	 *            the configuration for the executor.
	 * @return A new recovery executor of the same class but with set
	 *         configuration.
	 */
	public IRecoveryExecutor newInstance(Properties config);

	/**
	 * Runs the recovery mechanism for given queries.
	 * 
	 * @param queryIds
	 *            The ids of the queries.
	 * @param caller
	 *            The caller is a session for the same user, who backed up the
	 *            data.
	 * @param log
	 *            All system log entries to recover.
	 * @throws Exception
	 *             if any error occurs.
	 */
	public void recover(List<Integer> queryIds, ISession caller, List<ISysLogEntry> log) throws Exception;

	/**
	 * Activates the backup mechanism for given queries.
	 * 
	 * @param qbConfig
	 *            The used query build configuration.
	 * @param caller
	 *            The caller is a session of the user, who wants to back up the
	 *            data.
	 * @param queries
	 *            The queries.
	 * @return {@code queries} either modified for recovery or not. Depends on
	 *         the used recovery strategy.
	 */
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries);

}