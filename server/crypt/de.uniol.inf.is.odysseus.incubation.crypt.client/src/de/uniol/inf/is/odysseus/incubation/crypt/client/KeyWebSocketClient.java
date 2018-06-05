package de.uniol.inf.is.odysseus.incubation.crypt.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.EncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetEncKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.GetPublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.PublicKeyMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.common.messages.StringMessage;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.EncKeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util.JsonUtils;

/**
 * Client for the Cryptor to communicate with the KeymanagementServer.
 *
 */
public class KeyWebSocketClient extends WebSocketClient {

	private static KeyWebSocketClient instance;
	private Gson gson;
	private List<IEncKeyReceiver> encKeyListener;
	private List<IPubKeyReceiver> pubKeyReceiver;

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
		this.encKeyListener = new ArrayList<>();
		this.pubKeyReceiver = new ArrayList<>();
		this.connect();
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
			try {
				instance = new KeyWebSocketClient(new URI(OdysseusConfiguration.instance.get("crypt.client.uri")));
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
			for (IEncKeyReceiver encKeyListener : this.encKeyListener) {
				encKeyListener.onEncKeyReceived(message.getEncKey(), message.getReceiver());
			}
			break;
		case "PublicKeyMessage":
			PublicKeyMessage message2 = gson.fromJson(values[1], PublicKeyMessage.class);
			for (IPubKeyReceiver pubKeyReceiver : this.pubKeyReceiver) {
				pubKeyReceiver.onPubKeyReceived(message2.getPublicKey());
			}
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
		// if (!this.getConnection().isOpen()) {
		// // try to connect
		// this.connect();
		// }
		if (this.getConnection().isOpen()) {
			String json = JsonUtils.getJsonString(message);
			this.send(json);
		} else {
			System.out.println("Could not connect to server!");
		}
	}

	/**
	 * Send a String to the server
	 * 
	 * @param string
	 *            The String
	 */
	public void sendStringMessage(String string) {
		StringMessage message = new StringMessage(string);
		this.defaultSend(message);
	}

	/**
	 * Send an EncKeyWrapper to the Server
	 * 
	 * @param encKey
	 *            The EncKeyWrapper
	 */
	public void sendEncKeyMessage(EncKeyWrapper encKey) {
		// this will send to the database, so there is no specific receiver of
		// this message
		EncKeyMessage message = new EncKeyMessage(encKey, 0);
		this.defaultSend(message);
	}

	/**
	 * Send a GetEncKeyMessage to the Server
	 * 
	 * @param receiverId
	 *            The id of the receiver of the EncKey
	 * @param streamId
	 *            The streamID of the crypting with the encrypted Key
	 * @param receiverHash
	 *            The HashCode of the receiver od the encrypted symmetric key
	 */
	public void sendGetEncKeyMessage(int receiverId, int streamId, int receiverHash) {
		GetEncKeyMessage message = new GetEncKeyMessage(receiverId, streamId, receiverHash);
		this.defaultSend(message);
	}

	/**
	 * Send a GetPublicKeyMessage to the server
	 * 
	 * @param id
	 *            The List of ids of the PubKeys to receive
	 */
	public void sendGetPublicKeyMessage(List<Integer> id) {
		GetPublicKeyMessage message = new GetPublicKeyMessage();
		for (Integer i : id) {
			message.addId(i);
		}
		this.defaultSend(message);
	}

	/**
	 * Send a Public Key to the server
	 * 
	 * @param pubKey
	 *            The public Key
	 */
	public void sendPublicKeyMessage(List<KeyWrapper<PublicKey>> pubKey) {
		PublicKeyMessage message = new PublicKeyMessage(pubKey);
		this.defaultSend(message);
	}

	/**
	 * Returns the EncKeyListener
	 * 
	 * @return the encKeyListener
	 */
	public List<IEncKeyReceiver> getEncKeyListener() {
		return encKeyListener;
	}

	/**
	 * Adds a EncKeyListener
	 * 
	 * @param encKeyListener
	 *            the encKeyListener to add
	 */
	public void addEncKeyListener(IEncKeyReceiver encKeyListener) {
		this.encKeyListener.add(encKeyListener);
	}

	/**
	 * Returns the PubKeyReceiver
	 * 
	 * @return the pubKeyReceiver
	 */
	public List<IPubKeyReceiver> getPubKeyReceiver() {
		return pubKeyReceiver;
	}

	/**
	 * Adds a pubKeyReceiver
	 * 
	 * @param pubKeyReceiver
	 *            the pubKeyReceiver to add
	 */
	public void addPubKeyReceiver(IPubKeyReceiver pubKeyReceiver) {
		this.pubKeyReceiver.add(pubKeyReceiver);
	}

}