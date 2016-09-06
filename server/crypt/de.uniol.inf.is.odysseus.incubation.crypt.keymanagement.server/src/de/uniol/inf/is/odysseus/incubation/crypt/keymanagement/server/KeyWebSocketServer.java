/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.server;

import java.net.InetSocketAddress;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.EncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetEncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetPublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.PublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.StringMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.KeyManager;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.JsonUtils;

/**
 * A server to communicate with laotseClients and to distribute requests to the
 * LaotseDb, Mosaik and Odysseus.
 *
 */
public class KeyWebSocketServer extends WebSocketServer {
	private Gson gson;
	private static KeyWebSocketServer instance;

	private IKeyManager keyManager;

	/**
	 * Constructor for the LaotseWebSocketServer.
	 * 
	 * @param address
	 *            the address to listen to.
	 */
	private KeyWebSocketServer(InetSocketAddress address) {
		super(address);
		init();
	}

	private void init() {
		this.gson = new Gson();
		this.keyManager = KeyManager.getInstance();
	}

	/**
	 * Returns the LaotseWebSocketServer instance.
	 * 
	 * @return the websocketserver instance.
	 */
	public static KeyWebSocketServer getInstance() {
		// TODO config file?
		if (instance == null) {
			instance = new KeyWebSocketServer(new InetSocketAddress("localhost", 8887));
		}
		return instance;
	}

	@Override
	public void onClose(WebSocket conn, int arg1, String arg2, boolean arg3) {
		// TODO logger?
		System.out.println("Closed connection to " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println("an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex);
	}

	@Override
	public void onMessage(WebSocket conn, String json) {
		String[] values = gson.fromJson(json, String[].class);
		switch (values[0]) {
		case "StringMessage":
			StringMessage message = gson.fromJson(values[1], StringMessage.class);
			this.onStringMessage(conn, message);
			break;
		case "GetPublicKeyMessage":
			GetPublicKeyMessage message2 = gson.fromJson(values[1], GetPublicKeyMessage.class);
			this.onGetPublicKeyMessage(conn, message2);
			break;
		case "PublicKeyMessage":
			PublicKeyMessage message3 = gson.fromJson(values[1], PublicKeyMessage.class);
			this.onPublicKeyMessage(conn, message3);
			break;
		case "GetEncKeyMessage":
			GetEncKeyMessage message4 = gson.fromJson(values[1], GetEncKeyMessage.class);
			this.onGetEncKeyMessage(conn, message4);
			break;
		case "EncKeyMessage":
			EncKeyMessage message5 = gson.fromJson(values[1], EncKeyMessage.class);
			this.onEncKeyMessage(conn, message5);
			break;
		default:
			System.out.println("Unknown message in key server");
			break;
		}
	}

	private void onEncKeyMessage(WebSocket conn, EncKeyMessage message5) {
		if (message5 != null) {
			this.keyManager.setEncSymKey(message5.getEncKey());
		}
	}

	private void onGetEncKeyMessage(WebSocket conn, GetEncKeyMessage message4) {
		if (message4 != null) {
			EncKeyWrapper encKey = this.keyManager.getEncSymKey(message4.getReceiverId(), message4.getStreamId());
			EncKeyMessage encKeyMessage = new EncKeyMessage(encKey);
			String json = JsonUtils.getJsonString(encKeyMessage);
			conn.send(json);
		}
	}

	private void onPublicKeyMessage(WebSocket conn, PublicKeyMessage message3) {
		if (message3 != null) {
			this.keyManager.setPublicKey(message3.getPublicKey());
		}
	}

	private void onGetPublicKeyMessage(WebSocket conn, GetPublicKeyMessage message2) {
		if (message2 != null) {
			PublicKeyMessage pubKeyMessage = new PublicKeyMessage(this.keyManager.getPublicKey(message2.getId()));
			String json = JsonUtils.getJsonString(pubKeyMessage);
			conn.send(json);
		}
	}

	private void onStringMessage(WebSocket conn, StringMessage message) {
		System.out.println(message.getString());
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake arg1) {
		System.out.println("Connected to " + conn.getRemoteSocketAddress());
	}

	/**
	 * Sends a string to all connected clients.
	 * 
	 * @param json
	 *            the string.
	 */
	private void broadcast(String json) {
		Collection<WebSocket> sockets = connections();
		synchronized (sockets) {
			for (WebSocket socket : sockets) {
				socket.send(json);
			}
		}
	}

	/**
	 * Sends a String to all connected sockets except conn.
	 * 
	 * @param json
	 *            the string.
	 * @param conn
	 *            the websocket that should not receive json.
	 */
	private void broadcastExceptConn(WebSocket conn, String json) {
		Collection<WebSocket> sockets = connections();
		synchronized (sockets) {
			for (WebSocket socket : sockets) {
				if (socket != conn) {
					socket.send(json);
				}
			}
		}
	}

}
