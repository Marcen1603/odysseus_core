package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.net.Socket;

@SuppressWarnings("rawtypes")
public class ByteBufferSinkStreamHandlerBuilder implements
		ISinkStreamHandlerBuilder {
	
	@Override
	public ISinkStreamHandler newInstance(Socket socket) {
		return new ByteBufferStreamHandler(socket);
	}

}
