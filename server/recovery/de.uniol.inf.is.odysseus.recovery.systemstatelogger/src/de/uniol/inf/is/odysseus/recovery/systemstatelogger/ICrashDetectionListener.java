package de.uniol.inf.is.odysseus.recovery.systemstatelogger;

/**
 * Listeners for crash detections of Odysseus.
 * 
 * @author Michael Brand
 *
 */
public interface ICrashDetectionListener {

	/**
	 * Called after a crash has been detected.
	 * 
	 * @param lastStartup
	 *            The time stamp in nanoseconds of the last logged startup.
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onCrashDetected(long lastStartup) throws Throwable;

}