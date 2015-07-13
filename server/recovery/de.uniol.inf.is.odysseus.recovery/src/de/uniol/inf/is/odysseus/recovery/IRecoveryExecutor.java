package de.uniol.inf.is.odysseus.recovery;

import java.util.List;

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
	 * Runs the recovery mechanism for given queries.
	 * 
	 * @param queryIds
	 *            The ids of the queries.
	 * @throws Exception
	 *             if any error occurs.
	 */
	public void recover(List<Integer> queryIds) throws Exception;

	/**
	 * Activates the backup mechanism for given queries.
	 * 
	 * @param queryIds
	 *            The ids of the queries.
	 */
	public void activateBackup(List<Integer> queryIds);

}