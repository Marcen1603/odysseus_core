package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.WindowCalculator;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.IDataFactory;
import de.uniol.inf.is.odysseus.pnapproach.base.predicate.SlidingTimeWindowPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * Dieses Fenster kann sowohl fuer JumpingTimeWindow als auch fuer SlidingDeltaTimeWindow
 * benutzt werden. Lediglich SlidingTimeWindow laesst sich schneller berechnen, wenn man
 * es separat implementiert.
 * 
 * @author Andre Bolles
 */
@SuppressWarnings("unchecked")
public class SlidingTimeWindowPNPO<M extends IPosNeg, T extends IMetaAttributeContainer<M>> extends
	AbstractNonBlockingWindowPNPO<M, T> {

	public SlidingTimeWindowPNPO(long windowSize, long windowAdvance, IDataFactory<M, M, T, T> dFac) {
		super(windowSize, windowAdvance, dFac);
		IPredicate<T> removePredicate = new SlidingTimeWindowPredicate(this.windowSize);
		this.init(removePredicate);
	}

	public SlidingTimeWindowPNPO(SlidingTimeWindowPNPO name) {
		super(name);
	}

	@Override
	protected PointInTime calcWindowEnd(PointInTime startTimestamp) {
		return WindowCalculator.calcSlidingWindowEnd(startTimestamp, this.windowSize);
	}

	@Override
	public SlidingTimeWindowPNPO<M, T> clone() {
		return new SlidingTimeWindowPNPO<M, T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

}
