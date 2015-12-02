package de.uniol.inf.is.odysseus.interval_trust;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.trust.ITrust;

public interface ITimeIntervalTrust extends ITimeInterval, ITrust {

	@Override
	public ITimeIntervalTrust clone();
	
}