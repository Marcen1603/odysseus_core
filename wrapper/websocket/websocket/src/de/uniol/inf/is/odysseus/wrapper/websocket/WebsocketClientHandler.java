package de.uniol.inf.is.odysseus.wrapper.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class WebsocketClientHandler extends AbstractTransportHandler {

	final static private String URI = "uri";
	private WebSocketClient client;

	public WebsocketClientHandler() {
		super();
	}

	public WebsocketClientHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new WebsocketClientHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return "WebsocketClient";
	}

	@Override
	public void processInOpen() throws IOException {

		client = new WebSocketClient(getUri()) {

			@Override
			public void onOpen(ServerHandshake handshakedata) {
			}

			@Override
			public void onMessage(String message) {
				ByteBuffer bytes = ByteBuffer.wrap(message.getBytes());
				bytes.position(bytes.limit());
				fireProcess(bytes);
			}
			@Override
			public void onMessage(ByteBuffer bytes) {
				bytes.position(bytes.limit());
				fireProcess(bytes);
			}

			@Override
			public void onError(Exception ex) {
			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
			}
		};
		client.connect();
	}

	@Override
	public void processOutOpen() throws IOException {

		client = new WebSocketClient(getUri()) {

			@Override
			public void onOpen(ServerHandshake handshakedata) {

			}

			@Override
			public void onMessage(String message) {

			}

			@Override
			public void onError(Exception ex) {

			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
			}
		};
		client.connect();
		
	}

	private URI getUri() {
		URI uri;
		try {
			uri = new URI(getOptionsMap().get(URI));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return uri;
	}

	@Override
	public void processInClose() throws IOException {
		if (client != null) {
			client.close();
			client = null;
		}
	}

	@Override
	public void processOutClose() throws IOException {
		if (client != null) {
			client.close();
			client = null;
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		client.send(message);
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
