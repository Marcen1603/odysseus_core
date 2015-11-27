package de.uniol.inf.is.odysseus.net.data.impl.container;

import java.util.Collection;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataContainer;

public class RemoteDistributedDataContainer implements IDistributedDataContainer {

//	private final IOdysseusNodeCommunicator communicator;
	
	public RemoteDistributedDataContainer(IOdysseusNodeCommunicator communicator) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");

//		this.communicator = communicator;
	}

	@Override
	public void add(IDistributedData data) {
		
	}

	@Override
	public Optional<IDistributedData> remove(IDistributedData data) {
		return null;
	}

	@Override
	public Optional<IDistributedData> remove(UUID uuid) {
		return null;
	}

	@Override
	public Collection<IDistributedData> remove(String name) {
		return null;
	}

	@Override
	public Collection<UUID> getUUIDs() {
		return null;
	}

	@Override
	public Collection<String> getNames() {
		return null;
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) {
		return null;
	}

	@Override
	public Collection<IDistributedData> get(String name) {
		return null;
	}

	@Override
	public boolean containsUUID(UUID uuid) {
		return false;
	}

	@Override
	public boolean containsName(String name) {
		return false;
	}

	@Override
	public void dispose() {
		
	}

}
