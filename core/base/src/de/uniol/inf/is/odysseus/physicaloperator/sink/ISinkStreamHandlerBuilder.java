package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

public interface ISinkStreamHandlerBuilder {

	ISinkStreamHandler newInstance(Socket socket);

}
