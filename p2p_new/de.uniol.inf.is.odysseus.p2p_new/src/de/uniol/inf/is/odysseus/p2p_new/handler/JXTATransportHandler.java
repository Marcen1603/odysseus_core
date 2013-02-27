package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class JXTATransportHandler extends AbstractTransportHandler {

	// for transportFactory
	public JXTATransportHandler() {
	}

	public JXTATransportHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		JXTATransportHandler handler = new JXTATransportHandler(protocolHandler);
		return handler;
	}

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public String getName() {
		return "JXTA";
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

}
