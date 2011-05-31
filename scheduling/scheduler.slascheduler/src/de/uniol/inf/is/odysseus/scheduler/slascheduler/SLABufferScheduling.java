package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractScheduling;

public class SLABufferScheduling extends AbstractScheduling {

	public SLABufferScheduling(IPartialPlan plan) {
		super(plan);
		// TODO not implemented yet
	}

	@Override
	public IIterableSource<?> nextSource() {
		// TODO not implemented yet
		return null;
	}

	@Override
	public void sourceDone(IIterableSource<?> source) {
		// TODO not implemented yet
	}

	@Override
	public boolean isDone() {
		// TODO not implemented yet
		return false;
	}

	@Override
	public boolean schedule(long maxTime) {
		// TODO not implemented yet
		return super.schedule(maxTime);
	}
	
	public boolean schedule(long maxTime, int schedsPerBuffer, int trainSize) {
		// TODO not implemented yet
		return false;
	}

}
