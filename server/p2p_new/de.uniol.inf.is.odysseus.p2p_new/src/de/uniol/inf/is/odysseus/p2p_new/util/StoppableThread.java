package de.uniol.inf.is.odysseus.p2p_new.util;

public abstract class StoppableThread implements Runnable {

	private boolean isRunning = true;

	public void stopRunning() {
		isRunning = false;
	}

	public final boolean isRunning() {
		return isRunning;
	}
}
