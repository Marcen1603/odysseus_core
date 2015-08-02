package de.uniol.inf.is.odysseus.recovery;

import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

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
	 * @param queryIds
	 *            The ids of the queries.
	 * @param caller
	 *            The caller is a session of the user, who wants to back up the
	 *            data.
	 */
	public void activateBackup(List<Integer> queryIds, ISession caller);

}