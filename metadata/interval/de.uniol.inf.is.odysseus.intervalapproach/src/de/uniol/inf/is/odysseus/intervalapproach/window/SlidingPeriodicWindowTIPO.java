/*
 * SlidingDeltaWindowPO.java
 *
 * Created on 13. November 2007, 13:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.intervalapproach.window;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.LiesInPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.StartsBeforePredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;

/**
 * This is the physical sliding delta window po. It returns elements after a
 * period of time (delta) and is blocking all the other time.
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 */
public class SlidingPeriodicWindowTIPO<R extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractWindowTIPO<R> {

	/**
	 * This operator needs a sweeparea if the pos-neg approach will be used.
	 */
	private AbstractSweepArea<IMetaAttributeContainer<? extends ITimeInterval>> sa;

	/**
	 * This is the number of slides that have been processed.
	 */
	private long slideNo;

	/**
	 * list with elements to deliver
	 */
	private LinkedList<IMetaAttributeContainer<? extends ITimeInterval>> deliveryList;

	/** Creates a new instance of SlidingDeltaWindowPO */
	public SlidingPeriodicWindowTIPO(WindowAO logical) {
		super(logical);

		this.sa = new DefaultTISweepArea<IMetaAttributeContainer<? extends ITimeInterval>>();
		this.deliveryList = new LinkedList<IMetaAttributeContainer<? extends ITimeInterval>>();

		this.sa.setRemovePredicate(StartsBeforePredicate.getInstance());
		// the execution mode must be positive negative in every case,
		// because in this window only the start time stamp of the elements
		// is important, but in interval based mode, also the end time stamp
		// would be considered and this would lead to an errornous output.
		this.sa.setQueryPredicate(LiesInPredicate.getInstance());
		this.slideNo = 1;
	}

	public SlidingPeriodicWindowTIPO(SlidingPeriodicWindowTIPO<R> original) {
		super(original);

		this.sa = original.sa;
		this.deliveryList = original.deliveryList;
		this.slideNo = original.slideNo;
	}

	@Override
	public SlidingPeriodicWindowTIPO<R> clone() {
		return new SlidingPeriodicWindowTIPO<R>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process_next(R object, int port) {
		long t_s = object.getMetadata().getStart().getMainPoint();
		long delta = this.windowAdvance;
		long winSize = this.windowSize;
		// first check if elements have to be removed and/or delivered
		if ((t_s / delta) >= this.slideNo) {
			this.slideNo = t_s / delta;

			// return all elements with a start timestamp
			// (t_s/delta) * delta - winsize <= start timestamp < (t_s/ delta) *
			// delta
			long comp_t_start = this.slideNo * delta - winSize;
			PointInTime p_start = new PointInTime(Math.max(comp_t_start, 0));
			// p_end must be infinity because otherwise, the lies in predicate
			// will
			// not work. the elements in the sweep area still have no end
			// timestamp
			// and therefore their end timestamp is infinity
			PointInTime p_end = new PointInTime();
			TimeInterval compVal = new TimeInterval(p_start, p_end);
			IMetaAttributeContainer<? extends ITimeInterval> compElem = new MetaAttributeContainer<ITimeInterval>(
					compVal);
			Iterator<IMetaAttributeContainer<? extends ITimeInterval>> iter = this.sa
					.query(compElem, Order.LeftRight);
			while (iter.hasNext()) {
				IMetaAttributeContainer<? extends ITimeInterval> retval;
				retval = (IMetaAttributeContainer<? extends ITimeInterval>) iter
						.next().clone();
				// edit the time interval
				retval.getMetadata().setStart(p_start);
				// the end time stamp must be ( actual / delta )
				retval.getMetadata().setEnd(
						new PointInTime(this.slideNo * delta));
				this.deliveryList.add(retval);
			}

			// remove all elements that have a start timestamp
			// (t_s / delta) * delta - winsize <= start timestamp < (t_s /
			// delta) * delta - winsize + delta
			long remove_t_start = (this.slideNo * delta) - winSize + delta - 1;
			PointInTime p_remove = new PointInTime(remove_t_start);

			this.sa.purgeElementsBefore(p_remove);
		}

		this.sa.insert(object);

		while (!this.deliveryList.isEmpty()) {
			this.transfer((R) this.deliveryList.removeFirst());
		}
	}

	public void process_close() {
	}
}
