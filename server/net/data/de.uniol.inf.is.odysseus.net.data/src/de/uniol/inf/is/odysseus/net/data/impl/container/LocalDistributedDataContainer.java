package de.uniol.inf.is.odysseus.net.data.impl.container;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.impl.DistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.AddDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.RemoveDistributedDataMessage;

public class LocalDistributedDataContainer implements IDistributedDataContainer, IOdysseusNodeConnectionManagerListener, IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(LocalDistributedDataContainer.class);

	private final Map<String, Collection<IDistributedData>> ddNameMap = Maps.newHashMap();
	private final Map<UUID, IDistributedData> ddUUIDMap = Maps.newHashMap();

	private final Object syncObject = new Object();
	private final IOdysseusNodeConnectionManager connectionManager;
	private final IOdysseusNodeCommunicator communicator;

	private final Collection<IOdysseusNode> otherContainers = Lists.newArrayList();
	private final Collection<IDistributedDataListener> listeners = Lists.newArrayList();

	public LocalDistributedDataContainer(IOdysseusNodeCommunicator communicator, IOdysseusNodeConnectionManager connectionManager) {
		Preconditions.checkNotNull(connectionManager, "connectionManager must not be null!");
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

		this.connectionManager = connectionManager;
		for (IOdysseusNodeConnection connection : this.connectionManager.getConnections()) {
			nodeConnected(connection);
		}

		this.communicator = communicator;
		this.communicator.registerMessageType(AddDistributedDataMessage.class);
		this.communicator.registerMessageType(RemoveDistributedDataMessage.class);

		this.communicator.addListener(this, AddDistributedDataMessage.class);
		this.communicator.addListener(this, RemoveDistributedDataMessage.class);

		this.connectionManager.addListener(this);

		LOG.info("Local distributed data container created");
	}

	@Override
	public void dispose() {
		connectionManager.removeListener(this);

		communicator.removeListener(this, AddDistributedDataMessage.class);
		communicator.removeListener(this, RemoveDistributedDataMessage.class);

		communicator.unregisterMessageType(AddDistributedDataMessage.class);
		communicator.unregisterMessageType(RemoveDistributedDataMessage.class);

		LOG.info("Local distributed data container disposed");
	}

	@Override
	public void add(IDistributedData data) {
		Preconditions.checkNotNull(data, "data must not be null!");
		UUID uuid = data.getUUID();
		long ts = data.getTimestamp();
		String name = data.getName();

		synchronized (syncObject) {
			LOG.info("Trying to add distributed data {}", data);
			if (ddUUIDMap.containsKey(uuid)) {
				IDistributedData oldData = ddUUIDMap.get(uuid);
				long oldts = oldData.getTimestamp();
				if (oldts < ts) {
					ddUUIDMap.put(uuid, data);

					Collection<IDistributedData> dataCollection = ddNameMap.get(name);
					dataCollection.remove(oldData);
					dataCollection.add(data);

					LOG.info("Distributed data added with younger timestamp");

					sendMessageToOtherContainers(new AddDistributedDataMessage(data));
					fireModifiedEvent(oldData, data);
					
				} else {
					LOG.info("Timestamp of distributed data is too old");
				}

			} else {
				ddUUIDMap.put(uuid, data);

				Collection<IDistributedData> dataCollection = ddNameMap.get(name);
				if (dataCollection == null) {
					dataCollection = Lists.newArrayList();
					ddNameMap.put(name, dataCollection);
				}
				dataCollection.add(data);
				LOG.info("Distributed data added as new element");

				sendMessageToOtherContainers(new AddDistributedDataMessage(data));
				fireAddedEvent(data);
			}
		}
	}

	@Override
	public Optional<IDistributedData> remove(IDistributedData data) {
		Preconditions.checkNotNull(data, "data must not be null!");

		synchronized (syncObject) {
			LOG.info("Trying to remove distributed data {}", data);

			Collection<IDistributedData> dataCollection = ddNameMap.get(data.getName());
			if (dataCollection != null) {
				dataCollection.remove(data);
				if (dataCollection.isEmpty()) {
					ddNameMap.remove(data.getName());
				}
			}

			IDistributedData data2 = ddUUIDMap.get(data.getUUID());
			if (data2 != null) {
				ddUUIDMap.remove(data2.getUUID());

				LOG.info("Distributed data removed");

				sendMessageToOtherContainers(new RemoveDistributedDataMessage(data));
				fireRemovedEvent(data);
				return Optional.of(data2);
			}

		}

		return Optional.absent();
	}

	@Override
	public Optional<IDistributedData> remove(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		synchronized (syncObject) {
			IDistributedData oldData = ddUUIDMap.get(uuid);
			if (oldData != null) {
				return remove(oldData);
			}
		}

		return Optional.absent();
	}

	@Override
	public Collection<IDistributedData> remove(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		Collection<IDistributedData> removedData = Lists.newArrayList();

		synchronized (syncObject) {
			Collection<IDistributedData> dataCollection = Lists.newArrayList(ddNameMap.get(name));
			if (dataCollection != null) {
				for (IDistributedData data : dataCollection) {
					remove(data);
				}

				removedData.addAll(dataCollection);
			}
		}

		return removedData;
	}

	@Override
	public Collection<UUID> getUUIDs() {
		synchronized (syncObject) {
			return Lists.newArrayList(ddUUIDMap.keySet());
		}
	}

	@Override
	public Collection<String> getNames() {
		synchronized (syncObject) {
			return Lists.newArrayList(ddNameMap.keySet());
		}
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		synchronized (syncObject) {
			return Optional.fromNullable(ddUUIDMap.get(uuid));
		}
	}

	@Override
	public Collection<IDistributedData> get(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		synchronized (syncObject) {
			Collection<IDistributedData> dataCollection = ddNameMap.get(name);
			if (dataCollection == null || dataCollection.isEmpty()) {
				return Lists.newArrayList();
			}

			return Lists.newArrayList(dataCollection);
		}
	}

	@Override
	public boolean containsUUID(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		synchronized (syncObject) {
			return ddUUIDMap.containsKey(uuid);
		}
	}

	@Override
	public boolean containsName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		synchronized (syncObject) {
			return ddNameMap.containsKey(name);
		}
	}

	@Override
	public void nodeConnected(IOdysseusNodeConnection connection) {
		IOdysseusNode node = connection.getOdysseusNode();
		Optional<String> optProperty = node.getProperty(DistributedDataManager.LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY);
		if (optProperty.isPresent()) {
			String property = optProperty.get();
			if (property.equalsIgnoreCase("true")) {

				synchronized (otherContainers) {
					otherContainers.add(node);

					LOG.info("Added node {} as new remote container", node);
				}

				synchronized (syncObject) {
					if (!ddUUIDMap.isEmpty()) {
						for (IDistributedData data : ddUUIDMap.values()) {
							try {
								communicator.send(node, new AddDistributedDataMessage(data));
							} catch (OdysseusNodeCommunicationException e) {
								LOG.error("Could not send add message to node {}", node);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		IOdysseusNode node = connection.getOdysseusNode();
		synchronized (otherContainers) {
			if (otherContainers.contains(node)) {
				otherContainers.remove(node);

				LOG.info("Removed node as other container {}", node);
			}
		}

	}

	private void sendMessageToOtherContainers(IMessage message) {
		synchronized (otherContainers) {
			if (!otherContainers.isEmpty()) {
				LOG.debug("Sending message {} to other containers", message);

				for (IOdysseusNode otherContainer : otherContainers) {
					try {
						communicator.send(otherContainer, message);
					} catch (OdysseusNodeCommunicationException e) {
						LOG.error("Could not send message {} to other container {}", new Object[] { message, otherContainer, e });
					}
				}
			}
		}
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof AddDistributedDataMessage) {
			AddDistributedDataMessage msg = (AddDistributedDataMessage) message;

			LOG.info("Adding distribted data remotely from {}", senderNode);

			add(msg.getDistributedData());
		} else if (message instanceof RemoveDistributedDataMessage) {
			RemoveDistributedDataMessage msg = (RemoveDistributedDataMessage) message;

			LOG.info("Removing distributed data remotely from {}", senderNode);

			remove(msg.getDistributedData());
		}
	}
	
	@Override
	public void addListener(IDistributedDataListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener(IDistributedDataListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	private void fireAddedEvent( IDistributedData addedData) {
		synchronized( listeners ) {
			for( IDistributedDataListener listener : listeners ) {
				try {
					listener.distributedDataAdded(addedData);
				} catch( Throwable t ) {
					LOG.error("Exception in distributed data listener", t);
				}
			}
		}
	}
	
	private void fireModifiedEvent( IDistributedData oldData, IDistributedData newData) {
		synchronized( listeners ) {
			for( IDistributedDataListener listener : listeners ) {
				try {
					listener.distributedDataModified(oldData, newData);
				} catch( Throwable t ) {
					LOG.error("Exception in distributed data listener", t);
				}
			}
		}
	}
	
	private void fireRemovedEvent( IDistributedData removedData) {
		synchronized( listeners ) {
			for( IDistributedDataListener listener : listeners ) {
				try {
					listener.distributedDataRemoved(removedData);
				} catch( Throwable t ) {
					LOG.error("Exception in distributed data listener", t);
				}
			}
		}
	}
}
