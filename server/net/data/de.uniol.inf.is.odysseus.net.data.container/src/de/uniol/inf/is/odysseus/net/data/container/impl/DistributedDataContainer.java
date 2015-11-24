package de.uniol.inf.is.odysseus.net.data.container.impl;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.container.IDistributedDataContainer;

public class DistributedDataContainer implements IDistributedDataContainer {

	private final Map<String, Collection<IDistributedData>> ddNameMap = Maps.newHashMap();
	private final Map<UUID, IDistributedData> ddUUIDMap = Maps.newHashMap();
	
	private final Object syncObject = new Object();
		
	@Override
	public void add(IDistributedData data) {
		Preconditions.checkNotNull(data, "data must not be null!");
		UUID uuid = data.getUUID();
		long ts = data.getTimestamp();
		String name = data.getName();
		
		synchronized( syncObject ) {
			if( ddUUIDMap.containsKey(uuid)) {
				IDistributedData oldData = ddUUIDMap.get(uuid);
				long oldts = oldData.getTimestamp();
				if( oldts < ts ) {
					ddUUIDMap.put(uuid, data);
					
					Collection<IDistributedData> dataCollection = ddNameMap.get(name);
					dataCollection.remove(oldData);
					dataCollection.add(data);
				}
				
			} else {
				ddUUIDMap.put(uuid, data);
				
				Collection<IDistributedData> dataCollection = ddNameMap.get(name);
				if( dataCollection == null ) {
					dataCollection = Lists.newArrayList();
					ddNameMap.put(name, dataCollection);
				}
				dataCollection.add(data);
			}
		}
	}

	@Override
	public Optional<IDistributedData> remove(IDistributedData data) {
		Preconditions.checkNotNull(data, "data must not be null!");

		synchronized( syncObject ) {
			Collection<IDistributedData> dataCollection = ddNameMap.get(data.getName());
			if( dataCollection != null ) {
				dataCollection.remove(data);
				if( dataCollection.isEmpty() ) {
					ddNameMap.remove(data.getName());
				}
			}
			
			IDistributedData data2 = ddUUIDMap.get(data.getUUID());
			if( data2 != null ) {
				ddUUIDMap.remove(data2.getUUID());
				return Optional.of(data2);
			}
			
		}
		
		return Optional.absent();
	}

	@Override
	public Optional<IDistributedData> remove(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		synchronized( syncObject ) {
			IDistributedData oldData = ddUUIDMap.get(uuid);
			if( oldData != null ) {
				return remove(oldData);
			} 
		}
		
		return Optional.absent();
	}

	@Override
	public Collection<IDistributedData> remove(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		Collection<IDistributedData> removedData = Lists.newArrayList();
		
		synchronized( syncObject ) {
			Collection<IDistributedData> dataCollection = Lists.newArrayList(ddNameMap.get(name));
			if( dataCollection != null ) {
				for( IDistributedData data : dataCollection ) {
					remove(data);
				}
				
				removedData.addAll(dataCollection);
			}
		}
		
		return removedData;
	}

	@Override
	public Collection<UUID> getUUIDs() {
		synchronized( syncObject ) {
			return Lists.newArrayList(ddUUIDMap.keySet());
		}
	}

	@Override
	public Collection<String> getNames() {
		synchronized( syncObject ) {
			return Lists.newArrayList(ddNameMap.keySet());
		}
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		synchronized( syncObject ) {
			return Optional.fromNullable(ddUUIDMap.get(uuid));
		}
	}

	@Override
	public Collection<IDistributedData> get(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		synchronized( syncObject ) {
			Collection<IDistributedData> dataCollection = ddNameMap.get(name);
			if( dataCollection == null || dataCollection.isEmpty()) {
				return Lists.newArrayList();
			}
			
			return Lists.newArrayList(dataCollection);
		}
	}

	@Override
	public boolean containsUUID(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		synchronized( syncObject ) {
			return ddUUIDMap.containsKey(uuid);
		}
	}

	@Override
	public boolean containsName(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");

		synchronized( syncObject ) {
			return ddNameMap.containsKey(name);
		}
	}
	
}
