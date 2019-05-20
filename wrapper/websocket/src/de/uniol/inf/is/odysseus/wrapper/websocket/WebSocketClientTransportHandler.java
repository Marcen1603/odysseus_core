package de.uniol.inf.is.odysseus.wrapper.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class WebSocketClientTransportHandler extends AbstractTransportHandler {

	Logger LOG = LoggerFactory.getLogger(WebSocketClientTransportHandler.class);

	private static final String URI = "uri";
	public static final String NAME = "WebsocketClient";

	private WebSocketClient client;
	private URI uri;

	public WebSocketClientTransportHandler() {
		super();
	}

	public WebSocketClientTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		options.checkRequiredException(URI);
		try {
			this.uri = new URI(getOptionsMap().get(URI));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new WebSocketClientTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {

		LOG.info("Connecting to " + uri);
		
		if (client == null) {

			client = new WebSocketClient(uri) {

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					LOG.info("Connected to " + uri);
				}

				@Override
				public void onMessage(String message) {
					fireProcess(message);
				}

				@Override
				public void onMessage(ByteBuffer bytes) {
					ByteBuffer copy = ByteBuffer.allocate(bytes.capacity());
					final ByteBuffer readOnly = bytes.asReadOnlyBuffer();
					copy.put(readOnly);
					fireProcess(copy);
				}

				@Override
				public void onError(Exception ex) {
					LOG.error("ERROR in Websocket Connection", ex);
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					LOG.info("Disconnected from %s", uri);
				}
			};
			client.connect();
		}
	}

	@Override
	public void processOutOpen() throws IOException {

		LOG.info("Connecting to " + uri);

		client = new WebSocketClient(uri) {

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
