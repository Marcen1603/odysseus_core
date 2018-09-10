package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * A recovery component handles the backup and recovery of certain information
 * (e.g., installed queries).
 * 
 * @author Michael Brand
 *
 */
public interface IRecoveryComponent {

	/**
	 * Initializes the component with a given configuration.
	 * 
	 * @param config
	 *            the configuration for the component.
	 */
	public void initialize(Properties config);

	/**
	 * Runs the recovery mechanism for given queries.
	 * 
	 * @param qbConfig
	 *            The used query build configuration.
	 * @param session
	 *            The session of the user, who wants to back up the data.
	 * @param queries
	 *            The queries.
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
	 *            The queries.
	 * @param caller
	 *            The executor that called this method.
	 * @return {@code queries} either modified for recovery or not. Depends on
	 *         the used recovery strategy.
	 */
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller);

}