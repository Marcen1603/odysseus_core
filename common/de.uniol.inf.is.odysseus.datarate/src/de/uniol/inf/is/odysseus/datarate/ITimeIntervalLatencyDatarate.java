package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public interface ITimeIntervalLatencyDatarate extends ITimeIntervalDatarate, ITimeInterval, IDatarate, ILatency {

	@Override
	public ITimeIntervalLatencyDatarate clone();
}
