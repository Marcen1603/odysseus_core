package de.uniol.inf.is.odysseus.net.communication.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionListener;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.connect.OdysseusNetConnectionException;

public class OdysseusNodeCommunicator implements IOdysseusNodeCommunicator, IOdysseusNodeConnectionManagerListener, IOdysseusNodeConnectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNodeCommunicator.class);

	private static IOdysseusNodeConnectionManager connectionManager;
	
	private final Collection<IOdysseusNode> destinationNodes = Lists.newArrayList();

	private final Map<Class<? extends IMessage>, Integer> messageTypeMap = Maps.newHashMap();
	private final Map<Integer, Class<? extends IMessage>> messageIDMap = Maps.newHashMap();

	// called by OSGi-DS
	public static void bindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		connectionManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		if (connectionManager == serv) {
			connectionManager = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		for (IOdysseusNodeConnection connection : connectionManager.getConnections()) {
			nodeConnected(connection);
		}

		connectionManager.addListener(this);

		LOG.info("OdysseusNodeCommunicator activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		connectionManager.removeListener(this);
		for (IOdysseusNodeConnection connection : connectionManager.getConnections()) {
			nodeDisconnected(connection);
		}

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
	public void send(IOdysseusNode destinationNode, IMessage message) throws OdysseusNodeCommunicationException {
		Preconditions.checkNotNull(destinationNode, "destinationNode must not be null!");
		Preconditions.checkNotNull(message, "message must not be null!");
		
		if( destinationNode.isLocal() ) {
			Collection<IOdysseusNodeCommunicatorListener> listeners = OdysseusNodeCommunicatorListenerRegistry.getInstance().getListeners(message.getClass());
			if (!listeners.isEmpty()) {
				fireMessageReceivedEvent(destinationNode, listeners, message);
			}
			return;
		}

		if (!connectionManager.isConnected(destinationNode)) {
			throw new OdysseusNodeCommunicationException("Node " + destinationNode + " is not connected");
		}

		Class<? extends IMessage> messageType = message.getClass();
		Integer messageID = messageTypeMap.get(messageType);
		if (messageID == null) {
			throw new OdysseusNodeCommunicationException("MessageType " + messageType + " is not registered");
		}
		byte[] messageData = message.toBytes();
		if (messageData == null) {
			throw new OdysseusNodeCommunicationException("Message " + message.toString() + " has returned null as bytes!");
		}

		byte[] payload = new byte[messageData.length + 4];
		insertInt(payload, 0, messageID);
		System.arraycopy(messageData, 0, payload, 4, messageData.length);

		Optional<IOdysseusNodeConnection> optConnection = connectionManager.getConnection(destinationNode);
		if (!optConnection.isPresent()) {
			throw new OdysseusNodeCommunicationException("Could not send message to non-connected node " + destinationNode);
		}
		IOdysseusNodeConnection connection = optConnection.get();
		try {
			connection.send(payload);
		} catch (OdysseusNetConnectionException e) {
			throw new OdysseusNodeCommunicationException("Could not send message to node " + destinationNode, e);
		}
	}

	@Override
	public void addListener(IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType) {
		if (!messageTypeMap.containsKey(messageType)) {
			throw new RuntimeException("Listener cannot be registered for unknown message type " + messageType);
		}

		OdysseusNodeCommunicatorListenerRegistry.getInstance().add(listener, messageType);
	}

	@Override
	public void removeListener(IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType) {
		OdysseusNodeCommunicatorListenerRegistry.getInstance().remove(listener, messageType);
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

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	private static int byteArrayToInt(byte[] b, int offset) {
		return b[3 + offset] & 0xFF | (b[2 + offset] & 0xFF) << 8 | (b[1 + offset] & 0xFF) << 16 | (b[0 + offset] & 0xFF) << 24;
	}

	// called by odysseus node connection manager
	@Override
	public void nodeConnected(IOdysseusNodeConnection connection) {
		connection.addListener(this);
		
		synchronized( destinationNodes ) {
			destinationNodes.add(connection.getOdysseusNode());
		}
	}

	// called by odysseus node connection manager
	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		connection.removeListener(this);
		
		synchronized(destinationNodes) {
			destinationNodes.remove(connection.getOdysseusNode());
		}
	}

	// called by one node connection
	@Override
	public void messageReceived(IOdysseusNodeConnection connection, byte[] data) {
		try {
			if (data != null && data.length >= 4) {
				int msgId = byteArrayToInt(data, 0);
				Collection<IOdysseusNodeCommunicatorListener> listeners = OdysseusNodeCommunicatorListenerRegistry.getInstance().getListeners(messageIDMap.get(msgId));
				if (!listeners.isEmpty()) {
					Optional<IMessage> optMsg = createNewMessageInstance(msgId);
					if (optMsg.isPresent()) {

						byte[] msgBytes = new byte[data.length - 4];
						System.arraycopy(data, 4, msgBytes, 0, msgBytes.length);

						IMessage msg = optMsg.get();
						msg.fromBytes(msgBytes);

						fireMessageReceivedEvent(connection.getOdysseusNode(), listeners, msg);
					}
				}
			} else {
				LOG.warn("Got message with too few bytes from node {}", connection.getOdysseusNode());
			}
		} catch (Throwable t) {
			LOG.error("Exception during processing incoming message", t);
			throw t;
		}
	}

	private void fireMessageReceivedEvent(IOdysseusNode node, Collection<IOdysseusNodeCommunicatorListener> listeners, IMessage msg) {
		for (IOdysseusNodeCommunicatorListener listener : listeners) {
			try {
				listener.receivedMessage(this, node, msg);
			} catch (Throwable t) {
				LOG.error("Exception in odysseus node communicator listener", t);
			}
		}
	}

	// called by one node connection
	@Override
	public void disconnected(IOdysseusNodeConnection connection) {
		connection.removeListener(this);
	}

	@Override
	public Collection<IOdysseusNode> getDestinationNodes() {
		synchronized( destinationNodes ) {
			return Lists.newArrayList(destinationNodes);
		}
	}
	
	@Override
	public boolean canSend(IOdysseusNode node) {
		synchronized(destinationNodes ) {
			return destinationNodes.contains(node);
		}
	}
}
