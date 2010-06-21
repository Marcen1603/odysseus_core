package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public abstract class AbstractPriorityPlanScheduling implements
		IPartialPlanScheduling, ISchedulingEventListener {

	final private PriorityQueue<IScheduling> queue;

	public AbstractPriorityPlanScheduling(
			AbstractPriorityPlanScheduling staticPriorityScheduling) {
		queue = new PriorityQueue<IScheduling>(staticPriorityScheduling.queue);
	}

	public AbstractPriorityPlanScheduling() {
		queue = new PriorityQueue<IScheduling>(10,
				new CurrentPlanPriorityComperator());
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
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
				if (!plan.isSchedulingBlocked() && !plan.isSchedulingPaused()) {
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
	public void removeCurrent() {
		synchronized (queue) {
			queue.poll().removeSchedulingEventListener(this);
		}
	}

	protected void updatePriorityCurrent(int prio) {
		synchronized (queue) {
			IScheduling s = queue.poll();
			s.getPlan().setCurrentPriority(prio);
			queue.add(s);
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
		}
	}

	@Override
	public abstract AbstractPriorityPlanScheduling clone();

	protected abstract void updatePriorities(IScheduling current);

}
