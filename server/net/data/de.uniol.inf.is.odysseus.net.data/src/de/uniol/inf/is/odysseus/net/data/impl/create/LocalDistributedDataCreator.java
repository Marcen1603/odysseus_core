package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataCreator;

public class LocalDistributedDataCreator implements IDistributedDataCreator {

	private final IDistributedDataContainer container;

	public LocalDistributedDataCreator(IDistributedDataContainer container) {
		Preconditions.checkNotNull(container, "container must not be null!");

		this.container = container;
	}

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean isPersistent, long lifetime) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkNotNull(data, "data must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		UUID uuid = UUID.randomUUID();
		long timestamp = System.currentTimeMillis();
		IDistributedData distributedData = new DistributedData(uuid, name, data, isPersistent, lifetime, timestamp, creator);

		container.add(distributedData);

		return distributedData;
	}

	@Override
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent) throws DistributedDataException {
		return create(creator, data, name, persistent, -1L);
	}

	@Override
	public Optional<IDistributedData> destroy(OdysseusNodeID creator, UUID uuid) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		Optional<IDistributedData> optDistributedData = container.get(uuid);
		if (optDistributedData.isPresent()) {
			IDistributedData distributedData = optDistributedData.get();
			if (distributedData.getCreator().equals(creator)) {
				container.remove(distributedData);
				return Optional.of(distributedData);
			}
		}

		return Optional.absent();
	}

	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, String name) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		Collection<IDistributedData> destroyedData = Lists.newArrayList();
		Collection<IDistributedData> distributedDatas = container.get(name);
		for (IDistributedData distributedData : distributedDatas) {
			Optional<IDistributedData> optDestroyed = destroy(creator, distributedData.getUUID());
			if (optDestroyed.isPresent()) {
				destroyedData.add(optDestroyed.get());
			}
		}

		return destroyedData;
	}

	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID id) throws DistributedDataException {
		Preconditions.checkNotNull(id, "id must not be null!");

		Collection<IDistributedData> destroyedDatas = Lists.newArrayList();
		Collection<IDistributedData> distributedDatas = container.get(id);
		for (IDistributedData distributedData : distributedDatas) {
			Optional<IDistributedData> optDestroyed = destroy(id, distributedData.getUUID());
			if (optDestroyed.isPresent()) {
				destroyedDatas.add(optDestroyed.get());
			}
		}

		return destroyedDatas;
	}
}
