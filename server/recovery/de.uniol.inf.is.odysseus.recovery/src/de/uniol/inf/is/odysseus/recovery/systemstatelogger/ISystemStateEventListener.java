package de.uniol.inf.is.odysseus.recovery.systemstatelogger;

/**
 * Listener for system state events of Odysseus.
 * 
 * @author Michael Brand
 *
 */
public interface ISystemStateEventListener {

	/**
	 * Called after Odysseus has been started successful.
	 * 
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onSystemStartup() throws Throwable;

	/**
	 * Called after Odysseus has been shut down successful.
	 * 
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onSystemShutdown() throws Throwable;

	/**
	 * Called after a crash has been detected.
	 * 
	 * @param lastStartup
	 *            The time stamp in milliseconds of the last logged startup.
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onCrashDetected(long lastStartup) throws Throwable;

}