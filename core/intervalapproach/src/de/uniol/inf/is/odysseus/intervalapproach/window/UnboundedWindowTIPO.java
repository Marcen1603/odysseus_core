package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class UnboundedWindowTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractNonBlockingWindowTIPO<T> {

	public UnboundedWindowTIPO(WindowAO algebraOp) {
		super(algebraOp);
	}

	public UnboundedWindowTIPO(UnboundedWindowTIPO<T> unboundedWindowTIPO) {
		super(unboundedWindowTIPO);
	}

	@Override
	protected PointInTime calcWindowEnd(ITimeInterval interval) {
		return PointInTime.getInfinityTime();
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new UnboundedWindowTIPO<T>(this);
	}

}
