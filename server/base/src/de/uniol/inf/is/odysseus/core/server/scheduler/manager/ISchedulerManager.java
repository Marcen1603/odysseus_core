/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.scheduler.manager;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.event.IEventHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IInfoProvider;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.exception.NoSchedulerLoadedException;

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
	 * TODO: sollte jemand erkl�ren der Ahnung davon hat (Wolf).
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
	public void refreshScheduling(IExecutionPlan executivePlan)
			throws NoSchedulerLoadedException;

	
	/**
	 * Adds a new query to schedule
	 * @param affectedQuery
	 */
	public void addQuery(IPhysicalQuery affectedQuery);

	/**
	 * Removes a new query from scheduling
	 * @param affectedQuery
	 */
	public void removeQuery(IPhysicalQuery affectedQuery);

	/**
	 * Event, that this query is started
	 * @param affectedQuery
	 */
	public void startedQuery(IPhysicalQuery affectedQuery);

	/**
	 * Event, that this query is stopped
	 * @param affectedQuery
	 */
	public void stoppedQuery(IPhysicalQuery affectedQuery);
	
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
			String activeSchedulingStrategy, IExecutionPlan execPlan);

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
