package de.uniol.inf.is.odysseus.core.server.recovery;

import java.util.List;
import java.util.Properties;

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
	 * Gets the name of the component.
	 * 
	 * @return A string unique for recovery components.
	 */
	public String getName();

	/**
	 * Creates a new recovery component with a given configuration.
	 * 
	 * @param config
	 *            the configuration for the component.
	 * @return A new recovery component of the same class but with set
	 *         configuration.
	 */
	public IRecoveryComponent newInstance(Properties config);

	/**
	 * Runs the recovery mechanism for given queries.
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
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries);

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
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries);

}