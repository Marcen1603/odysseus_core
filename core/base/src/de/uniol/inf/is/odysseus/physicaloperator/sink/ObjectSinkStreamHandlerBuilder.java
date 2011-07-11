package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

public class ObjectSinkStreamHandlerBuilder implements ISinkStreamHandlerBuilder {

	@Override
	public ISinkStreamHandler newInstance(Socket socket) {
		ObjectSinkStreamHandler val = new ObjectSinkStreamHandler(socket);
		return val;
	}

}
