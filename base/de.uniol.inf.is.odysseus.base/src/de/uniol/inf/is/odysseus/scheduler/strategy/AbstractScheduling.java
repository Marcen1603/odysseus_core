package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
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

	static private Logger logger = LoggerFactory
			.getLogger(AbstractScheduling.class);

	private List<ISchedulingEventListener> schedulingEventListener = new ArrayList<ISchedulingEventListener>();
	private IPartialPlan plan = null;
	protected boolean isPlanChanged = true;
	/**
	 * BitVector for every source, is set to false, if no data is available
	 * Changed on ProcessDone
	 */
	BitSet schedulable = new BitSet();
	/**
	 * if scheduling currently paused because nothing to schedule, need to send
	 * event if scheduling can be continued
	 */
	boolean schedulingPaused = false;
	/**
	 * BitVector for every source, is set to false, if source in manually
	 * blocked Changed by Blocked and Unblocked Events
	 */
	BitSet notBlocked = new BitSet();
	/**
	 * if all schedulable operators are blocked, is set to true, need to send
	 * event that scheduling is possible again
	 */
	boolean blocked = false;
	
	/**
	 * 
	 * @param plan
	 */
	private SLATestCalculator slaTestCalculator = null;
	
	public AbstractScheduling(IPartialPlan plan) {
		this.plan = plan;
		prepareSources();
	}

	public void planChanged() {
		this.isPlanChanged = true;
	}

	public void applyChangedPlan() {
		prepareSources();
	}

	protected void prepareSources() {
		logger.debug("Prepare Sources " + plan.getIterableSource());

		for (int bitIndex = 0; bitIndex < plan.getIterableSource().size(); bitIndex++) {
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.ProcessDone);
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.Unblocked);
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.Blocked);
			schedulable.set(bitIndex, true);
			notBlocked.set(bitIndex, true);
		}
		blocked = false;
		schedulingPaused = false;
		// TMP to Test
		slaTestCalculator = new SLATestCalculator(plan.getIterableSource());
	}

	@Override
	/** Returns true partial plan is done (== nothing more to schedule)
	 */
	public boolean schedule(long maxTime) {
		// if the underlying plan has changed, we need to call
		// the update-method before starting the scheduling:
		if (this.isPlanChanged) {
			this.applyChangedPlan();
			this.isPlanChanged = false;
		}

		long endTime = System.currentTimeMillis() + maxTime;
		IIterableSource<?> nextSource = nextSource();
		synchronized (schedulingEventListener) {
			while (!this.blocked && !this.schedulingPaused
					&& nextSource != null
					&& System.currentTimeMillis() < endTime) {
				// System.out.println("Process ISource "+nextSource+" b="+nextSource.isBlocked()+" a="
				// +nextSource.isActive()+" n="+nextSource.hasNext());
				if (nextSource.isDone()) {
					sourceDone(nextSource);
				} else if (nextSource.isBlocked()) {
					logger.debug(nextSource + " blocked");
					updateBlocked(plan.getSourceId(nextSource));
				} else if (nextSource.hasNext() && nextSource.isActive()) {
					// logger.debug(nextSource + " process");
					nextSource.transferNext();
				} else {
					// logger.debug(nextSource + " nothing to process");
					updateSchedulable(nextSource);
				}
				nextSource = nextSource();
			}
			//System.out.println(slaTestCalculator);
			return isDone();
		}
	}

	private void updateSchedulable(IIterableSource<?> nextSource) {
		schedulable.set(plan.getSourceId(nextSource), false);
		if (schedulable.cardinality() == 0) {
			if (schedulingPaused == false) {
				schedulingPaused = true;
				// logger.debug("Scheduling paused, nothing to schedule");
				for (ISchedulingEventListener l : schedulingEventListener) {
					l.nothingToSchedule(this);
				}
			}
		}
	}

	private void updateBlocked(int index) {
		notBlocked.set(index, false);
		if (notBlocked.cardinality() == 0) {
			if (blocked == false) {
				blocked = true;
				logger.debug("Processing blocked because all operators are blocked");
				for (ISchedulingEventListener l : schedulingEventListener) {
					l.nothingToSchedule(this);
				}
			}
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
		IIterableSource<?> s = (IIterableSource<?>) poEvent.getSource();
		int index = plan.getSourceId(s);
		synchronized (notBlocked) {
			if (poEvent.getPOEventType() == POEventType.Blocked) {
				// System.out.println(poEvent);
				updateBlocked(index);
				return;
			} else if (poEvent.getPOEventType() == POEventType.Unblocked) {
				// System.out.println(poEvent);
				notBlocked.set(index, true);
				if (blocked) {
					for (ISchedulingEventListener l : schedulingEventListener) {
						l.scheddulingPossible(this);
					}
				}
			}
		}
		// Ignore ProcessDone Events if Source is blocked
		if (notBlocked.get(index)) {
			// System.out.println(poEvent);
			if (poEvent.getPOEventType() == POEventType.ProcessDone) {
				synchronized (schedulingEventListener) {
					schedulable.set(index, true);
					if (schedulingPaused && s.isActive() && !s.isBlocked()) {
						schedulingPaused = false;
						// logger.debug("Scheduling reactivated");
						for (ISchedulingEventListener l : schedulingEventListener) {
							l.scheddulingPossible(this);
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean isSchedulingBlocked() {
		return blocked;
	}
	
	@Override
	public boolean isSchedulingPaused() {
		return schedulingPaused;
	}

}

class SLATestCalculator implements IPOEventListener{
	final Map<IPhysicalOperator, Long> processPerOperator;
	long overallCount = 0;
	
	public SLATestCalculator(List<? extends IPhysicalOperator> toMonitor) {
		processPerOperator = new HashMap<IPhysicalOperator, Long>();
		for (IPhysicalOperator p:toMonitor){
			processPerOperator.put(p, 0l);
			p.subscribe(this, POEventType.ProcessDone);
		}
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		IPhysicalOperator source = poEvent.getSource();
		synchronized (processPerOperator) {
			long c = processPerOperator.get(source);
			processPerOperator.put(source, c+1);
			overallCount++;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer(this.getClass().getSimpleName());
		b.append("OverallCount "+overallCount+"\n");
		for (Entry<IPhysicalOperator, Long> p:processPerOperator.entrySet()){
			b.append("--> "+p.getKey()+" = "+p.getValue()+" "+ (overallCount>0?(p.getValue() / overallCount):0)+"\n");
		}
		return b.toString();
	}
}
