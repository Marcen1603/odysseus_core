package de.uniol.inf.is.odysseus.net.data.impl.container;

import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationUtils;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.DistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.AddDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.AddListenerMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.ModifiedDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.RemoveDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.RemoveListenerMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.BooleanMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsOdysseusNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCollectionMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetOdysseusNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.NamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OdysseusNodeIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OptionalDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestNamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestOdysseusNodeIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestUUIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.UUIDsMessage;

public class RemoteDistributedDataContainer implements IDistributedDataContainer, IOdysseusNodeConnectionManagerListener, IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteDistributedDataContainer.class);

	private final IOdysseusNodeCommunicator communicator;
	private final IOdysseusNodeConnectionManager connectionManager;
	private final IDistributedDataManager dataManager;
	private final Collection<IDistributedDataListener> listeners = Lists.newArrayList();

	private IOdysseusNode nodeWithContainer;

	public RemoteDistributedDataContainer(IDistributedDataManager dataManager, IOdysseusNodeCommunicator communicator, IOdysseusNodeConnectionManager connectionManager) {
		Preconditions.checkNotNull(dataManager, "dataManager must not be null!");
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

		this.dataManager = dataManager;

		this.communicator = communicator;
		this.communicator.addListener(this, AddDistributedDataMessage.class);
		this.communicator.addListener(this, ModifiedDistributedDataMessage.class);
		this.communicator.addListener(this, RemoveDistributedDataMessage.class);

		this.connectionManager = connectionManager;
		this.connectionManager.addListener(this);

		updateConnection();

		LOG.info("Remote distributed data container created");
	}

	@Override
	public void dispose() {
		this.communicator.removeListener(this, AddDistributedDataMessage.class);
		this.communicator.removeListener(this, ModifiedDistributedDataMessage.class);
		this.communicator.removeListener(this, RemoveDistributedDataMessage.class);

		this.connectionManager.removeListener(this);

		LOG.info("Remote distributed data container removed");
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
	public Collection<IDistributedData> remove(OdysseusNodeID nodeID) {
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
	public Collection<OdysseusNodeID> getOdysseusNodeIDs() throws DistributedDataException {
		RequestOdysseusNodeIDsMessage msg = new RequestOdysseusNodeIDsMessage();

		try {
			OdysseusNodeIDsMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, OdysseusNodeIDsMessage.class);
			return answer.getOdysseusNodeIDs();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not get odysseus node ids of distributed data", e);
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
	public Collection<IDistributedData> get(OdysseusNodeID nodeID) throws DistributedDataException {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");

		checkConnection();

		GetOdysseusNodeIDMessage msg = new GetOdysseusNodeIDMessage(nodeID);

		try {
			DistributedDataCollectionMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, DistributedDataCollectionMessage.class);
			return answer.getDistributedData();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not get distributed data from specified odysseus node id", e);
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
	public boolean containsOdysseusNodeID(OdysseusNodeID nodeID) throws DistributedDataException {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");

		checkConnection();

		ContainsOdysseusNodeIDMessage msg = new ContainsOdysseusNodeIDMessage(nodeID);
		try {
			BooleanMessage answer = OdysseusNodeCommunicationUtils.sendAndWaitForAnswer(communicator, nodeWithContainer, msg, BooleanMessage.class);
			return answer.getValue();
		} catch (OdysseusNodeCommunicationException e) {
			throw new DistributedDataException("Could not retrieve containment of distributed data from specified name", e);
		}
	}

	@Override
	public void nodeConnected(IOdysseusNodeConnection connection) {
		LOG.info("Node {} connected", connection.getOdysseusNode());
		if (nodeWithContainer == null) {
			nodeWithContainer = connection.getOdysseusNode();

			LOG.info("Selected node {} as active connection to remote distributed data container", nodeWithContainer);

			synchronized (listeners) {
				if (!listeners.isEmpty()) {
					trySendAddListenerMessage();
				}
			}
		}
	}

	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		LOG.info("Node {} disconnected", connection.getOdysseusNode());

		if (nodeWithContainer == connection.getOdysseusNode()) {
			Optional<IOdysseusNodeConnection> optConnection = determineNewConnection();
			if (optConnection.isPresent()) {
				nodeWithContainer = optConnection.get().getOdysseusNode();
				if (!listeners.isEmpty()) {
					trySendAddListenerMessage();
				}

				LOG.info("Replaced active connection to remote distributed data container with node {}", nodeWithContainer);
			} else {
				nodeWithContainer = null;
				LOG.info("Currently, no active connection to a remote distributed data container available");
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
			if (!listeners.isEmpty()) {
				trySendAddListenerMessage();
			}

			LOG.info("Updated connection to {}", nodeWithContainer);
		} else {
			nodeWithContainer = null;
			LOG.info("Currently, no active connection to a remote distributed data container available");
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
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);

				if (listeners.size() == 1) {
					trySendAddListenerMessage();
				}

				LOG.info("Added listener for remote distributed data container");
			}
		}
	}

	private void trySendAddListenerMessage() {
		if (nodeWithContainer != null) {
			try {
				communicator.send(nodeWithContainer, new AddListenerMessage());
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send add listener message to node {}", nodeWithContainer, e);
			}
		}
	}

	@Override
	public void removeListener(IDistributedDataListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			if (listeners.contains(listener)) {
				listeners.remove(listener);

				if (listeners.isEmpty()) {
					trySendRemoveListenerMessage();
				}

				LOG.info("Removed listener for remote distributed data container");
			}
		}
	}

	private void trySendRemoveListenerMessage() {
		if (nodeWithContainer != null) {
			try {
				communicator.send(nodeWithContainer, new RemoveListenerMessage());
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send remove listener message to node {}", nodeWithContainer, e);
			}
		}
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		LOG.debug("Received a message of type {}", message.getClass());

		if (message instanceof AddDistributedDataMessage) {
			AddDistributedDataMessage msg = (AddDistributedDataMessage) message;

			fireAddedEvent(msg.getDistributedData());
		} else if (message instanceof RemoveDistributedDataMessage) {
			RemoveDistributedDataMessage msg = (RemoveDistributedDataMessage) message;

			fireRemovedEvent(msg.getDistributedData());
		} else if (message instanceof ModifiedDistributedDataMessage) {
			ModifiedDistributedDataMessage msg = (ModifiedDistributedDataMessage) message;

			fireModifiedEvent(msg.getOldData(), msg.getNewData());
		} else {
			LOG.warn("Got a message of unknown type: {}", message);
		}
	}

	private void fireAddedEvent(IDistributedData addedData) {
		synchronized (listeners) {
			for (IDistributedDataListener listener : listeners) {
				try {
					listener.distributedDataAdded(dataManager, addedData);
				} catch (Throwable t) {
					LOG.error("Exception in distributed data listener", t);
				}
			}
		}
	}

	private void fireModifiedEvent(IDistributedData oldData, IDistributedData newData) {
		synchronized (listeners) {
			for (IDistributedDataListener listener : listeners) {
				try {
					listener.distributedDataModified(dataManager, oldData, newData);
				} catch (Throwable t) {
					LOG.error("Exception in distributed data listener", t);
				}
			}
		}
	}

	private void fireRemovedEvent(IDistributedData removedData) {
		synchronized (listeners) {
			for (IDistributedDataListener listener : listeners) {
				try {
					listener.distributedDataRemoved(dataManager, removedData);
				} catch (Throwable t) {
					LOG.error("Exception in distributed data listener", t);
				}
			}
		}
	}
}
