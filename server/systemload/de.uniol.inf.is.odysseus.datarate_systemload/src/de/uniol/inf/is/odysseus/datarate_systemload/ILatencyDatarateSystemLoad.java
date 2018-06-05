package de.uniol.inf.is.odysseus.datarate_systemload;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public interface ILatencyDatarateSystemLoad extends IDatarateSystemLoad, ILatency {

	@Override
	public ILatencyDatarateSystemLoad clone();
}
