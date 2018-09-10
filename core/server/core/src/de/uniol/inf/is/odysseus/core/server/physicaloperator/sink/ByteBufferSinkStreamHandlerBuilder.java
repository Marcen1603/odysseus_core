package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.net.Socket;

import de.uniol.inf.is.odysseus.core.physicaloperator.sink.ISinkStreamHandler;

@SuppressWarnings("rawtypes")
public class ByteBufferSinkStreamHandlerBuilder implements
		ISinkStreamHandlerBuilder {
	
	@Override
	public ISinkStreamHandler newInstance(Socket socket) {
		return new ByteBufferStreamHandler(socket);
	}

}
