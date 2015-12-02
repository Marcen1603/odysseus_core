package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataListener;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.container.LocalDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.container.RemoteDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.AddDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.AddListenerMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.ModifiedDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.RemoveDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.container.message.RemoveListenerMessage;
import de.uniol.inf.is.odysseus.net.data.impl.create.LocalDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.create.RemoteDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.message.BooleanMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsOdysseusNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.CreateDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCollectionMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetOdysseusNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.MultipleDistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.NamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OdysseusNodeIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OptionalDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestNamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestOdysseusNodeIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestUUIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.UUIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.server.DistributedDataServer;

public class DistributedDataManager extends OdysseusNetComponentAdapter implements IDistributedDataManager {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataManager.class);
	public static final String LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY = "net.dd.local";
	private static final boolean IS_LOCAL_DEFAULT_VALUE = true;

	private static IOdysseusNodeCommunicator communicator;
	private static IOdysseusNodeConnectionManager connectionManager;

	private final Collection<IDistributedDataListener> listenerCache = Lists.newArrayList();

	private IOdysseusNode localNode;

	private IDistributedDataContainer container;
	private IDistributedDataCreator creator;

	private DistributedDataServer server;

	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		communicator = serv;

		communicator.registerMessageType(CreateDistributedDataMessage.class);
		communicator.registerMessageType(DistributedDataCreatedMessage.class);
		communicator.registerMessageType(DestroyDistributedDataWithUUIDMessage.class);
		communicator.registerMessageType(DestroyDistributedDataWithNameMessage.class);
		communicator.registerMessageType(DestroyDistributedDataWithNodeIDMessage.class);
		communicator.registerMessageType(DistributedDataDestroyedMessage.class);
		communicator.registerMessageType(MultipleDistributedDataDestroyedMessage.class);
		communicator.registerMessageType(RequestUUIDsMessage.class);
		communicator.registerMessageType(UUIDsMessage.class);
		communicator.registerMessageType(RequestNamesMessage.class);
		communicator.registerMessageType(NamesMessage.class);
		communicator.registerMessageType(RequestOdysseusNodeIDsMessage.class);
		communicator.registerMessageType(OdysseusNodeIDsMessage.class);

		communicator.registerMessageType(GetUUIDMessage.class);
		communicator.registerMessageType(GetNameMessage.class);
		communicator.registerMessageType(GetOdysseusNodeIDMessage.class);
		communicator.registerMessageType(OptionalDistributedDataMessage.class);
		communicator.registerMessageType(DistributedDataCollectionMessage.class);
		communicator.registerMessageType(ContainsUUIDMessage.class);
		communicator.registerMessageType(ContainsNameMessage.class);
		communicator.registerMessageType(ContainsOdysseusNodeIDMessage.class);
		communicator.registerMessageType(BooleanMessage.class);

		communicator.registerMessageType(AddDistributedDataMessage.class);
		communicator.registerMessageType(ModifiedDistributedDataMessage.class);
		communicator.registerMessageType(RemoveDistributedDataMessage.class);

		communicator.registerMessageType(AddListenerMessage.class);
		communicator.registerMessageType(RemoveListenerMessage.class);

	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (communicator == serv) {
			communicator.unregisterMessageType(CreateDistributedDataMessage.class);
			communicator.unregisterMessageType(DistributedDataCreatedMessage.class);
			communicator.unregisterMessageType(DestroyDistributedDataWithUUIDMessage.class);
			communicator.unregisterMessageType(DestroyDistributedDataWithNameMessage.class);
			communicator.unregisterMessageType(DestroyDistributedDataWithNodeIDMessage.class);
			communicator.unregisterMessageType(DistributedDataDestroyedMessage.class);
			communicator.unregisterMessageType(MultipleDistributedDataDestroyedMessage.class);
			communicator.unregisterMessageType(RequestUUIDsMessage.class);
			communicator.unregisterMessageType(UUIDsMessage.class);
			communicator.unregisterMessageType(RequestNamesMessage.class);
			communicator.unregisterMessageType(NamesMessage.class);
			communicator.unregisterMessageType(RequestOdysseusNodeIDsMessage.class);
			communicator.unregisterMessageType(OdysseusNodeIDsMessage.class);

			communicator.unregisterMessageType(GetUUIDMessage.class);
			communicator.unregisterMessageType(GetNameMessage.class);
			communicator.unregisterMessageType(OptionalDistributedDataMessage.class);
			communicator.unregisterMessageType(DistributedDataCollectionMessage.class);
			communicator.unregisterMessageType(ContainsUUIDMessage.class);
			communicator.unregisterMessageType(ContainsNameMessage.class);
			communicator.unregisterMessageType(ContainsOdysseusNodeIDMessage.class);
			communicator.unregisterMessageType(BooleanMessage.class);

			communicator.unregisterMessageType(AddDistributedDataMessage.class);
			communicator.unregisterMessageType(ModifiedDistributedDataMessage.class);
			communicator.unregisterMessageType(RemoveDistributedDataMessage.class);

			communicator.unregisterMessageType(AddListenerMessage.class);
			communicator.unregisterMessageType(RemoveListenerMessage.class);

			communicator = null;
		}
	}
	
	// called by OSGi-DS
	public void bindDistributedDataListener(IDistributedDataListener serv) {
		addListener(serv);
	}

	// called by OSGi-DS
	public void unbindDistributedDataListener(IDistributedDataListener serv) {
		removeListener(serv);
	}

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

	@Override
	public void init(IOdysseusNode localNode) throws OdysseusNetException {
		localNode.addProperty(LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY, String.valueOf(isLocal()));

		this.localNode = localNode;
	}

	@Override
	public void start() throws OdysseusNetException {
		LOG.info("Starting distributed data manager");
		
		synchronized (listenerCache) {

			if (isLocal()) {
				container = new LocalDistributedDataContainer(this, communicator, connectionManager);
				creator = new LocalDistributedDataCreator(container);
				server = new DistributedDataServer(communicator, creator, this);
			} else {
				container = new RemoteDistributedDataContainer(this, communicator, connectionManager);
				creator = new RemoteDistributedDataCreator(communicator, connectionManager);
			}

			if (!listenerCache.isEmpty()) {
				for (IDistributedDataListener listener : listenerCache) {
					container.addListener(listener);
				}

				listenerCache.clear();
			}
		}
	}

	@Override
	public void stop() {
		if (server != null) {
			server.stop();
		}
		if (creator != null) {
			creator.dispose();
		}
		if (container != null) {
			container.dispose();
		}
		
		LOG.info("Stopped distributed data manager");
	}

	@Override
	public void terminate(IOdysseusNode localNode) {
		this.localNode = null;
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime) throws DistributedDataException {
		return creator.create(localNode.getID(), data, name, persistent, lifetime);
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent) throws DistributedDataException {
		return creator.create(localNode.getID(), data, name, persistent);
	}

	@Override
	public Optional<IDistributedData> destroy(UUID uuid) throws DistributedDataException {
		return creator.destroy(localNode.getID(), uuid);
	}

	@Override
	public Collection<IDistributedData> destroy(String name) throws DistributedDataException {
		return creator.destroy(localNode.getID(), name);
	}

	@Override
	public Collection<IDistributedData> destroyOwn() throws DistributedDataException {
		return creator.destroy(localNode.getID(), localNode.getID());
	}

	@Override
	public Collection<UUID> getUUIDs() throws DistributedDataException {
		return container.getUUIDs();
	}

	@Override
	public Collection<String> getNames() throws DistributedDataException {
		return container.getNames();
	}
	
	@Override
	public Collection<OdysseusNodeID> getOdysseusNodeIDs() throws DistributedDataException {
		return container.getOdysseusNodeIDs();
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) throws DistributedDataException {
		return container.get(uuid);
	}

	@Override
	public Collection<IDistributedData> get(String name) throws DistributedDataException {
		return container.get(name);
	}
	
	@Override
	public Collection<IDistributedData> get(OdysseusNodeID nodeID) throws DistributedDataException {
		return container.get(nodeID);
	}
	
	@Override
	public Collection<IDistributedData> getOwn() throws DistributedDataException {
		return container.get(localNode.getID());
	}

	@Override
	public boolean containsUUID(UUID uuid) throws DistributedDataException {
		return container.containsUUID(uuid);
	}

	@Override
	public boolean containsName(String name) throws DistributedDataException {
		return container.containsName(name);
	}
	
	@Override
	public boolean containsOdysseusNodeID(OdysseusNodeID nodeID) throws DistributedDataException {
		return container.containsOdysseusNodeID(nodeID);
	}

	@Override
	public boolean isLocal() {
		String isLocalStr = OdysseusNetConfiguration.get(LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY, "" + IS_LOCAL_DEFAULT_VALUE);
		return tryToBoolean(isLocalStr, IS_LOCAL_DEFAULT_VALUE);
	}

	private static boolean tryToBoolean(String isLocalStr, boolean def) {
		try {
			return Boolean.valueOf(isLocalStr);
		} catch (Throwable t) {
			return def;
		}
	}

	@Override
	public void addListener(IDistributedDataListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listenerCache) {
			if (container != null) {
				container.addListener(listener);
			} else {
				listenerCache.add(listener);
			}
		}
	}

	@Override
	public void removeListener(IDistributedDataListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listenerCache) {
			container.removeListener(listener);
			listenerCache.remove(listener);
		}
	}
}
