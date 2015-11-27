package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentAdapter;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.container.DistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.create.LocalDistributedDataCreator;

public class DistributedDataManager extends OdysseusNetComponentAdapter implements IDistributedDataManager {
	
	private IOdysseusNode localNode;
	
	private IDistributedDataContainer container;
	private IDistributedDataCreator creator;
	
	@Override
	public void init(IOdysseusNode localNode) throws OdysseusNetException {
		this.localNode = localNode;
	}
	
	@Override
	public void start() throws OdysseusNetException {
		container = new DistributedDataContainer();
		creator = new LocalDistributedDataCreator(container, localNode);
	}
	
	@Override
	public void terminate(IOdysseusNode localNode) {
		this.localNode = null;
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime) {
		return creator.create(data, name, persistent, lifetime);
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent) {
		return creator.create(data, name, persistent);
	}

	@Override
	public Optional<IDistributedData> destroy(UUID uuid) {
		return creator.destroy(uuid);
	}

	@Override
	public Collection<IDistributedData> destory(String name) {
		return creator.destroy(name);
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

}
