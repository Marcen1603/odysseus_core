package de.uniol.inf.is.odysseus.systemload;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public interface ILatencySystemLoad extends ILatency, ISystemLoad {
	
	@Override
	public ILatencySystemLoad clone();

}
