package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;

public interface ITransportHandlerOpenCloseHandler {

	void processInOpen() throws IOException;
	void processOutOpen() throws IOException;
	
	void processInClose() throws IOException;
	void processOutClose() throws IOException;
	

	
}
