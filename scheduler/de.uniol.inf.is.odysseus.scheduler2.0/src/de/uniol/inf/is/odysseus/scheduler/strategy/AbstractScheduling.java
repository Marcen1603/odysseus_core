package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;

/**
 * Superclass for scheduling strategies. When scheduled by the scheduler, it
 * will execute sources returned by subsequent calls to the abstract method
 * {@link #nextSource()}, until either the time limit is reached or
 * {@link #nextSource()} returns null.
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractScheduling implements IScheduling,
		IPOEventListener {

	private List<ISchedulingEventListener> schedulingEventListener = new ArrayList<ISchedulingEventListener>();
	private IPartialPlan plan = null;
	protected boolean isPlanChanged = true;
	BitSet schedulable = new BitSet();

	public AbstractScheduling(IPartialPlan plan) {
		this.plan = plan;
		processSources();
	}

	public void planChanged() {
		this.isPlanChanged = true;
	}

	public void applyChangedPlan() {
		processSources();
	}

	protected void processSources() {
		for (int bitIndex = 0;bitIndex < plan.getIterableSource().size(); bitIndex++){
			plan.getIterableSource(bitIndex).subscribe(this, POEventType.ProcessDone);
			schedulable.set(bitIndex,true);
		}
	}

	@Override
	/** Returns true if nothing more to schedule
	 */
	public boolean schedule(long maxTime) {
		// Testen ob die Zeit richtig gesetzt ist (ansonsten wuerde die Schleife
		// in transferNext() aufrufen. Evtl. den Test weiter nach oben schieben?
		// if (maxTime <= 0){
		// throw new IllegalArgumentException("maxTime must be greater 0");
		// }

		// if the underlying plan has changed, we need to call
		// the update-method before starting the scheduling:
		if (this.isPlanChanged) {
			this.applyChangedPlan();
			this.isPlanChanged = false;
		}

		long endTime = System.currentTimeMillis() + maxTime;
		IIterableSource<?> nextSource = nextSource();
		synchronized (schedulingEventListener) {
			while (nextSource != null && System.currentTimeMillis() < endTime) {
				// System.out.println("Process ISource "+nextSource);
				if (nextSource.isDone()) {
					sourceDone(nextSource);
				} else if (nextSource.hasNext() && nextSource.isActive()) {
					nextSource.transferNext();
				} else {
					schedulable.set(plan.getSourceId(nextSource), false);
					if (schedulable.cardinality() == 0) {
						for (ISchedulingEventListener l : schedulingEventListener) {
							l.nothingToSchedule(this);
						}
						break;
					}

				}
				nextSource = nextSource();
			}
			return isDone();
		}
	}

	public abstract IIterableSource<?> nextSource();

	public abstract void sourceDone(IIterableSource<?> source);

	public abstract boolean isDone();

	public IPartialPlan getPlan() {
		return plan;
	};

	@Override
	public void addSchedulingEventListener(
			ISchedulingEventListener eventListener) {
		synchronized (schedulingEventListener) {
			schedulingEventListener.add(eventListener);
		}
	}

	@Override
	public void removeSchedulingEventListener(
			ISchedulingEventListener eventListener) {
		synchronized (schedulingEventListener) {
			schedulingEventListener.remove(eventListener);
		}
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		synchronized (schedulingEventListener) {
			for (ISchedulingEventListener l : schedulingEventListener) {
				l.scheddulingPossible(this);
			}
			schedulable.set(plan.getSourceId(((IIterableSource<?>) poEvent.getSource())),true);
		}
	}
}
