package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SLAPartialPlanScheduling implements IPartialPlanScheduling {
	
	private int trainSize;
	
	private List<ISLAViolationEventListener> listeners;
	
	private List<IScheduling> plans;
	
	public SLAPartialPlanScheduling() {
		this.plans = new ArrayList<IScheduling>();
		this.listeners = new ArrayList<ISLAViolationEventListener>();
	}
	
	public SLAPartialPlanScheduling(SLAPartialPlanScheduling schedule) {
		this.trainSize = schedule.trainSize;
		this.listeners = new ArrayList<ISLAViolationEventListener>();
		for (ISLAViolationEventListener listener: schedule.listeners) {
			this.listeners.add(listener);
		}
		this.plans = new ArrayList<IScheduling>();
		for (IScheduling plan : schedule.plans) {
			this.plans.add(plan);
		}
	}

	@Override
	public void clear() {
		this.plans.clear();
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		this.plans.add(scheduling);
	}

	@Override
	public int planCount() {
		return this.plans.size();
	}

	@Override
	public IScheduling nextPlan() {
		// TODO not implemented yet
		return null;
	}

	@Override
	public void removePlan(IScheduling plan) {
		this.plans.remove(plan);
	}
	
	@Override
	public IPartialPlanScheduling clone() {
		return new SLAPartialPlanScheduling(this);
	}

	public void setTrainSize(int trainSize) {
		this.trainSize = trainSize;
	}

	public int getTrainSize() {
		return trainSize;
	}
	
	public void addSLAViolationEventListener(ISLAViolationEventListener listener) {
		this.listeners.add(listener);
	}
	
	public boolean removeSLAViolationEventListener(ISLAViolationEventListener listener) {
		return this.listeners.remove(listener);
	}
	
	private void fireSLAViolationEvent (SLAViolationEvent event) {
		for (ISLAViolationEventListener listener : this.listeners) {
			listener.slaViolated(event);
		}
	}
	
}
