package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

/**
 * Allgemeine Klasse fuer SlidingTimeWindow. Spezielle implementierungen fuer SlidingTimeWindow mit Delta = 1 und FixedWindow mit Delta = WindowSize
 * vorhanden, da diese performanter sind, als diese allgemeine Implementierung.
 * 
 * @author abolles
 *
 * @param <T>
 */
public class SlidingAdvanceTimeWindowTIPO <T extends IMetaAttribute<? extends ITimeInterval>> extends AbstractNonBlockingWindowTIPO<T>{

	public SlidingAdvanceTimeWindowTIPO(WindowAO algebraOp) {
		super(algebraOp);
	}

	public SlidingAdvanceTimeWindowTIPO(SlidingAdvanceTimeWindowTIPO<T> name) {
		super(name);
	}
	
	@Override
	public SlidingAdvanceTimeWindowTIPO<T> clone() {
		return new SlidingAdvanceTimeWindowTIPO<T>(this);
	}
	
	protected PointInTime calcWindowEnd(ITimeInterval time) {
		return WindowCalculator.calcSlidingDeltaWindowEnd(time, this.windowAdvance, this.windowSize);
	}
}
