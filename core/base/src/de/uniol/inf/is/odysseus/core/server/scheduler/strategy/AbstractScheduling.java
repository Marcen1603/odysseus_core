/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.scheduler.strategy;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.ISchedulingEventListener;

/**
 * Superclass for scheduling strategies. When scheduled by the scheduler, it
 * will execute sources returned by subsequent calls to the abstract method
 * {@link #nextSource()}, until either the time limit is reached or
 * {@link #nextSource()} returns null.
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractScheduling implements IScheduling,
		ITrainScheduling, IEventListener {

	static private Logger logger = LoggerFactory
			.getLogger(AbstractScheduling.class);

	private List<ISchedulingEventListener> schedulingEventListener = new ArrayList<ISchedulingEventListener>();
	private IPhysicalQuery plan = null;
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

	public AbstractScheduling(IPhysicalQuery plan) {
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
		logger.debug("Prepare Sources " + plan.getIterableSources());

		for (int bitIndex = 0; bitIndex < plan.getIterableSources().size(); bitIndex++) {
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.ProcessDone);
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.Unblocked);
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.Blocked);
			plan.getIterableSource(bitIndex).subscribe(this,
					POEventType.CloseDone);
			schedulable.set(bitIndex, true);
			notBlocked.set(bitIndex, true);
		}
		blocked = false;
		schedulingPaused = false;
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
		while (nextSource != null && System.currentTimeMillis() < endTime) {
			// System.out.println("Process ISource "+nextSource+" b="+nextSource.isBlocked()+" n="+nextSource.hasNext());
			try {
				if (nextSource.tryLock()) {
					if (nextSource.isDone()) {
						sourceDone(nextSource);
					} else if (nextSource.isBlocked()) {
						logger.debug(nextSource + " blocked");
						updateBlocked(plan.getSourceId(nextSource));
					} else if (nextSource.isOpen() && nextSource.hasNext()) {
						// logger.debug(nextSource + " process");
						nextSource.transferNext();
					} else {
						// logger.debug(nextSource + " nothing to process");
						updateSchedulable(nextSource);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				nextSource.unlock();
			}
			
			nextSource = nextSource();
		}
		return isDone();
	}

	@Override
	public boolean schedule(long maxTime, int trainSize) {
		// if the underlying plan has changed, we need to call
		// the update-method before starting the scheduling:
		if (this.isPlanChanged) {
			this.applyChangedPlan();
			this.isPlanChanged = false;
		}

		long endTime = System.currentTimeMillis() + maxTime;
		IIterableSource<?> nextSource = nextSource();
		while (nextSource != null && System.currentTimeMillis() < endTime) {
			try {
				if (nextSource.tryLock()) {
					if (nextSource.isDone()) {
						sourceDone(nextSource);
					} else if (nextSource.isBlocked()) {
						updateBlocked(plan.getSourceId(nextSource));
					} else if (nextSource.isOpen() && nextSource.hasNext()) {
						// batch processing of tuple train
						int numScheds = 0;
						do {
							numScheds++;
							nextSource.transferNext();
						} while (nextSource.hasNext() && numScheds < trainSize);
					} else {
						updateSchedulable(nextSource);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				nextSource.unlock();
			}
			nextSource = nextSource();
		}
		return isDone();
	}

	protected void updateSchedulable(IIterableSource<?> nextSource) {
		int id = plan.getSourceId(nextSource);
		if (id >= 0) {
			schedulable.set(id, false);
		} else {
			logger.warn(nextSource + " not Part of plan "
					+ plan.getIterableSources());
		}
		if (schedulable.cardinality() == 0) {
			if (schedulingPaused == false) {
				schedulingPaused = true;
				// logger.debug("Scheduling paused, nothing to schedule");
				synchronized (schedulingEventListener) {
					for (ISchedulingEventListener l : schedulingEventListener) {
						l.nothingToSchedule(this);
					}
				}
			}
		}
	}

	protected void updateBlocked(int index) {
		notBlocked.set(index, false);
		if (notBlocked.cardinality() == 0) {
			if (blocked == false) {
				blocked = true;
				logger.debug("Processing blocked because all operators are blocked");
				synchronized (schedulingEventListener) {
					for (ISchedulingEventListener l : schedulingEventListener) {
						l.nothingToSchedule(this);
					}
				}
			}
		}
	}

	public abstract IIterableSource<?> nextSource();

	public abstract void sourceDone(IIterableSource<?> source);

	public abstract boolean isDone();

	@Override
	public IPhysicalQuery getPlan() {
		return plan;
	}

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
	public void eventOccured(IEvent<?, ?> poEvent, long nanoTimestamp) {
		IIterableSource<?> s = (IIterableSource<?>) poEvent.getSender();
		int index = plan.getSourceId(s);
		if (poEvent.getEventType() == POEventType.CloseDone) {
			// sourceDone(s);

		} else {
			synchronized (notBlocked) {
				if (poEvent.getEventType() == POEventType.Blocked) {
					// System.out.println(poEvent);
					updateBlocked(index);
					return;
				} else if (poEvent.getEventType() == POEventType.Unblocked) {
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
				if (poEvent.getEventType() == POEventType.ProcessDone) {
					synchronized (schedulingEventListener) {
						schedulable.set(index, true);
						if (schedulingPaused && !s.isBlocked()) {
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
	}

	@Override
	public boolean isSchedulingBlocked() {
		return blocked;
	}

	@Override
	public boolean isSchedulingPaused() {
		return schedulingPaused;
	}

	@Override
	public boolean isSchedulable() {
		// TODO:
		return true;
	}

}
