package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple utility class for executing code after a timeout
 * @author Thore Stratmann
 *
 */
public abstract class TimeoutTask extends TimerTask{
	private volatile boolean active = true;
	
	public TimeoutTask() {
		
	}


	/**
	 * This method is executed after timeout
	 */
	public abstract void runAfterTimeout();
	
	@Override
	public void run() {
		if (isActive()) {
			runAfterTimeout();
		}
	}

	/**
	 * Starts the TimeoutTask
	 * @param delayUntilTimeout delay in milliseconds until timeout
	 */
	public void start(long delayUntilTimeout) {
		Timer timer = new Timer();
		timer.schedule(this,delayUntilTimeout);		
	}
	
	/**
	 * Returns whether the task will be executed after timeout
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets whether the task should be executed after timeout
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
