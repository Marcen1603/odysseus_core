package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public interface ILatencyDatarate extends ILatency, IDatarate {

	@Override
	public ILatencyDatarate clone();
	
}
