package de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class StaticPriorityPlanScheduling implements IPartialPlanScheduling {

	List<List<IScheduling>> plans = Collections
			.synchronizedList(new ArrayList<List<IScheduling>>());
	HashMap<Integer, Integer> curPositions = new HashMap<Integer, Integer>();

	public StaticPriorityPlanScheduling() {
	}

	@SuppressWarnings("unchecked")
	public StaticPriorityPlanScheduling(
			StaticPriorityPlanScheduling staticPriorityPlanScheduling) {
		this.plans = Collections
				.synchronizedList(new ArrayList<List<IScheduling>>(
						staticPriorityPlanScheduling.plans));
		this.curPositions = (HashMap<Integer, Integer>) staticPriorityPlanScheduling.curPositions
				.clone();
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (plans) {
			ListIterator<List<IScheduling>> li = plans.listIterator();

			int basePriority = scheduling.getPlan().getBasePriority();
			while (li.hasNext()) {
				List<IScheduling> next = li.next();
				int nextBasePriority = next.get(0).getPlan().getBasePriority();
				if (nextBasePriority == basePriority) {
					next.add(scheduling);
					return;
				}
				if (nextBasePriority < basePriority) {
					li.previous();
					break;
				}
			}
			ArrayList<IScheduling> list = new ArrayList<IScheduling>();
			list.add(scheduling);
			li.add(list);
			curPositions.put(basePriority, 0);
		}
	}

	@Override
	public void clear() {
		synchronized (this.plans) {
			this.plans.clear();
			this.curPositions.clear();
		}
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (plans) {
			for (List<IScheduling> scheduling : plans) {
				int basePriority = scheduling.get(0).getPlan()
						.getBasePriority();
				int curPosition = curPositions.get(basePriority);
				int size = scheduling.size();
				for (int i = 0; i < size; ++i) {
					IScheduling curScheduling = scheduling
							.get((i + curPosition) % size);
					if (curScheduling.isSchedulable()) {
						curPositions.put(basePriority, ++curPosition % size);
						return curScheduling;
					}
				}
			}
		}
		return null;
	}

	@Override
	public int planCount() {
		int sum = 0;
		for (List<?> curList : plans) {
			sum += curList.size();
		}
		return sum;
	}

	@Override
	public void removePlan(IScheduling plan) {
		int priority = plan.getPlan().getBasePriority();
		synchronized (plans) {
			for (List<IScheduling> scheduling : plans) {
				if (scheduling.get(0).getPlan().getBasePriority() == priority) {
					scheduling.remove(plan);
					if (scheduling.isEmpty()) {
						this.curPositions.remove(priority);
						plans.remove(scheduling);
					}
					return;
				}
			}
		}
	}

	@Override
	public StaticPriorityPlanScheduling clone() {
		return new StaticPriorityPlanScheduling(this);
	}

}
