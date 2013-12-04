package de.uniol.inf.is.odysseus.scheduler.slascheduler;

public class SLAHelper {
	private static long heartbeatInterval;
	private static boolean testWorkaroundEnabled;

	/**
	 * @return the heartbeatInterval of the AssureHeartbeat operator in milliseconds
	 */
	public static long getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/**
	 * @param heartbeatInterval the heartbeatInterval of the AssureHeartbeat operator in milliseconds to set
	 */
	public static void setHeartbeatInterval(long heartbeatInterval) {
		SLAHelper.heartbeatInterval = heartbeatInterval;
	}

	/**
	 * Enables the test workaround for checking the slas and persisting the costs without the dependence on the scheduler 
	 * @return the testWorkaroundEnabled
	 */
	public static boolean isTestWorkaroundEnabled() {
		return testWorkaroundEnabled;
	}

	/**
	 * Enables the test workaround for checking the slas and persisting the costs without the dependence on the scheduler 
	 * @param testWorkaroundEnabled the testWorkaroundEnabled to set
	 */
	public static void setTestWorkaroundEnabled(boolean testWorkaroundEnabled) {
		SLAHelper.testWorkaroundEnabled = testWorkaroundEnabled;
	}
	
}
