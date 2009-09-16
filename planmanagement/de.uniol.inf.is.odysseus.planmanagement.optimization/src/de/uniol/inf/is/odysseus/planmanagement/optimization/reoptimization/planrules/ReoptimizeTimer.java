/**
 * 
 */
package de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.AbstractPlanReoptimizeRule;

/**
 * ReoptimizeTimer is a reoptimze rule. After a defined time an reoptimize
 * request is send.
 * 
 * @author Wolf Bauer
 * 
 */
public class ReoptimizeTimer extends AbstractPlanReoptimizeRule implements
		Runnable {

	/**
	 * Is the timer thread running.
	 */
	private boolean running = true;

	/**
	 * Current time stamp.
	 */
	private float curTime = 0;

	/**
	 * Stop time which defines when a request is send.
	 */
	private float stopTime = 0;

	/**
	 * Creates a new time rule with a specific time stamp.
	 * 
	 * @param time
	 *            Time which defines when a request is send.
	 */
	public ReoptimizeTimer(float time) {
		this.stopTime = time;
		Thread thread = new Thread(this);
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// while this Thread is running
		while (this.running) {
			this.curTime++;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// if current tim greater the stop time send request
			if (curTime >= this.stopTime) {
				fireReoptimizeEvent();
				// begin anew
				curTime = 0;
			}
		}
	}
}
