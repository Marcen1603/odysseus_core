package de.uniol.inf.is.odysseus.net.rcp.util;

public abstract class StoppableThread implements Runnable {

	private boolean isRunning = true;

	public void stopRunning() {
		isRunning = false;
	}

	public final boolean isRunning() {
		return isRunning;
	}
}
