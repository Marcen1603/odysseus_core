package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public interface ITimeIntervalSweepArea<T> extends ITemporalSweepArea<T>{

	public PointInTime getMaxTs();
	public PointInTime getMinTs();
	@Override
	public ITimeIntervalSweepArea<T> clone();
}
