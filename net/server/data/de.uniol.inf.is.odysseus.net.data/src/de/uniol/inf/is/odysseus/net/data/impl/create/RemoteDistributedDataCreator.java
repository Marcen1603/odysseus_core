package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataUpdatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.MultipleDistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.UpdateDistributedDataMessage;

public class RemoteDistributedDataCreator implements IDistributedDataCreator, IOdysseusNodeConnectionManagerListener {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteDistributedDataCreator.class);
	
	private final IOdysseusNodeCommunicator communicator;
	private final IOdysseusNodeConnectionManager connectionManager;

	private IOdysseusNode nodeWithContainer;

	public RemoteDistributedDataCreator(IOdysseusNodeCommunicator communicator, IOdysseusNodeConnectionManager connectionManager) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(connectionManager, "connectionManager must not be null!");

		this.communicator = communicator;
		this.connectionManager = connectionManager;
		this.connectionManager.addListener(this);

		updateConnection();
		
		LOG.info("Created remote distributed data creator");
	}

	@Override
	public void dispose() {
		this.connectionManager.removeListener(this);
		
		LOG.info("Remote distributed data creator disposed");
	}

	@Override
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent, long lifetime) throws DistributedDataException {
		checkConnection();

		CreateDistributedDataMessage msg = new CreateDistributedDataMessage(data, name, persistent, lifetime);

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
	public Optional<IDistributedData> update(OdysseusNodeID creator, UUID uuid, JSONObject data) throws DistributedDataException {
		checkConnection();
		
		UpdateDistributedDataMessage msg = new UpdateDistributedDataMessage(uuid, data);
		
		try {
			DistributedDataUpdatedMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, DistributedDataUpdatedMessage.class);
			return Optional.fromNullable(answer.getDistributedData());
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not send request and receive answer for updating distributed data", e);
		}
	}

	@Override
	public Optional<IDistributedData> destroy(OdysseusNodeID creator, UUID uuid) throws DistributedDataException {
		checkConnection();

		DestroyDistributedDataWithUUIDMessage msg = new DestroyDistributedDataWithUUIDMessage(uuid);

		try {
			DistributedDataDestroyedMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, DistributedDataDestroyedMessage.class);
			return Optional.of(answer.getDistributedData());
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not send request and receive answer for destroying distributed data", e);
		}
	}

	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, String name) throws DistributedDataException {
		checkConnection();

		DestroyDistributedDataWithNameMessage msg = new DestroyDistributedDataWithNameMessage(name);

		try {
			MultipleDistributedDataDestroyedMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, MultipleDistributedDataDestroyedMessage.class);
			return answer.getDistributedData();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not send request and receive answer for destroying distributed data", e);
		}
	}

	
	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, OdysseusNodeID id) throws DistributedDataException {
		checkConnection();
		
		DestroyDistributedDataWithNodeIDMessage msg = new DestroyDistributedDataWithNodeIDMessage(id);
		try {
			MultipleDistributedDataDestroyedMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, MultipleDistributedDataDestroyedMessage.class);
			return answer.getDistributedData();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not send request and receive answer for destroying own distributed data", e);
		}
	}
	
	@Override
	public void nodeConnected(IOdysseusNodeConnection connection) {
		if (nodeWithContainer == null) {
			nodeWithContainer = connection.getOdysseusNode();
			
			LOG.info("Got a node to use its distributed data container: {}", nodeWithContainer);
		}
	}

	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		if (nodeWithContainer == connection.getOdysseusNode()) {
			Optional<IOdysseusNodeConnection> optConnection = determineNewConnection();
			if (optConnection.isPresent()) {
				nodeWithContainer = optConnection.get().getOdysseusNode();
				LOG.info("Replaced node to use its distributed data container: {}", nodeWithContainer);
				
			} else {
				nodeWithContainer = null;
				
				LOG.info("No node to use its distributed data container found");
			}
		}
	}

	private void updateConnection() {
		Optional<IOdysseusNodeConnection> optConnection = determineNewConnection();
		if (optConnection.isPresent()) {
			nodeWithContainer = optConnection.get().getOdysseusNode();

			LOG.info("Got a node to use its distributed data container: {}", nodeWithContainer);
		} else {
			nodeWithContainer = null;
			
			LOG.info("No node to use its distributed data container found");
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

	private void checkConnection() throws DistributedDataException {
		if (nodeWithContainer == null) {
			updateConnection();
			if (nodeWithContainer == null) {
				throw new DistributedDataException("There is no remote node with a distributed data container");
			}
		}
	}

}
