package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.List;

import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;

public class SLAPartialPlanScheduling implements IPartialPlanScheduling {
	
	private int trainSize;
	
	private List<ISLAViolationEventListener> listener;

	@Override
	public void clear() {
		// TODO not implemented yet
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		// TODO not implemented yetv
	}

	@Override
	public int planCount() {
		// TODO not implemented yet
		return 0;
	}

	@Override
	public IScheduling nextPlan() {
		// TODO not implemented yet
		return null;
	}

	@Override
	public void removePlan(IScheduling plan) {
		// TODO not implemented yet

	}
	
	@Override
	public IPartialPlanScheduling clone() {
		// TODO not implemented yet
		return null;
	}

	public void setTrainSize(int trainSize) {
		this.trainSize = trainSize;
	}

	public int getTrainSize() {
		return trainSize;
	}
	
	public void addSLAViolationEventListener(ISLAViolationEventListener listener) {
		this.listener.add(listener);
	}
	
	public boolean removeSLAViolationEventListener(ISLAViolationEventListener listener) {
		return this.listener.remove(listener);
	}
	
	private void fireSLAViolationEvent (ISLAViolationEventListener event) {
		// TODO not implemented yet
	}

}
