package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

public interface ISinkStreamHandlerBuilder {

	@SuppressWarnings("rawtypes")
	ISinkStreamHandler newInstance(Socket socket);

}
