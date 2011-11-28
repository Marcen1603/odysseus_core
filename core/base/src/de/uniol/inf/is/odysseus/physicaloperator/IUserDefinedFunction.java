package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;

public interface IUserDefinedFunction<R, W> {
	void init(String initString);
	W process(R in, int port);
	OutputMode getOutputMode();
}
