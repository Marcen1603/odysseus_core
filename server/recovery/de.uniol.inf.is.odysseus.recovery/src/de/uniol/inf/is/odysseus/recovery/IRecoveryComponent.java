package de.uniol.inf.is.odysseus.recovery;

import java.util.List;

import com.google.common.collect.ImmutableCollection;

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
	 * Gets the dependencies.
	 * 
	 * @return The names of all recovery components, where
	 *         {@link #recover(List)} has to be called before this component.
	 */
	public ImmutableCollection<String> getDependencies();

	/**
	 * Runs the recovery mechanism.
	 * 
	 * @param log
	 *            All entries to recover.
	 * @throws Exception
	 *             if any error occurs.
	 */
	public void recover(List<ISysLogEntry> log) throws Exception;

	/**
	 * Activates the backup mechanism for given queries.
	 * 
	 * @param queryIds
	 *            The ids of the queries.
	 */
	public void activateBackup(List<Integer> queryIds);

}