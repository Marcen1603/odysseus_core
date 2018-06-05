package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public interface ITimeIntervalLatencySystemLoad extends ITimeIntervalSystemLoad, ITimeInterval, ILatency, ISystemLoad {

	@Override
	public ITimeIntervalLatencySystemLoad clone();
}
