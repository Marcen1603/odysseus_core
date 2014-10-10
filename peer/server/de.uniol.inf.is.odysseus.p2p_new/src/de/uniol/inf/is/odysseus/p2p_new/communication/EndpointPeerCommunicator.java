package de.uniol.inf.is.odysseus.p2p_new.communication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointListener;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.Messenger;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionaryAdapter;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;

public class EndpointPeerCommunicator extends P2PDictionaryAdapter implements IPeerCommunicator, EndpointListener {

	private static final String COMMUNICATION_SERVICE_ID = "odysseusP2PComm";

	private static final Logger LOG = LoggerFactory.getLogger(EndpointPeerCommunicator.class);

	private static IP2PDictionary p2pDictionary;

	private final Map<PeerID, Messenger> messengerMap = Maps.newHashMap();
	private final Map<Class<? extends IMessage>, Integer> messageTypeMap = Maps.newHashMap();
	private final Map<Integer, Class<? extends IMessage>> messageIDMap = Maps.newHashMap();

	// called by OSGi-DS
	public void bindP2PDictionary(IP2PDictionary dict) {
		p2pDictionary = dict;
		LOG.debug("Bound P2PDictionary {}", dict);

		p2pDictionary.addListener(this);
	}

	// called by OSGi-DS
	public void unbindP2PDictionary(IP2PDictionary dict) {
		if (dict == p2pDictionary) {
			p2pDictionary.removeListener(this);

			LOG.debug("Unbound P2PDictionary {}", dict);
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		Thread waitingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				JxtaServicesProvider.waitFor();
				JxtaServicesProvider.getInstance().getEndpointService().addIncomingMessageListener(EndpointPeerCommunicator.this, COMMUNICATION_SERVICE_ID, null);
			}
		});
		waitingThread.setName("Endpoint peer communicator waiting thread");
		waitingThread.setDaemon(true);
		waitingThread.start();

		LOG.debug("Activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		messengerMap.clear();
		messageTypeMap.clear();
		messageIDMap.clear();

		LOG.debug("Deactivated");
	}

	@Override
	public void registerMessageType(Class<? extends IMessage> messageType) {
		Preconditions.checkNotNull(messageType, "MessageType must not be null!");
		Preconditions.checkArgument(hasDefaultConstructor(messageType), "MessageType %s has no default constructor which is needed!", messageType);

		int messageID = messageType.toString().hashCode();
		messageTypeMap.put(messageType, messageID);
		messageIDMap.put(messageID, messageType);
	}

	private static boolean hasDefaultConstructor(Class<? extends IMessage> messageType) {
		try {
			messageType.newInstance();
			return true;
		} catch (InstantiationException | IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public void unregisterMessageType(Class<? extends IMessage> messageType) {
		Preconditions.checkNotNull(messageType, "MessageType must not be null!");

		Integer msgID = messageTypeMap.remove(messageType);
		if (msgID != null) {
			messageIDMap.remove(msgID);
		}
	}

	@Override
	public void send(PeerID destinationPeer, IMessage message) throws PeerCommunicationException {
		Messenger messenger = messengerMap.get(destinationPeer);
		if (messenger == null) {
			EndpointAddress addr = new EndpointAddress(destinationPeer, null, null);
			messenger = JxtaServicesProvider.getInstance().getEndpointService().getMessenger(addr);
			if (messenger == null) {
				throw new PeerCommunicationException("Wanted to send message to unknown peer " + destinationPeer);
			}

			messengerMap.put(destinationPeer, messenger);
		}

		if (messenger.isClosed()) {
			messengerMap.remove(destinationPeer);
			throw new PeerCommunicationException("Tried to send message with closed messenger (e.g. lost peer): " + destinationPeer);
		}

		Class<? extends IMessage> messageType = message.getClass();
		Integer messageID = messageTypeMap.get(messageType);
		if (messageID == null) {
			throw new PeerCommunicationException("MessageType " + messageType + " is not registered");
		}
		byte[] messageData = message.toBytes();
		if (messageData == null) {
			throw new PeerCommunicationException("Message " + message.toString() + " has returned null as bytes!");
		}

		Message msgToSend = new Message();
		byte[] payload = new byte[messageData.length + 4];
		insertInt(payload, 0, messageID);
		System.arraycopy(messageData, 0, payload, 4, messageData.length);

		msgToSend.addMessageElement(new ByteArrayMessageElement("bytes", null, payload, null));

		try {
			messenger.sendMessage(msgToSend, COMMUNICATION_SERVICE_ID, null);
		} catch (IOException e) {
			throw new PeerCommunicationException("Could not send message", e);
		}
	}

	@Override
	public void addListener(IPeerCommunicatorListener listener, Class<? extends IMessage> messageType) {
		if (!messageTypeMap.containsKey(messageType)) {
			throw new RuntimeException("Listener cannot be registered for unknown message type " + messageType);
		}

		PeerCommunicatorListenerRegistry.getInstance().add(listener, messageType);
	}

	@Override
	public void removeListener(IPeerCommunicatorListener listener, Class<? extends IMessage> messageType) {
		PeerCommunicatorListenerRegistry.getInstance().remove(listener, messageType);
	}

	@Override
	public void processIncomingMessage(Message message, EndpointAddress srcAddr, EndpointAddress dstAddr) {
		try {
			PeerID pid = (PeerID) toID("urn:jxta:" + srcAddr.getProtocolAddress());
	
			ByteArrayMessageElement messageElement = (ByteArrayMessageElement) message.getMessageElement("bytes");
			byte[] data = messageElement.getBytes();
	
			if (data != null && data.length >= 4) {
				int msgId = byteArrayToInt(data, 0);
				Collection<IPeerCommunicatorListener> listeners = PeerCommunicatorListenerRegistry.getInstance().getListeners(messageIDMap.get(msgId));
				if (!listeners.isEmpty()) {
	
					Optional<IMessage> optMsg = createNewMessageInstance(msgId);
					if (optMsg.isPresent()) {
	
						byte[] msgBytes = new byte[data.length - 4];
						System.arraycopy(data, 4, msgBytes, 0, msgBytes.length);
	
						IMessage msg = optMsg.get();
						msg.fromBytes(msgBytes);
	
						for (IPeerCommunicatorListener listener : listeners) {
							try {
								listener.receivedMessage(this, pid, msg);
							} catch (Throwable t) {
								LOG.error("Exception in peer communicator listener", t);
							}
						}
					}
				}
			} else {
				LOG.warn("Got message with too few bytes from " + p2pDictionary.getRemotePeerName(pid));
			}
		} catch( Throwable t ) {
			LOG.error("Exception during processing incoming message", t);
			throw t;
		}
	}

	private Optional<IMessage> createNewMessageInstance(int msgId) {
		Class<? extends IMessage> messageType = messageIDMap.get(msgId);
		if (messageType == null) {
			LOG.error("Got message of unknown type: " + msgId);
			return Optional.absent();
		}

		try {
			return Optional.<IMessage> of(messageType.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Could not create message of type {}", messageType, e);
			return Optional.absent();
		}
	}

	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	private static int byteArrayToInt(byte[] b, int offset) {
		return b[3 + offset] & 0xFF | (b[2 + offset] & 0xFF) << 8 | (b[1 + offset] & 0xFF) << 16 | (b[0 + offset] & 0xFF) << 24;
	}
}
