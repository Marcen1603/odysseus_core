package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource.OutputMode;

public interface IUserDefinedFunction<R, W> {
	void init(String initString);
	W process(R in, int port);
	OutputMode getOutputMode();
}
