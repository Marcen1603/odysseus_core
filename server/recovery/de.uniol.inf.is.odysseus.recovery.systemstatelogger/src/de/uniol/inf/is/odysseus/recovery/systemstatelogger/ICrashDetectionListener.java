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
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onCrashDetected() throws Throwable;

}