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
package de.uniol.inf.is.odysseus.scheduler.priorityscheduler;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPhysicalQueryScheduling;

abstract public class AbstractDynamicPriorityPlanScheduling implements
		IPhysicalQueryScheduling {

	final CurrentPlanPriorityComperator comperator = new CurrentPlanPriorityComperator();

	private final List<IScheduling> queue;
	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();

	public AbstractDynamicPriorityPlanScheduling() {
		queue = new LinkedList<IScheduling>();
	}

	public AbstractDynamicPriorityPlanScheduling(
			AbstractDynamicPriorityPlanScheduling dynamicPriorityPlanScheduling) {
		queue = new LinkedList<IScheduling>(dynamicPriorityPlanScheduling.queue);
	}

	abstract protected void updatePriority(IScheduling current);

	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
			// Init with Base Priority
			scheduling.getPlan().setCurrentPriority(scheduling.getPlan().getBasePriority());
		}
	}

	@Override
	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (lastRun) {
			if (lastRun.size() > 0) {
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
		synchronized (queue) {
			Collections.sort(queue, comperator);
			Iterator<IScheduling> iter = queue.iterator();
			synchronized (lastRun) {
				lastRun.add(iter.next());
				long prio = queue.get(0).getPlan().getCurrentPriority();
				while (iter.hasNext()) {
					IScheduling s = iter.next();
					if (s.getPlan().getCurrentPriority() == prio) {
						lastRun.add(s);
					} else {
						break;
					}
				}
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
	}
	
	

	protected IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		updatePriority(toSchedule);
		return toSchedule;
	}

	@Override
	public int planCount() {
		synchronized (queue) {
			return queue.size();
		}
	}

	@Override
	public void removePlan(IScheduling plan) {
		synchronized (queue) {
			queue.remove(plan);
		}
	}

	@Override
	abstract public IPhysicalQueryScheduling clone();

}
