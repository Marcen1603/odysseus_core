package de.uniol.inf.is.odysseus.p2p_new.util;

public class StoppableThread extends Thread {

	private boolean isRunning = true;

	public void stopRunning() {
		isRunning = false;
	}

	protected final boolean isRunning() {
		return isRunning;
	}
}
