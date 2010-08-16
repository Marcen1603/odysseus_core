package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import java.util.Collections;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SimpleDynamicPriorityPlanScheduling extends
		AbstractPriorityPlanScheduling {

	private static final CurrentPlanPriorityComperator CURRENT_PLAN_PRIORITY_COMPERATOR = new CurrentPlanPriorityComperator();
	final int minPrio;

	public SimpleDynamicPriorityPlanScheduling(int minPrio) {
		this.minPrio = minPrio;
	}

	public SimpleDynamicPriorityPlanScheduling(
			SimpleDynamicPriorityPlanScheduling dynamicPriorityPlanScheduling) {
		super(dynamicPriorityPlanScheduling);
		minPrio = dynamicPriorityPlanScheduling.minPrio;
	}

	@Override
	public SimpleDynamicPriorityPlanScheduling clone() {
		return new SimpleDynamicPriorityPlanScheduling(this);
	}

	@Override
	/**
	 * This Strategy reduces plan priority if element got scheduled
	 */
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

	@Override
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
						Collections.swap(queue, pos--, nextPos);
					} else {
						break;
					}
				}
			}
		}
	}

}
