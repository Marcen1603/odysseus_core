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
package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;

public class RoundRobinPlanScheduling implements IPartialPlanScheduling,
		ISchedulingEventListener, IClone {

	Logger logger = LoggerFactory.getLogger(RoundRobinPlanScheduling.class);

	final private List<IScheduling> planList;
	final private Set<IScheduling> pausedPlans;
	private Iterator<IScheduling> planIterator = null;

	private IScheduling currentPlan;
	
	public RoundRobinPlanScheduling() {
		planList = new ArrayList<IScheduling>();
		pausedPlans = new HashSet<IScheduling>();
	}

	public RoundRobinPlanScheduling(RoundRobinPlanScheduling other) {
		this.planList = new ArrayList<IScheduling>(other.planList);
		this.pausedPlans = new HashSet<IScheduling>(other.pausedPlans);
	}

	@Override
	public void addPlan(IScheduling plan) {
		planList.add(plan);
		plan.addSchedulingEventListener(this);
		planIterator = null;
	}

	@Override
	public void clear() {
		planIterator = null;
		for (IScheduling plan: planList){
			plan.removeSchedulingEventListener(this);
		}
		pausedPlans.clear();
		planList.clear();
	}

	@Override
	public void removePlan(IScheduling plan) {
		if (planIterator != null && plan == currentPlan) {
			planIterator.remove();
			currentPlan.removeSchedulingEventListener(this);
		}
	}

	@Override
	public int planCount() {
		return planList.size();
	}

	@Override
	public IScheduling nextPlan() {
		if (planIterator == null || !planIterator.hasNext()) {
			planIterator = planList.iterator();
		}
		if (planIterator.hasNext()) {
//			synchronized (pausedPlans) {
//				while (planCount() == pausedPlans.size()) {
//					try {
//						logger.debug(this + " paused");
//						pausedPlans.wait(10);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
			while (planIterator.hasNext()) {
				currentPlan = planIterator.next();
//				if (currentPlan.isSchedulable()) {
					return currentPlan;
//				}
			}
		}
		return null;
	}

	@Override
	public void nothingToSchedule(IScheduling sched) {
		pausedPlans.add(sched);
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		pausedPlans.remove(sched);
		synchronized (pausedPlans) {
			pausedPlans.notifyAll();
		}
	}

	@Override
	public RoundRobinPlanScheduling clone() {
		return new RoundRobinPlanScheduling(this);
	}

}