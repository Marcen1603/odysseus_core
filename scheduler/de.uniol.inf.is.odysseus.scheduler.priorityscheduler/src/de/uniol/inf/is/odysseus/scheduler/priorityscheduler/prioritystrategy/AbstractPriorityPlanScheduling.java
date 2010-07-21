package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import java.util.ArrayList;
import java.util.Collections;

import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public abstract class AbstractPriorityPlanScheduling implements
		IPartialPlanScheduling, ISchedulingEventListener {

	final protected ArrayList<IScheduling> queue;

	public AbstractPriorityPlanScheduling(
			AbstractPriorityPlanScheduling staticPriorityScheduling) {
		queue = new ArrayList<IScheduling>(staticPriorityScheduling.queue);
	}

	public AbstractPriorityPlanScheduling() {
		queue = new ArrayList<IScheduling>();
	}

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

	protected void updatePriorityCurrent(IScheduling current, int prio) {
		synchronized (queue) {
			current.getPlan().setCurrentPriority(prio);
			Collections.sort(queue, new CurrentPlanPriorityComperator());
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

	@Override
	public abstract AbstractPriorityPlanScheduling clone();

	protected abstract void updatePriorities(IScheduling current);

}
