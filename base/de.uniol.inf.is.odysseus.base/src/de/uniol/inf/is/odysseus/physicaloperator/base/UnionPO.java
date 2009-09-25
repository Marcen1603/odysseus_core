package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;

/**
 * Man braucht für PN:
 * SweepArea
 * AfterPredicate als RemovePredicate
 * UnionPNHelper
 * @author Andre Bolles
 */
public class UnionPO<R extends IMetaAttributeContainer<?>> extends AbstractPipe<R, R> {

	/**
	 * 
	 * WICHTIG: Im PN-Ansatz darf keine SweepArea verwendet werden, die Element
	 * beim Insert lï¿½scht (negatives Einfï¿½gen).
	 */
	ISweepArea<R> area;

	/**
	 * 0 = left min timestamp 1 = right min timestamp
	 */
	PointInTime minTs[];

	UnionHelper<R> helper;

	/**
	 * WICHTIG: Im PN-Ansatz darf keine SweepArea verwendet werden, die Element
	 * beim Insert lï¿½scht (negatives Einfï¿½gen).
	 * 
	 * @param area
	 * @param helper
	 */
	public UnionPO(ISweepArea<R> area, UnionHelper<R> helper) {
		super();
		this.area = area;
		this.helper = helper;
		minTs = new PointInTime[2];
	}

	public UnionPO(UnionPO<R> unionPO) {
		this.area = unionPO.area;
		this.helper = unionPO.helper;
		minTs = unionPO.minTs;
	}

	@Override
	public UnionPO<R> clone() {
		return new UnionPO<R>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected synchronized void process_next(R object, int port) {

		// setting the timestamp must be independent
		// of the metadata type
		synchronized (minTs) {
			synchronized (this.helper) {
				minTs[port] = this.helper.getStart(object);
			}
		}

		// getting the minimal timestamp of both inputs
		PointInTime min_ts = null;
		synchronized (this.minTs) {
			if (this.minTs[0] != null && this.minTs[1] != null) {
				min_ts = PointInTime.min(this.minTs[0], this.minTs[1]);
			}
		}

		synchronized (this.area) {
			this.area.insert(object);

			if (min_ts != null) {
				// get all elements from the queue, that
				// have a (start) timestamp smaller than
				// min_ts and transfer them to the next operator
				synchronized (this.helper) {
					Iterator<R> iter = this.area.extractElements(this.helper
							.getReferenceElement(min_ts, object),
							Order.LeftRight);
					while (iter.hasNext()) {
						this.transfer(iter.next());
					}
				}
			}
		}
	}
	
}
