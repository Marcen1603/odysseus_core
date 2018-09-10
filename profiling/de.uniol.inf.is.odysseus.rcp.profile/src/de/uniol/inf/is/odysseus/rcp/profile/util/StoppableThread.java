package de.uniol.inf.is.odysseus.rcp.profile.util;

public class StoppableThread extends Thread {

	private boolean isRunning = true;

	public void stopRunning() {
		isRunning = false;
	}

	protected final boolean isRunning() {
		return isRunning;
	}
}
