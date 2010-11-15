package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

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
		this.planList = new ArrayList(other.planList);
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
			synchronized (pausedPlans) {
				while (planCount() == pausedPlans.size()) {
					try {
						logger.debug(this + " paused");
						pausedPlans.wait(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			while (planIterator.hasNext()) {
				currentPlan = planIterator.next();
				if (currentPlan.isSchedulable()) {
					return currentPlan;
				}
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