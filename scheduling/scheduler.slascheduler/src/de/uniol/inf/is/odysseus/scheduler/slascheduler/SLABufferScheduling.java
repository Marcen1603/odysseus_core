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
		// TODO process batch, limit schedsPerBuffer
		// if the underlying plan has changed, we need to call
		// the update-method before starting the scheduling:
		if (this.isPlanChanged) {
			this.applyChangedPlan();
			this.isPlanChanged = false;
		}
		
		long endTime = System.currentTimeMillis() + maxTime;
		IIterableSource<?> nextSource = nextSource();
		while (!this.isSchedulingBlocked() && !this.isSchedulingPaused() && nextSource != null
				&& System.currentTimeMillis() < endTime) {
			// System.out.println("Process ISource "+nextSource+" b="+nextSource.isBlocked()+" n="+nextSource.hasNext());
			if (nextSource.isDone()) {
				sourceDone(nextSource);
			} else if (nextSource.isBlocked()) {
				updateBlocked(getPlan().getSourceId(nextSource));
			}else if (nextSource.hasNext()) {
				nextSource.transferNext();
			} else {
				updateSchedulable(nextSource);
			}
			nextSource = nextSource();
		}
		return isDone();
	}

}
