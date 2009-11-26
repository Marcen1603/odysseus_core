package de.uniol.inf.is.odysseus.scheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.base.planmanagement.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

/**
 * IScheduler describes a scheduler for scheduling physical plans. A scheduler
 * uses {@link IPartialPlan} and {@link IIterableSource}. It can be started and
 * stopped.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IScheduler extends IErrorEventHandler {

	/**
	 * Start scheduling of the registered physical plan.
	 */
	public void startScheduling();

	/**
	 * Stop scheduling of the registered physical plan.
	 */
	public void stopScheduling();

	/**
	 * TODO: Sollte beschreiben, der sich damit auskennt. (Wolf)
	 * 
	 * @param time
	 */
	public void setTimeSlicePerStrategy(long time);

	/**
	 * Signalizes if this scheduler is started oder not.
	 * 
	 * @return TRUE: Scheduler is started. FALSE: Scheduler is stopped.
	 */
	public boolean isRunning();

	/**
	 * Set the global sources for scheduling (no pipes).
	 * 
	 * @param sources
	 *            Global sources for scheduling (no pipes).
	 */
	public void setSources(List<IIterableSource<?>> sources);

	/**
	 * Set the partial plans for scheduling (pipes and roots).
	 * 
	 * @param partialPlans
	 *            Partial plans for scheduling (pipes and roots).
	 */
	public void setPartialPlans(List<IPartialPlan> partialPlans);

	/**
	 * Get the partial plans for scheduling (pipes and roots).
	 * 
	 * @return Registered partial plans for scheduling (pipes and roots).
	 */
	public List<IPartialPlan> getPartialPlans();
}
