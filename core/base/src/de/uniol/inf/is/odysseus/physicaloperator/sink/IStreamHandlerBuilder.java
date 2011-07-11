package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

public interface IStreamHandlerBuilder {

	IStreamHandler newInstance(Socket socket);

}
