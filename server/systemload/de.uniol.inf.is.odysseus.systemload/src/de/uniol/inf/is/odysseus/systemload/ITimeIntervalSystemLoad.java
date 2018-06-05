package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ITimeIntervalSystemLoad extends ITimeInterval, ISystemLoad {
	
	@Override
	public ITimeIntervalSystemLoad clone();

}
