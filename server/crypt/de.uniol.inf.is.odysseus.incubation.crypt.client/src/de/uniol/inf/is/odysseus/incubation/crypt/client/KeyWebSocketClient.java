package de.uniol.inf.is.odysseus.incubation.crypt.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.EncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetEncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetPublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.PublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.StringMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.JsonUtils;

/**
 * Client for the Laotse Gui to communicate with the LaotseWebSocketServer.
 *
 */
public class KeyWebSocketClient extends WebSocketClient {

	private static KeyWebSocketClient instance;
	private Gson gson;
	private IEncKeyReceiver encKeyListener;
	private IPubKeyReceiver pubKeyReceiver;

	/**
	 * Private Constructor. Initializes the client instance trying to connect to
	 * server with URI serverUri.
	 * 
	 * @param serverUri
	 *            the URI to connect to, referencing port.
	 */
	private KeyWebSocketClient(URI serverUri) {
		super(serverUri);
		this.gson = new Gson();
	}

	/**
	 * Returns the LaotseWebSocketClient instance.
	 * 
	 * @return the instance
	 * @throws WebSocketClientException
	 *             on Connect error.
	 */
	public static KeyWebSocketClient instance() throws Exception {
		if (instance == null || instance.getConnection().isClosed()) {
			System.out.println("Connecting to server");
			// TODO config file
			String serverUri = "ws://" + "localhost" + ":" + "8887";
			try {
				instance = new KeyWebSocketClient(new URI(serverUri));
			} catch (URISyntaxException ex) {
				throw new Exception("Uri Syntax invalid", ex);
			}
		}
		return instance;
	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		System.out.println("Connection to " + getURI() + " closed.");
	}

	@Override
	public void onError(Exception ex) {
		System.out.println("Error at: " + ex);
	}

	@Override
	public void onMessage(String json) {
		String[] values = gson.fromJson(json, String[].class);
		switch (values[0]) {
		case "EncKeyMessage":
			EncKeyMessage message = gson.fromJson(values[1], EncKeyMessage.class);
			this.encKeyListener.onEncKeyReceived(message.getEncKey());
			break;
		case "PublicKeyMessage":
			PublicKeyMessage message2 = gson.fromJson(values[1], PublicKeyMessage.class);
			this.pubKeyReceiver.onPubKeyReceived(message2.getPublicKey());
			break;
		default:
			System.out.println("Unknown message type: " + json);
			break;
		}
	}

	@Override
	public void onOpen(ServerHandshake arg0) {
		System.out.println("Connection to " + getURI() + " established.");
	}

	private void defaultSend(Object message) {
		if (this.getConnection().isOpen()) {
			String json = JsonUtils.getJsonString(message);
			this.send(json);
		} else {
			System.out.println("Could not connect to server!");
		}
	}

	public void sendStringMessage(String string) {
		StringMessage message = new StringMessage(string);
		this.defaultSend(message);
	}

	public void sendEncKeyMessage(EncKeyWrapper encKey) {
		EncKeyMessage message = new EncKeyMessage(encKey);
		this.defaultSend(message);
	}

	public void sendGetEncKeyMessage(int receiverId, int streamId) {
		GetEncKeyMessage message = new GetEncKeyMessage(receiverId, streamId);
		this.defaultSend(message);
	}

	public void sendGetPublicKeyMessage(int id) {
		GetPublicKeyMessage message = new GetPublicKeyMessage(id);
		this.defaultSend(message);
	}

	public void sendPublicKeyMessage(KeyWrapper<PublicKey> pubKey) {
		PublicKeyMessage message = new PublicKeyMessage(pubKey);
		this.defaultSend(message);
	}

	/**
	 * @return the encKeyListener
	 */
	public IEncKeyReceiver getEncKeyListener() {
		return encKeyListener;
	}

	/**
	 * @param encKeyListener
	 *            the encKeyListener to set
	 */
	public void setEncKeyListener(IEncKeyReceiver encKeyListener) {
		this.encKeyListener = encKeyListener;
	}

	/**
	 * @return the pubKeyReceiver
	 */
	public IPubKeyReceiver getPubKeyReceiver() {
		return pubKeyReceiver;
	}

	/**
	 * @param pubKeyReceiver
	 *            the pubKeyReceiver to set
	 */
	public void setPubKeyReceiver(IPubKeyReceiver pubKeyReceiver) {
		this.pubKeyReceiver = pubKeyReceiver;
	}

	// @Override
	// public void sendNewSensorMessage(Sensor sensor) {
	// if (this.getConnection().isOpen()) {
	// NewSensorMessage message = new NewSensorMessage();
	// message.setSensor(sensor);
	// String json = JsonUtils.getJsonString(message);
	// this.send(json);
	// } else {
	// Logger.getLogger(getClass()).error("Could not connect to server!");
	// }
	// }

}