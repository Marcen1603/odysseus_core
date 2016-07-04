package de.uniol.inf.is.odysseus.recovery.checkpointing;

import java.io.Serializable;

/**
 * Entity to manage checkpoints. After a checkpoint manager is configured
 * ({@link #newInstance(CheckpointUnit, long)}) and started (
 * {@link #start(long)}, it notifies listeners (
 * {@link #addListener(ICheckpointListener)}), if a checkpoint is reached.
 * 
 * @author Michael Brand
 *
 */
public interface ICheckpointManager extends Serializable {

	/**
	 * Gets the name.
	 */
	public String getName();

	/**
	 * Creates a new checkpoint manager.
	 * 
	 * @param unit
	 *            The unit to use to determine, when to set checkpoints.
	 * @param period
	 *            The amount of {@code units} between two checkpoints.
	 * @return A new created checkpoint manager ready to use.
	 */
	public ICheckpointManager newInstance(CheckpointUnit unit, long period) throws Exception;

	/**
	 * Creates a new checkpoint manager as a copy of this one. <br />
	 * Make sure, that unit and period are set by
	 * {@link #newInstance(CheckpointUnit, long)}.
	 */
	public ICheckpointManager clone();

	/**
	 * Adds a listener.
	 */
	public void addListener(ICheckpointListener listener);

	/**
	 * Removes a listener
	 */
	public void removeListener(ICheckpointListener listener);

	/**
	 * Gets the unit for checkpoints.
	 */
	public CheckpointUnit getUnit();

	/**
	 * Gets the period between two checkpoints.
	 */
	public long getPeriod();

	/**
	 * Starts the manager.
	 */
	public void start();

	/**
	 * Stops the manager.
	 */
	public void stop();

	/**
	 * Adds a logical query to handle. <br />
	 * All added queries will be suspended, if a checkpoint is reached and
	 * resumed, if all handler finished.
	 * 
	 * @param id
	 *            The id of the logical query.
	 */
	public void addLogicalQuery(int id);

}