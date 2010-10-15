package de.uniol.inf.is.odysseus.scheduler.manager;

import java.util.Set;

import de.uniol.inf.is.odysseus.event.IEventHandler;
import de.uniol.inf.is.odysseus.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.exception.NoSchedulerLoadedException;

/**
 * Describes an object which manages the scheduling of the execution plan.
 * ISchedulerManager is the scheduling module of the odysseus. This interface is
 * used for scheduling services.
 * 
 * A manager can handle many scheduler, scheduler factories and scheduling
 * strategy factories. It defines which scheduler are active and distributes the
 * execution plan for scheduling.
 * 
 * @author Wolf Bauer
 * 
 */
public interface ISchedulerManager extends IInfoProvider, IErrorEventHandler,
		IErrorEventListener, IEventHandler {
	/**
	 * Start scheduling of the registered physical plan.
	 * 
	 * @throws NoSchedulerLoadedException
	 *             An {@link Exception} occurs because no scheduler is loaded.
	 * @throws OpenFailedException
	 *             An {@link Exception} occurs while opening a physical
	 *             opertaor.
	 */
	public void startScheduling() throws NoSchedulerLoadedException,
			OpenFailedException;

	/**
	 * Stop scheduling of the registered physical plan.
	 * 
	 * @throws NoSchedulerLoadedException
	 *             An {@link Exception} occurs because no scheduler is loaded.
	 */
	public void stopScheduling() throws NoSchedulerLoadedException;

	/**
	 * TODO: sollte jemand erklären der Ahnung davon hat (Wolf).
	 * 
	 * @param time
	 * @throws NoSchedulerLoadedException
	 *             An {@link Exception} occurs because no scheduler is loaded.
	 */
	public void setTimeSlicePerStrategy(long time)
			throws NoSchedulerLoadedException;

	/**
	 * Indicates if the scheduling is started.
	 * 
	 * @return TRUE: Scheduling is started. FALSE: Scheduling is stopped.
	 * @throws NoSchedulerLoadedException
	 *             An {@link Exception} occurs because no scheduler is loaded.
	 */
	public boolean isRunning() throws NoSchedulerLoadedException;

	/**
	 * Sets the new execution plan which should be scheduled.
	 * 
	 * @param executivePlan
	 *            New execution plan which should be scheduled.
	 * @throws NoSchedulerLoadedException
	 *             An {@link Exception} occurs because no scheduler is loaded.
	 */
	public void refreshScheduling(IScheduleable executivePlan)
			throws NoSchedulerLoadedException;

	/**
	 * Sets how many scheduler should be used.
	 * 
	 * @param schedulerCount
	 *            How many scheduler should be used.
	 */
	public void setSchedulerCount(int schedulerCount);

	/**
	 * Returns how many scheduler are used.
	 * 
	 * @return How many scheduler are used.
	 */
	public int getSchedulerCount();

	/**
	 * Returns a set of all schedulers which are registered. A Scheduler is
	 * represented with an ID.
	 * 
	 * @return Set of all schedulers which are registered. A Scheduler is
	 *         represented with an ID.
	 */
	public Set<String> getScheduler();

	/**
	 * Returns a set of all scheduling strategies which are registered. A
	 * scheduling strategy is represented with an ID.
	 * 
	 * @return Set of all scheduling strategies which are registered. A
	 *         scheduling strategy is represented with an ID.
	 */
	public Set<String> getSchedulingStrategy();

	/**
	 * Set the active scheduler. This scheduler is used for scheduling the
	 * execution plan.
	 * 
	 * TODO: So nur sinnvoll, wenn nur ein Scheduler gleichzeitig ... sollte
	 * aber Standardfall sein ...
	 * 
	 * @param activeScheduler
	 *            ID of the scheduler which should be used.
	 * @param activeSchedulingStrategy
	 *            ID of the scheduling strategy which should be used.
	 * @param scheduleInfos
	 *            Object with informations about the scheduling.
	 */
	public void setActiveScheduler(String activeScheduler,
			String activeSchedulingStrategy, IScheduleable scheduleInfos);

	/**
	 * Returns the active scheduler as an ID.
	 * 
	 * @return The active scheduler as an ID.
	 */
	public String getActiveSchedulerID();

	/**
	 * Returns the active scheduler
	 * 
	 * @return The active scheduler
	 */
	public IScheduler getActiveScheduler();

	/**
	 * Returns the active scheduling strategy as an ID.
	 * 
	 * @return The active scheduling strategy as an ID.
	 */
	public String getActiveSchedulingStrategyID();
	
	public void schedulingsChanged();
}
