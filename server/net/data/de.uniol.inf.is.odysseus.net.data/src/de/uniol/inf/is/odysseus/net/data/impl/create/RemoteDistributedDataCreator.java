package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.DistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.message.CreateDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;

public class RemoteDistributedDataCreator implements IDistributedDataCreator, IOdysseusNodeConnectionManagerListener, IOdysseusNodeCommunicatorListener {

	private static final long MAX_WAIT_TIME_MILLIS = 20 * 1000;

	private static int messageIDCounter = 0;

	private final IOdysseusNodeCommunicator communicator;
	private final IOdysseusNodeConnectionManager connectionManager;

	private final Object syncObject = new Object();

	private final Map<Integer, Object> answerMap = Maps.newHashMap();
	private final List<Integer> waitingMessages = Lists.newArrayList();

	private IOdysseusNode nodeWithContainer;

	public RemoteDistributedDataCreator(IOdysseusNodeCommunicator communicator, IOdysseusNodeConnectionManager connectionManager) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(connectionManager, "connectionManager must not be null!");

		this.communicator = communicator;
		this.communicator.addListener(this, DistributedDataCreatedMessage.class);

		this.connectionManager = connectionManager;
		this.connectionManager.addListener(this);
	}

	@Override
	public void dispose() {
		this.connectionManager.removeListener(this);
	}

	@Override
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent, long lifetime) throws DistributedDataException {
		int messageID = messageIDCounter++;

		CreateDistributedDataMessage msg = new CreateDistributedDataMessage(data, name, persistent, lifetime, messageID);

		synchronized (syncObject) {
			if (nodeWithContainer == null) {
				throw new DistributedDataException("No connection to remote distributed data container");
			}

			synchronized (waitingMessages) {
				waitingMessages.add(messageID);
			}

			try {
				communicator.send(nodeWithContainer, msg);
			} catch (OdysseusNodeCommunicationException e) {
				synchronized (waitingMessages) {
					waitingMessages.remove(messageID);
				}
				throw new DistributedDataException("Could not create distributed data remotely", e);
			}
		}

		Optional<IDistributedData> optAnswer = waitForAnswerMessage(messageID);
		if (optAnswer.isPresent()) {
			return optAnswer.get();
		}
		
		throw new DistributedDataException("Could not create distributed data successfully (maybe)");
	}

	@SuppressWarnings("unchecked")
	private <T> Optional<T> waitForAnswerMessage(int messageID) {
		long waitTime = 0;

		Object answer = null;
		do {
			synchronized (answerMap) {
				answer = answerMap.get(messageID);
			}

			if (answer == null) {
				waitSomeTime();
				waitTime += 100;
			}
		} while (answer == null && waitTime < MAX_WAIT_TIME_MILLIS);

		if (answer != null) {
			synchronized (answerMap) {
				answerMap.remove(messageID);
			}
		}

		return Optional.fromNullable((T) answer);
	}

	private void waitSomeTime() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent) throws DistributedDataException {
		return create(creator, data, name, persistent, -1);
	}

	@Override
	public Optional<IDistributedData> destroy(OdysseusNodeID creator, UUID uuid) {
		return null;
	}

	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, String name) {
		return null;
	}

	@Override
	public void nodeConnected(IOdysseusNodeConnection connection) {
		synchronized (syncObject) {
			if (nodeWithContainer == null) {
				nodeWithContainer = connection.getOdysseusNode();
			}
		}
	}

	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		synchronized (syncObject) {
			if (nodeWithContainer == connection.getOdysseusNode()) {
				Optional<IOdysseusNodeConnection> optConnection = determineNewConnection();
				if (optConnection.isPresent()) {
					nodeWithContainer = optConnection.get().getOdysseusNode();
				} else {
					nodeWithContainer = null;
				}
			}
		}
	}

	private Optional<IOdysseusNodeConnection> determineNewConnection() {
		Collection<IOdysseusNodeConnection> connections = connectionManager.getConnections();
		for (IOdysseusNodeConnection con : connections) {
			Optional<String> optProperty = con.getOdysseusNode().getProperty(DistributedDataManager.LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY);
			if (optProperty.isPresent()) {
				String property = optProperty.get();
				if (!Strings.isNullOrEmpty(property) && property.equalsIgnoreCase("true")) {
					return Optional.of(con);
				}
			}
		}
		return Optional.absent();
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof DistributedDataCreatedMessage) {
			DistributedDataCreatedMessage msg = (DistributedDataCreatedMessage) message;
			int messageID = msg.getRequestMessageID();

			synchronized (waitingMessages) {
				if (waitingMessages.contains(messageID)) {
					waitingMessages.remove(messageID);
				} else {
					return;
				}
			}

			synchronized (answerMap) {
				answerMap.put(messageID, msg.getDistributedData());
			}
		}
	}

}
