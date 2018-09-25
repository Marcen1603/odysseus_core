package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

abstract public class AbstractSimplePullTransportHandler<T> extends
		AbstractPullTransportHandler implements IIteratable<T> {

	public AbstractSimplePullTransportHandler() {
		super();
	}

	public AbstractSimplePullTransportHandler(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	
	@Override
	public void processInOpen() throws IOException {
		
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Not implemented");
	}

	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Not implemented");
	}

}
