package de.uniol.inf.is.odysseus.scheduler;

import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

/**
 * Interface to send scheduling events
 * @author Marco Grawunder
 *
 */

public interface ISchedulingEventListener {

	/**
	 * Called if the scheduling for sched is started
	 * @param sched
	 */
	public void scheddulingPossible(IScheduling sched);
	public void nothingToSchedule(IScheduling sched);	
}
