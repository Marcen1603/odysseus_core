package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * Effizientere Implementierung eines SlidingAdvanceTimeWindow
 * @author Jonas Jacobi
 */
public class SlidingTimeWindowTIPO<T extends IMetaAttributeContainer<ITimeInterval>> extends
		AbstractNonBlockingWindowTIPO<T> {

	public SlidingTimeWindowTIPO(WindowAO algebraOp) {
		super(algebraOp);
	}

	public SlidingTimeWindowTIPO(SlidingTimeWindowTIPO<T> name) {
		super(name);
	}

	@Override
	protected PointInTime calcWindowEnd(ITimeInterval time) {
		return WindowCalculator.calcSlidingWindowEnd(time, windowSize);
	}

	@Override
	public SlidingTimeWindowTIPO<T> clone() {
		return new SlidingTimeWindowTIPO<T>(this);
	}
}
