package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface ITimeIntervalDatarate extends ITimeInterval, IDatarate {

	@Override
	public ITimeIntervalDatarate clone();
}
