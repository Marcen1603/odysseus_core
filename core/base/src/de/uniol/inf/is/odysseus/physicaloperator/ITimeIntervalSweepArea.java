package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;

public interface ITimeIntervalSweepArea<T> extends ITemporalSweepArea<T>{

	public PointInTime getMaxTs();
	public PointInTime getMinTs();
	@Override
	public ITimeIntervalSweepArea<T> clone();
}
