package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import java.util.ArrayList;
import java.util.Collections;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SimpleDynamicPriorityPlanScheduling implements IPartialPlanScheduling, ISchedulingEventListener {

	private static final CurrentPlanPriorityComperator CURRENT_PLAN_PRIORITY_COMPERATOR = new CurrentPlanPriorityComperator();
	final int minPrio;
	protected final ArrayList<IScheduling> queue;

	public SimpleDynamicPriorityPlanScheduling(int minPrio) {
		this.minPrio = minPrio;
		queue = new ArrayList<IScheduling>();
	}

	public SimpleDynamicPriorityPlanScheduling(
			SimpleDynamicPriorityPlanScheduling dynamicPriorityPlanScheduling) {
		minPrio = dynamicPriorityPlanScheduling.minPrio;
		queue = new ArrayList<IScheduling>(dynamicPriorityPlanScheduling.queue);
	}

	@Override
	public SimpleDynamicPriorityPlanScheduling clone() {
		return new SimpleDynamicPriorityPlanScheduling(this);
	}

	
	protected void updatePriorities(IScheduling current) {
		int currentPriority = current.getPlan().getCurrentPriority();
		int newPrio = currentPriority - 1;
		if (newPrio < minPrio) {
			newPrio = current.getPlan().getBasePriority();
		}
		if (newPrio != currentPriority) {
			updatePriorityCurrent(current, newPrio);
		}
	}

	protected void updatePriorityCurrent(IScheduling current, int prio) {
		synchronized (queue) {
			IPartialPlan curPlan = current.getPlan();
			int currentPriority = curPlan.getCurrentPriority();
			curPlan.setCurrentPriority(prio);
			int pos = queue.indexOf(current);
			int nextPos = pos;
			if (prio < currentPriority) {
				// TODO binary search sinnvoll, aber findet das element nicht
				// ...
				while (++nextPos < queue.size()) {
					if (queue.get(nextPos).getPlan().getCurrentPriority() > prio) {
						Collections.swap(queue, pos++, nextPos);
					} else {
						break;
					}
				}
			} else {
				while (--nextPos >= 0) {
					if (queue.get(nextPos).getPlan().getCurrentPriority() < prio) {
						System.out.println("SSSSSSSSSSWWWWWWWWWAPPPPPP BACK");
						Collections.swap(queue, pos--, nextPos);
					} else {
						break;
					}
				}
			}
		}
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
