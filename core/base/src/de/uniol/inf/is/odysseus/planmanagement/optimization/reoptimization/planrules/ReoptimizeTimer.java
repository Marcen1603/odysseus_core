package de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.planmanagement.plan.AbstractPlanReoptimizeRule;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPlan;

/**
 * ReoptimizeTimer is a reoptimze rule. After a defined time an reoptimize
 * request is send.
 * 
 * @author Wolf Bauer, Tobias Witt
 * 
 */
public class ReoptimizeTimer extends AbstractPlanReoptimizeRule implements
		Runnable {

	private long period;
	private Thread thread;

	/**
	 * Creates a new time rule with a specific time period.
	 * 
	 * @param period
	 *            Period which defines after which time a reoptimization is
	 *            fired.
	 */
	public ReoptimizeTimer(long time) {
		this.period = time;
		this.reoptimizable = new ArrayList<IPlan>();
		this.thread = new Thread(this);
		this.thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(this.period);
				fireReoptimizeEvent();
			}
		} catch (InterruptedException e) {
			//end
		}
	}

	@Override
	public void deinitialize() {
		this.thread.interrupt();
	}
}
