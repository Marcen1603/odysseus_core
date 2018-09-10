package de.uniol.inf.is.odysseus.datarate_systemload;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public interface ITimeIntervalLatencyDatarateSystemLoad extends ITimeIntervalDatarateSystemLoad, ILatency{

	@Override
	public ITimeIntervalLatencyDatarateSystemLoad clone();
}
