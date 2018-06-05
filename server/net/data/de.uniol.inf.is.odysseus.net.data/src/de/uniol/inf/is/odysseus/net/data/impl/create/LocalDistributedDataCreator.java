package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOG = LoggerFactory.getLogger(LocalDistributedDataCreator.class);

	private final IDistributedDataContainer container;

	public LocalDistributedDataCreator(IDistributedDataContainer container) {
		Preconditions.checkNotNull(container, "container must not be null!");

		this.container = container;

		LOG.info("Local distributed data creator created");
	}

	@Override
	public void dispose() {
		LOG.info("Local distributed data created disposed");
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

		LOG.info("Created new distributed data: {}", distributedData);

		return distributedData;
	}

	@Override
	public IDistributedData create(OdysseusNodeID creator, JSONObject data, String name, boolean persistent) throws DistributedDataException {
		return create(creator, data, name, persistent, -1L);
	}
	
	@Override
	public Optional<IDistributedData> update(OdysseusNodeID creator, UUID uuid, JSONObject data) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkNotNull(uuid, "uuid must not be null!");
		Preconditions.checkNotNull(data, "data must not be null!");

		LOG.info("Trying to update distributed data with uuid {}", uuid);
		
		Optional<IDistributedData> oldData = container.get(uuid);
		if (oldData.isPresent()) {
			if (oldData.get().getCreator().equals(creator)) {
				IDistributedData newData = new DistributedData(oldData.get().getUUID(), oldData.get().getName(), data, oldData.get().isPersistent(), oldData.get().getLifetimeMillis(), System.currentTimeMillis(), creator);
				container.add(newData);
				LOG.info("Updated distributed data {} to {}", oldData.get(), newData);
				return Optional.of(newData);
			}

			LOG.info("Did not update distributed data since it has an other creator node: {} instead of {}", creator, oldData.get().getCreator());
		} else {
			LOG.info("Specified distributed data with uuid {} not found for update", uuid);
		}

		return Optional.absent();
	}

	@Override
	public Optional<IDistributedData> destroy(OdysseusNodeID creator, UUID uuid) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		LOG.info("Trying to destroy distributed data with uuid {}", uuid);
		
		Optional<IDistributedData> optDistributedData = container.get(uuid);
		if (optDistributedData.isPresent()) {
			IDistributedData distributedData = optDistributedData.get();
			if (distributedData.getCreator().equals(creator)) {
				container.remove(distributedData);

				LOG.info("Destroyed distributed data {}", distributedData);
				return Optional.of(distributedData);
			}

			LOG.info("Did not destroy distributed data since it has an other creator node: {} instead of {}", creator, distributedData.getCreator());
		} else {
			LOG.info("Specified distributed data with uuid {} not found for destroy", uuid);
		}

		return Optional.absent();
	}

	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, String name) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		LOG.info("Trying to destroy distributed data with name {}", name);
		
		Collection<IDistributedData> destroyedData = Lists.newArrayList();
		Collection<IDistributedData> distributedDatas = container.get(name);
		if (!distributedDatas.isEmpty()) {
			for (IDistributedData distributedData : distributedDatas) {
				Optional<IDistributedData> optDestroyed = destroy(creator, distributedData.getUUID());
				if (optDestroyed.isPresent()) {
					destroyedData.add(optDestroyed.get());
				}
			}
		} else {
			LOG.info("No distributed data with name {} found", name);
		}

		return destroyedData;
	}

	@Override
	public Collection<IDistributedData> destroy(OdysseusNodeID creator, OdysseusNodeID nodeID) throws DistributedDataException {
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkNotNull(nodeID, "id must not be null!");

		LOG.info("Trying to destroy distributed data with nodeID {}", nodeID);
		
		Collection<IDistributedData> destroyedDatas = Lists.newArrayList();
		Collection<IDistributedData> distributedDatas = container.get(nodeID);
		if( !distributedDatas.isEmpty() ) {
			for (IDistributedData distributedData : distributedDatas) {
				Optional<IDistributedData> optDestroyed = destroy(creator, distributedData.getUUID());
				if (optDestroyed.isPresent()) {
					destroyedDatas.add(optDestroyed.get());
				}
			}
		} else {
			LOG.info("No distributed data with nodeID {} found", nodeID);
		}

		return destroyedDatas;
	}

}
