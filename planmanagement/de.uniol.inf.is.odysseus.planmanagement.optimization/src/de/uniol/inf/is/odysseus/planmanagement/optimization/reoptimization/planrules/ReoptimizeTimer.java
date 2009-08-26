/**
 * 
 */
package de.uniol.inf.is.odysseus.planmanagement.optimization.reoptimization.planrules;

import de.uniol.inf.is.odysseus.base.planmanagement.plan.AbstractPlanReoptimizeRule;

/**
 * ReoptimizeTimer
 * @author Wolf Bauer
 *
 */
public class ReoptimizeTimer extends AbstractPlanReoptimizeRule implements Runnable {
	
	private boolean running = true;
	
	private float curTime = 0;
	
	private float stopTime = 0;
	
	public ReoptimizeTimer(float time) {
		this.stopTime = time;
		Thread thread = new Thread(this);
		thread.start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(this.running) {
			this.curTime++;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(curTime >= this.stopTime) {
				fireReoptimizeEvent();
				curTime = 0;
			}
		}
	}
}
