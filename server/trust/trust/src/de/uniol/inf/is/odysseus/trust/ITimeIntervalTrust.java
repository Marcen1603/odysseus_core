package de.uniol.inf.is.odysseus.trust;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ITimeIntervalTrust extends ITimeInterval, ITrust {

	@Override
	public ITimeIntervalTrust clone();
	
}