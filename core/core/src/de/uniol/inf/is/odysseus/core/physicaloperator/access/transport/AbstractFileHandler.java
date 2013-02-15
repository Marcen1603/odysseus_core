package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

abstract public class AbstractFileHandler extends AbstractTransportHandler {

	protected String filename;
	protected InputStream in;
	protected OutputStream out;
	protected boolean append;

	public AbstractFileHandler() {
		super();
	}

	public AbstractFileHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(byte[] message) throws IOException {
		out.write(message);
	}

	@Override
	public InputStream getInputStream() {
		return in;
	}

	@Override
	public OutputStream getOutputStream() {
		return out;
	}

	@Override
	public void processInClose() throws IOException {
		fireOnDisconnect();
		in.close();
	}

	@Override
	public void processOutClose() throws IOException {
		fireOnDisconnect();
		out.flush();
		out.close();
	}

}
