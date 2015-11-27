package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.container.LocalDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.container.RemoteDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.create.LocalDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.create.RemoteDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.message.CreateDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.MultipleDistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.server.DistributedDataServer;

public class DistributedDataManager extends OdysseusNetComponentAdapter implements IDistributedDataManager {
	
	public static final String LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY = "net.dd.local";
	
	private static final boolean IS_LOCAL_DEFAULT_VALUE = true;

	private static IOdysseusNodeCommunicator communicator;
	private static IOdysseusNodeConnectionManager connectionManager;
	
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
		communicator.registerMessageType(DistributedDataDestroyedMessage.class);
		communicator.registerMessageType(MultipleDistributedDataDestroyedMessage.class);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (communicator == serv) {
			communicator.unregisterMessageType(CreateDistributedDataMessage.class);
			communicator.unregisterMessageType(DistributedDataCreatedMessage.class);
			communicator.unregisterMessageType(DestroyDistributedDataWithUUIDMessage.class);
			communicator.unregisterMessageType(DestroyDistributedDataWithNameMessage.class);
			communicator.unregisterMessageType(DistributedDataDestroyedMessage.class);
			communicator.unregisterMessageType(MultipleDistributedDataDestroyedMessage.class);

			communicator = null;
		}
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
		if( isLocal() ) {
			container = new LocalDistributedDataContainer();
			creator = new LocalDistributedDataCreator(container);
			server = new DistributedDataServer(communicator, creator);
		} else {
			container = new RemoteDistributedDataContainer(communicator); 
			creator = new RemoteDistributedDataCreator(communicator, connectionManager);
		}
	}
	
	@Override
	public void stop() {
		if( server != null ) {
			server.stop();
		}
		if( creator != null ) {
			creator.dispose();
		}
		if( container != null ) {
			container.dispose();
		}
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
	public Collection<UUID> getUUIDs() {
		return container.getUUIDs();
	}

	@Override
	public Collection<String> getNames() {
		return container.getNames();
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) {
		return container.get(uuid);
	}

	@Override
	public Collection<IDistributedData> get(String name) {
		return container.get(name);
	}

	@Override
	public boolean containsUUID(UUID uuid) {
		return container.containsUUID(uuid);
	}

	@Override
	public boolean containsName(String name) {
		return container.containsName(name);
	}

	@Override
	public boolean isLocal() {
		String isLocalStr = OdysseusNetConfiguration.get(LOCAL_DISTRIBUTED_DATA_CONTAINER_KEY, "" + IS_LOCAL_DEFAULT_VALUE);
		return tryToBoolean(isLocalStr, IS_LOCAL_DEFAULT_VALUE);
	}

	private static boolean tryToBoolean(String isLocalStr, boolean def) {
		try {
			return Boolean.valueOf(isLocalStr);
		} catch( Throwable t ) {
			return def;
		}
	}

}
