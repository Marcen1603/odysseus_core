package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

public class ObjectStreamHandlerBuilder implements IStreamHandlerBuilder {

	@Override
	public IStreamHandler newInstance(Socket socket) {
		ObjectStreamHandler val = new ObjectStreamHandler(socket);
		return val;
	}

}
