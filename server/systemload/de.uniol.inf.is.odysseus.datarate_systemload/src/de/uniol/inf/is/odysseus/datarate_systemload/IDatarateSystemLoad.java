package de.uniol.inf.is.odysseus.datarate_systemload;

import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;

public interface IDatarateSystemLoad extends IDatarate, ISystemLoad {

	@Override
	public IDatarateSystemLoad clone();
}
