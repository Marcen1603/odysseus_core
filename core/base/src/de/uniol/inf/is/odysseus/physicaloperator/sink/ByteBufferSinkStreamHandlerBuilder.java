package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.net.Socket;

import de.uniol.inf.is.odysseus.physicaloperator.access.IObjectHandler;

@SuppressWarnings("rawtypes")
public class ByteBufferSinkStreamHandlerBuilder implements
		ISinkStreamHandlerBuilder {

	private IObjectHandler objectHandler;

	public ByteBufferSinkStreamHandlerBuilder(IObjectHandler objectHandler){
		this.objectHandler = objectHandler;
	}
	
	@Override
	public ISinkStreamHandler newInstance(Socket socket) {
		return new ByteBufferStreamHandler(socket, objectHandler);
	}

}
