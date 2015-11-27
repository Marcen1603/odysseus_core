package de.uniol.inf.is.odysseus.net.data.impl.create;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataCreator;

public class LocalDistributedDataCreator implements IDistributedDataCreator {

	private final IDistributedDataContainer container;
	private final IOdysseusNode localNode;

	public LocalDistributedDataCreator(IDistributedDataContainer container, IOdysseusNode localNode) {
		Preconditions.checkNotNull(container, "container must not be null!");
		Preconditions.checkNotNull(localNode, "localNode must not be null!");

		this.localNode = localNode;
		this.container = container;
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean isPersistent, long lifetime) {
		Preconditions.checkNotNull(data, "data must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		UUID uuid = UUID.randomUUID();
		long timestamp = System.currentTimeMillis();
		IDistributedData distributedData = new DistributedData(uuid, name, data, isPersistent, lifetime, timestamp, localNode.getID());
		
		container.add(distributedData);
		
		return distributedData;
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent) {
		return create(data, name, persistent, -1L);
	}

	@Override
	public Optional<IDistributedData> destroy(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");
		
		Optional<IDistributedData> optDistributedData = container.get(uuid);
		if( optDistributedData.isPresent() ) {
			IDistributedData distributedData = optDistributedData.get();
			if( distributedData.getCreator().equals(localNode.getID())) {
				container.remove(distributedData);
				return Optional.of(distributedData);
			}
		}
		
		return Optional.absent();
	}

	@Override
	public Collection<IDistributedData> destory(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		Collection<IDistributedData> destroyedData = Lists.newArrayList();
		Collection<IDistributedData> distributedDatas = container.get(name);
		for( IDistributedData distributedData : distributedDatas ) {
			Optional<IDistributedData> optDestroyed = destroy(distributedData.getUUID());
			if(optDestroyed.isPresent() ) {
				destroyedData.add(optDestroyed.get());
			}
		}
		
		return destroyedData;
	}

}
