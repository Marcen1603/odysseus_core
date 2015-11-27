package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationUtils;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.DistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.message.CreateDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;

public class RemoteDistributedDataCreator implements IDistributedDataCreator, IOdysseusNodeConnectionManagerListener {

	private static int messageIDCounter = 0;

	private final IOdysseusNodeCommunicator communicator;
	private final IOdysseusNodeConnectionManager connectionManager;

	private IOdysseusNode nodeWithContainer;

	public RemoteDistributedDataCreator(IOdysseusNodeCommunicator communicator, IOdysseusNodeConnectionManager connectionManager) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(connectionManager, "connectionManager must not be null!");

		this.communicator = communicator;
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
		
		if( nodeWithContainer == null ) {
			throw new DistributedDataException("There is no remote node with a distributed data container");
		}

		CreateDistributedDataMessage msg = new CreateDistributedDataMessage(data, name, persistent, lifetime, messageID);

		try {
			DistributedDataCreatedMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, DistributedDataCreatedMessage.class);
			return answer.getDistributedData();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not send request and receive answer for creating distributed data", e);
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
		if (nodeWithContainer == null) {
			nodeWithContainer = connection.getOdysseusNode();
		}
	}

	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		if (nodeWithContainer == connection.getOdysseusNode()) {
			Optional<IOdysseusNodeConnection> optConnection = determineNewConnection();
			if (optConnection.isPresent()) {
				nodeWithContainer = optConnection.get().getOdysseusNode();
			} else {
				nodeWithContainer = null;
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
}
