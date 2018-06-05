package de.uniol.inf.is.odysseus.datarate_systemload;

import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ITimeIntervalSystemLoad;

public interface ITimeIntervalDatarateSystemLoad extends ITimeIntervalSystemLoad, IDatarate {

	@Override
	public ITimeIntervalDatarateSystemLoad clone();
}
