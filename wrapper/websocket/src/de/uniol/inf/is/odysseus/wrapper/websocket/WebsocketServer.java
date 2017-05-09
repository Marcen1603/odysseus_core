package de.uniol.inf.is.odysseus.wrapper.websocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebsocketServer extends WebSocketServer {

	private final WebsocketServerHandler handler;

	public WebsocketServer(WebsocketServerHandler handler, InetSocketAddress address) {
		super(address);
		this.handler = handler;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
		buf.position(buf.limit());
		handler.fireProcess(buf);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
	}

}
