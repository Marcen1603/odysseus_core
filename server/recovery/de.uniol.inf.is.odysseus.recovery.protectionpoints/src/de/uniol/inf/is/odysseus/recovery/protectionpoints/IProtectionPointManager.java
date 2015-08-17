package de.uniol.inf.is.odysseus.recovery.protectionpoints;

/**
 * Entity to manage protection points. After a protection point manager is
 * configured ({@link #newInstance(ProtectionPointUnit, long)}) and started (
 * {@link #start(long)}, it notifies listeners (
 * {@link #addHandler(IProtectionPointHandler)}), if a protection point is
 * reached.
 * 
 * @author Michael Brand
 *
 */
public interface IProtectionPointManager {

	/**
	 * Gets the name.
	 * 
	 * @return A string to implement the protection point manager
	 *         implementation.
	 */
	public String getName();

	/**
	 * Creates a new protection point manager.
	 * 
	 * @param unit
	 *            The unit to use to determine, when to set protection points.
	 * @param period
	 *            The amount of {@code units} between two protection points.
	 * @return A new created protection point manager ready to use.
	 * @throws NullPointerException
	 *             if {@code unit} is null.
	 * @throws IllegalArgumentException
	 *             if {@code period} is not greater than 0.
	 */
	public IProtectionPointManager newInstance(ProtectionPointUnit unit,
			long period) throws NullPointerException, IllegalArgumentException;

	/**
	 * Creates a new protection point manager as a copy of this one. <br />
	 * Make sure, that unit and period are set by
	 * {@link #newInstance(ProtectionPointUnit, long)}.
	 * 
	 * @return A copy of this protection manager.
	 */
	public IProtectionPointManager clone();

	/**
	 * Adds a listener.
	 * 
	 * @param handler
	 *            A listener to call, if a protection point is reached.
	 */
	public void addHandler(IProtectionPointHandler handler);

	/**
	 * Removes a listener
	 * 
	 * @param handler
	 *            A listener to call, if a protection point is reached.
	 */
	public void removeHandler(IProtectionPointHandler handler);

	/**
	 * Gets the unit for protection points.
	 * 
	 * @return The unit to use to determine, when to set protection points.
	 */
	public ProtectionPointUnit getUnit();

	/**
	 * Gets the period between two protection points.
	 * 
	 * @return The amount of {@code units} between tweo protection points.
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

}