package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

public interface IStreamHandlerFactory {

	IStreamHandler newInstance(Socket socket);

}
