package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.IDataFactory;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.WindowCalculator;
import de.uniol.inf.is.odysseus.pnapproach.base.predicate.SlidingAndJumpingDeltaWindowPNPredicate;

/**
 * Dieses Fenster kann sowohl fuer JumpingTimeWindow als auch fuer SlidingDeltaTimeWindow
 * benutzt werden. Lediglich SlidingTimeWindow laesst sich schneller berechnen, wenn man
 * es separat implementiert.
 * 
 * @author Andre Bolles
 */
public class SlidingAdvanceTimeWindowPNPO<M extends IPosNeg, T extends IMetaAttributeContainer<M>> extends
	AbstractNonBlockingWindowPNPO<M, T> {

	public SlidingAdvanceTimeWindowPNPO(long windowSize, long windowAdvance, IDataFactory<M, M, T, T> dFac) {
		super(windowSize, windowAdvance, dFac);
		IPredicate<T> removePredicate = new SlidingAndJumpingDeltaWindowPNPredicate<T>(this.windowSize, this.windowAdvance);
		this.init(removePredicate);
	}

	public SlidingAdvanceTimeWindowPNPO(SlidingAdvanceTimeWindowPNPO<M, T> name) {
		super(name);
	}

	@Override
	protected PointInTime calcWindowEnd(PointInTime startTimestamp) {
		return WindowCalculator.calcSlidingDeltaWindowEnd(startTimestamp, this.windowAdvance, this.windowSize);
	}

	@Override
	public SlidingAdvanceTimeWindowPNPO<M, T> clone() {
		return new SlidingAdvanceTimeWindowPNPO<M,T>(this);
	}

}
