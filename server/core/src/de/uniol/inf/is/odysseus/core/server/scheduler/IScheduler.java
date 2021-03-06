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
package de.uniol.inf.is.odysseus.core.server.scheduler;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.event.IEventHandler;
import de.uniol.inf.is.odysseus.core.server.event.error.IErrorEventHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * IScheduler describes a scheduler for scheduling physical plans. A scheduler
 * uses {@link IPhysicalQuery} and {@link IIterableSource}. It can be started and
 * stopped.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IScheduler extends IErrorEventHandler, IEventHandler {

	/**
	 * Start scheduling of the registered physical plan.
	 */
	void startScheduling();

	/**
	 * Stop scheduling of the registered physical plan.
	 */
	void stopScheduling();

	/**
	 * TODO: Sollte beschreiben, der sich damit auskennt. (Wolf)
	 * 
	 * @param time
	 */
	void setTimeSlicePerStrategy(long time);

	/**
	 * Signalizes if this scheduler is started oder not.
	 * 
	 * @return TRUE: Scheduler is started. FALSE: Scheduler is stopped.
	 */
	boolean isRunning();

	/**
	 * Set the global sources for scheduling (no pipes). Clears all existing sources
	 * 
	 * @param sources
	 *            Global sources for scheduling (no pipes).
	 */
	void setLeafSources(List<IIterableSource<?>> sources);
	
	void addLeafSources(List<IIterableSource<?>> leafSources);

	List<IIterableSource<?>> getLeafSources();

	/**
	 * Set the partial plans for scheduling (pipes and roots).
	 * 
	 * @param partialPlans
	 *            Partial plans for scheduling (pipes and roots).
	 */
	void setPartialPlans(Collection<IPhysicalQuery> partialPlans);

	Collection<IPhysicalQuery> getPartialPlans();

	void addPartialPlan(IPhysicalQuery query);

	void removePartialPlan(IPhysicalQuery affectedQuery);
	
	void removeLeafSources(List<IIterableSource<?>> sources);

	void clear();

	
	
}
