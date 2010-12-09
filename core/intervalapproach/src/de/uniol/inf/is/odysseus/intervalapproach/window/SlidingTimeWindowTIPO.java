package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

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
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
}
