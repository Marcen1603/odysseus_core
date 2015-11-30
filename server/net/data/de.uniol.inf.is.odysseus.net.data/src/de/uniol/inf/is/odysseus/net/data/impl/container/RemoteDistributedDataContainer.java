package de.uniol.inf.is.odysseus.net.data.impl.container;

import java.util.Collection;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationUtils;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.impl.DistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.message.BooleanMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCollectionMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.NamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OptionalDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestNamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestUUIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.UUIDsMessage;

public class RemoteDistributedDataContainer implements IDistributedDataContainer, IOdysseusNodeConnectionManagerListener {

	private final IOdysseusNodeCommunicator communicator;
	private final IOdysseusNodeConnectionManager connectionManager;

	private IOdysseusNode nodeWithContainer;

	public RemoteDistributedDataContainer(IOdysseusNodeCommunicator communicator, IOdysseusNodeConnectionManager connectionManager) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

		this.communicator = communicator;
		this.connectionManager = connectionManager;
		this.connectionManager.addListener(this);

		updateConnection();
	}

	@Override
	public void dispose() {
		this.connectionManager.removeListener(this);
	}

	@Override
	public void add(IDistributedData data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<IDistributedData> remove(IDistributedData data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<IDistributedData> remove(UUID uuid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<IDistributedData> remove(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<UUID> getUUIDs() throws DistributedDataException {
		checkConnection();

		RequestUUIDsMessage msg = new RequestUUIDsMessage();
		try {
			UUIDsMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, UUIDsMessage.class);
			return answer.getUUIDs();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not get uuids of distributed data", e);
		}
	}

	@Override
	public Collection<String> getNames() throws DistributedDataException {
		checkConnection();
		
		RequestNamesMessage msg = new RequestNamesMessage();
		
		try {
			NamesMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, NamesMessage.class);
			return answer.getNames();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not get names of distributed data", e);
		}
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) throws DistributedDataException {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		checkConnection();
		
		GetUUIDMessage msg = new GetUUIDMessage(uuid);
		try {
			OptionalDistributedDataMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, OptionalDistributedDataMessage.class);
			return answer.getDistributedData();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not get distributed data from specified uuid", e);
		}
	}

	@Override
	public Collection<IDistributedData> get(String name) throws DistributedDataException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		checkConnection();
		
		GetNameMessage msg = new GetNameMessage(name);
		
		try {
			DistributedDataCollectionMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, DistributedDataCollectionMessage.class);
			return answer.getDistributedData();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not get distributed data from specified name", e);
		}
	}

	@Override
	public boolean containsUUID(UUID uuid) throws DistributedDataException {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		checkConnection();
		
		ContainsUUIDMessage msg = new ContainsUUIDMessage(uuid);
		try {
			BooleanMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, BooleanMessage.class);
			return answer.getValue();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not retrieve containment of distributed data from specified uuid", e);
		}
	}

	@Override
	public boolean containsName(String name) throws DistributedDataException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		checkConnection();
		
		ContainsNameMessage msg = new ContainsNameMessage(name);
		try {
			BooleanMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, BooleanMessage.class);
			return answer.getValue();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not retrieve containment of distributed data from specified name", e);
		}
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

	private void updateConnection() {
		Optional<IOdysseusNodeConnection> optConnection = determineNewConnection();
		if (optConnection.isPresent()) {
			nodeWithContainer = optConnection.get().getOdysseusNode();
		} else {
			nodeWithContainer = null;
		}
	}

	private void checkConnection() throws DistributedDataException {
		if (nodeWithContainer == null) {
			updateConnection();
			if (nodeWithContainer == null) {
				throw new DistributedDataException("There is no remote node with a distributed data container");
			}
		}
	}
	
	@Override
	public void addListener(IDistributedDataListener listener) {
		// TODO
	}
	
	@Override
	public void removeListener(IDistributedDataListener listener) {
		// TODO
	}
}
