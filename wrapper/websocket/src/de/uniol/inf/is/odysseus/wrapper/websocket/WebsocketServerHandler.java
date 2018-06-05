package de.uniol.inf.is.odysseus.wrapper.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class WebsocketServerHandler extends AbstractTransportHandler {

	//private static final Logger LOG = LoggerFactory.getLogger(WebsocketServerHandler.class);
	private static final String PORT = "port";
	private int port;
	private WebsocketServer server = null;

	public WebsocketServerHandler() {
		super();
	}

	public WebsocketServerHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		port = options.getInt(PORT, 80);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		WebsocketServerHandler result = new WebsocketServerHandler(protocolHandler, options);
		return result;
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public String getName() {
		return "WebsocketServer";
	}

	@Override
	public void processInOpen() throws IOException {
		server = new WebsocketServer(this, new InetSocketAddress(port));
		server.start();
	}

	@Override
	public void processOutOpen() throws IOException {
		server = new WebsocketServer(this, new InetSocketAddress(port));
		server.start();
	}

	@Override
	public void processInClose() throws IOException {
		processClose();
	}

	@Override
	public void processOutClose() throws IOException {
		processClose();
	}

	private void processClose() throws IOException {
		if (server == null) {
			return;
		}

		try {
			server.stop();
		} catch (InterruptedException e) {
			throw new IOException(e);
		} finally {
			server = null;
		}

	}

	@Override
	public void send(byte[] message) throws IOException {
		for (WebSocket connection : server.connections()) {
			try {
				connection.send(message);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@Override
	public void send(Object message) throws IOException {
		send(message.toString().getBytes());
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
