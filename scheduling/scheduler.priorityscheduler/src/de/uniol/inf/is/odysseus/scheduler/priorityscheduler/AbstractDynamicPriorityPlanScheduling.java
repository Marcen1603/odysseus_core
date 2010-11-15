package de.uniol.inf.is.odysseus.scheduler.priorityscheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

abstract public class AbstractDynamicPriorityPlanScheduling implements IPartialPlanScheduling, ISchedulingEventListener {

	protected final List<IScheduling> queue;

	public AbstractDynamicPriorityPlanScheduling() {
		queue = new ArrayList<IScheduling>();
	}

	public AbstractDynamicPriorityPlanScheduling(
			AbstractDynamicPriorityPlanScheduling dynamicPriorityPlanScheduling) {
		queue = new ArrayList<IScheduling>(dynamicPriorityPlanScheduling.queue);
	}

	
	abstract protected void updatePriorities(IScheduling current);
	@Override
	abstract public AbstractDynamicPriorityPlanScheduling clone();
	


	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
			Collections.sort(queue, new CurrentPlanPriorityComperator());
			scheduling.addSchedulingEventListener(this);
		}
	}

	@Override
	public void clear() {
		synchronized (queue) {
			for (IScheduling s:queue){
				s.removeSchedulingEventListener(this);
			}
			queue.clear();
		}
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (queue) {
			for (IScheduling plan : queue) {
				if (!plan.isSchedulingBlocked() && !plan.isSchedulingPaused()
						&& plan.isSchedulable()) {
					updatePriorities(plan);
					return plan;
				}
			}
			return null;
		}
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
			plan.removeSchedulingEventListener(this);
		}
	}

	@Override
	public void nothingToSchedule(IScheduling sched) {
		synchronized (queue) {
			// keine gute Idee -->
			// queue.remove(sched);
		}
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		synchronized (queue) {
			queue.add(sched);
			Collections.sort(queue, new CurrentPlanPriorityComperator());
		}
	}

}
