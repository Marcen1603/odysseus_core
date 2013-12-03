package de.uniol.inf.is.odysseus.core.server.metadata;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ILatencyTimeInterval extends ILatency, ITimeInterval {	
	@Override
	public ILatencyTimeInterval clone();

}
