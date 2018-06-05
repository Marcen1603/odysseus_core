package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.server;

import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.EncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetEncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetPublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.PublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.StringMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.IKeyManager;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.KeyManager;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.JsonUtils;

/**
 * A server to communicate with crypt Clients
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

	/**
	 * Initiates this server
	 */
	private void init() {
		this.gson = new Gson();
		this.keyManager = KeyManager.getInstance();
	}

	/**
	 * Returns the KeymanagementSocketServer instance.
	 *
	 * @return the websocketserver instance.
	 */
	public static KeyWebSocketServer getInstance() {
		if (instance == null) {
			instance = new KeyWebSocketServer(new InetSocketAddress(OdysseusConfiguration.instance.get("crypt.server.hostname"),
					Integer.parseInt(OdysseusConfiguration.instance.get("crypt.server.port"))));
		}
		return instance;
	}

	@Override
	public void onClose(WebSocket conn, int arg1, String arg2, boolean arg3) {
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

	/**
	 * On EncryptedKeyMessage received
	 *
	 * @param conn
	 *            The WebSocket, at which you received the message
	 * @param message5
	 *            The EncKeyMessage
	 */
	private void onEncKeyMessage(WebSocket conn, EncKeyMessage message5) {
		if (message5 != null) {
			this.keyManager.setEncSymKey(message5.getEncKey());
		}
	}

	/**
	 * On GetEncKeyMessage received
	 *
	 * @param conn
	 *            The WebSocket, at which you received the message
	 * @param message4
	 *            The GetEncKeyMessage
	 */
	private void onGetEncKeyMessage(WebSocket conn, GetEncKeyMessage message4) {
		if (message4 != null) {
			EncKeyWrapper encKey = this.keyManager.getEncSymKey(message4.getReceiverId(), message4.getStreamId());
			EncKeyMessage encKeyMessage = new EncKeyMessage(encKey, message4.getReceiver());
			String json = JsonUtils.getJsonString(encKeyMessage);
			conn.send(json);
		}
	}

	/**
	 * On PublicKeyMessage received
	 *
	 * @param conn
	 *            The WebSocket, at which you received the message
	 * @param message3
	 *            The PublicKeyMessage
	 */
	private void onPublicKeyMessage(WebSocket conn, PublicKeyMessage message3) {
		if (message3 != null) {
			for (int i = 0; i < message3.getPublicKey().size(); i++) {
				this.keyManager.setPublicKey(message3.getPublicKey().get(i));
			}
		}
	}

	/**
	 * On GetPublicKeyMessage Received
	 *
	 * @param conn
	 *            The WebSocket, at which you received the message
	 * @param message2
	 *            The GetPubkicKeyMessage
	 */
	private void onGetPublicKeyMessage(WebSocket conn, GetPublicKeyMessage message2) {
		if (message2 != null) {
			List<KeyWrapper<PublicKey>> pubKeys = this.keyManager.getPublicKey(message2.getId());
			PublicKeyMessage pubKeyMessage = new PublicKeyMessage(pubKeys);

			// workaround for interface
			// Gson gson = new
			// GsonBuilder().registerTypeAdapter(PublicKey.class, new
			// InterfaceAdapter<PublicKey>()).create();
			// String serialized = gson.toJson(pubKeyMessage);
			// String json = gson.toJson(new String[] {
			// pubKeyMessage.getClass().getSimpleName(), serialized });

			String json = JsonUtils.getJsonString(pubKeyMessage);
			conn.send(json);
		}
	}

	/**
	 * On String Message received
	 *
	 * @param conn
	 *            The WebSocket, at which you received the message
	 * @param message
	 *            The String message
	 */
	private void onStringMessage(WebSocket conn, StringMessage message) {
		System.out.println(message.getString());
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake arg1) {
		System.out.println("Connected to " + conn.getRemoteSocketAddress());
	}

}
