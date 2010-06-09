package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.IWindow;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.IDataFactory;
import de.uniol.inf.is.odysseus.pnapproach.base.sweeparea.PNAwareSweepArea;

public abstract class AbstractNonBlockingWindowPNPO<M extends IPosNeg, T extends IMetaAttributeContainer<M>>
		extends AbstractPipe<T, T> implements IWindow {

	long windowSize;
	long windowAdvance;
	private IDataFactory<M, M, T, T> dFac;

	/**
	 * Eine einfache SweepArea reicht in einem WindowPNPO aus, da es an dieser
	 * Stelle noch keinen negativen Elemente gibt. Es muessen nur die Query und
	 * Remove-Praedikate entsprechend definiert werden.
	 */
	AbstractSweepArea<T> sa;

	public AbstractNonBlockingWindowPNPO(long windowSize, long windowAdvance,
			IDataFactory<M, M, T, T> dFac) {
		this.windowSize = windowSize;
		this.windowAdvance = windowAdvance;
		this.dFac = dFac;
	}

	public AbstractNonBlockingWindowPNPO(AbstractNonBlockingWindowPNPO<M, T> po) {
		this.windowSize = po.windowSize;
		this.windowAdvance = po.windowAdvance;
		this.sa = po.sa;
		this.dFac = po.dFac;
	}

	protected void init(IPredicate<T> removePredicate) {
		this.sa = new PNAwareSweepArea<T>();
		this.sa.setRemovePredicate(removePredicate);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		// Fuer jedes Element, dass sich noch in der SweepArea befindet,
		// und welches zu diesem Zeitpunkt entfernt werden kann, schreibe
		// ein neues negatives Element in den Ausgabedatenstrom
		Iterator<T> negs = this.sa.extractElements(object, Order.LeftRight);
		while (negs.hasNext()) {
			@SuppressWarnings("unchecked")
			T neg;
			neg = (T) negs.next().clone();
			T modifiedElem = this.dFac.createData(neg);
			neg.getMetadata().setTimestamp(
					this.calcWindowEnd(neg.getMetadata().getTimestamp()));
			neg.getMetadata().setElementType(ElementType.NEGATIVE);

			modifiedElem.setMetadata(neg.getMetadata());

			this.transfer(modifiedElem);
		}
		this.sa.insert(object);
		this.transfer(object);
	}

	@Override
	public long getWindowSize() {
		return this.windowSize;
	}

	public long getWindowAdvance() {
		return this.windowAdvance;
	}

	/**
	 * Same as getWindowAdvance()
	 * 
	 * @return this.windowAdvance
	 */
	public long getWindowDelta() {
		return this.windowAdvance;
	}

	@Override
	public void process_done() {
		// Iterator<T> negs = this.sa.extractAllElements();
		Iterator<T> negs = this.sa.iterator();
		while (negs.hasNext()) {
			@SuppressWarnings("unchecked")
			T neg;
			neg = (T) negs.next().clone();
			T modifiedElem = this.dFac.createData(neg);
			neg.getMetadata().setTimestamp(
					this.calcWindowEnd(neg.getMetadata().getTimestamp()));
			neg.getMetadata().setElementType(ElementType.NEGATIVE);

			modifiedElem.setMetadata(neg.getMetadata());

			this.transfer(modifiedElem);
		}
		sa.clear();
	}

	protected abstract PointInTime calcWindowEnd(PointInTime startTimestamp);

	@Override
	public WindowType getWindowType() {
		return WindowType.TIME_BASED;
	}

}
