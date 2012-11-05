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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.AbstractPartialPlanScheduling;

public class RoundRobinPlanScheduling extends AbstractPartialPlanScheduling implements
		IClone {

	Logger logger = LoggerFactory.getLogger(RoundRobinPlanScheduling.class);
	private ReentrantLock lock = new ReentrantLock();

	final private List<IScheduling> planList;
	private Iterator<IScheduling> planIterator = null;

	public RoundRobinPlanScheduling() {
		planList = new CopyOnWriteArrayList<IScheduling>();
	}

	public RoundRobinPlanScheduling(RoundRobinPlanScheduling other) {
		this.planList = new CopyOnWriteArrayList<IScheduling>(other.planList);
	}

	@Override
	public void addPlan(IScheduling plan) {
		super.addPlan(plan);
		lock.lock();
		planList.add(plan);
		planIterator = null;
		lock.unlock();
	}

	@Override
	public void clear() {
		super.clear();
		lock.lock();
		planIterator = null;
		for (IScheduling plan : planList) {
			plan.removeSchedulingEventListener(this);
		}
		planList.clear();
		lock.unlock();
	}

	@Override
	public void removePlan(IScheduling plan) {
		planList.remove(plan);
		plan.removeSchedulingEventListener(this);
	}

	@Override
	public int planCount() {
		return planList.size();
	}

	@Override
	public IScheduling nextPlan() {
		checkPausedPlans();

		IScheduling returnValue = null;

		lock.lock();
		if (planIterator == null || !planIterator.hasNext()) {
			planIterator = planList.iterator();
		}
		if (planIterator.hasNext()) {
			returnValue = planIterator.next();
		}
		lock.unlock();
		return returnValue;
	}


	@Override
	public RoundRobinPlanScheduling clone() {
		return new RoundRobinPlanScheduling(this);
	}

}